package com.dl7.downloaderlib.service;

import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import com.dl7.downloaderlib.DownloadListener;
import com.dl7.downloaderlib.db.FileDAOImpl;
import com.dl7.downloaderlib.entity.FileInfo;
import com.dl7.downloaderlib.exception.DownloadException;
import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.downloaderlib.model.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.Locale;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by long on 2016/5/25.
 * 下载线程
 */
public class DownloadTask implements Runnable {
    private final static String TAG = "DownloadTask";

    private static final int MIN_PROCESS_STEP = 65535;
    private static final int MIN_PROCESS_TIME = 2000;
    private static final int CALC_SPEED_TIME = 500;
    private static final int BUFFER_SIZE = 1024 * 4;
    private final OkHttpClient mClient;
    private FileInfo mFileInfo;
    private int mRetryTimes;
    private final DownloadListener mListener;
    private boolean mIsResumeAvailable = false;
    private boolean mIsRunning = false;
    private boolean mIsCancel = false;
    private boolean mIsStop = false;
    private boolean mIsRetry = false;
    private int mLastUpdateBytes;
    private long mLastUpdateTime;
    private int mLastCalcBytes;
    private long mLastCalcTime;


    public DownloadTask(OkHttpClient client, FileInfo fileInfo, int retryTimes, DownloadListener listener) {
        this.mClient = client;
        this.mFileInfo = fileInfo;
        this.mRetryTimes = retryTimes;
        this.mListener = listener;
    }

    public void setRetry(boolean retry) {
        mIsRetry = retry;
    }

    @Override
    public void run() {
        mIsRunning = true;
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        // 1.检测是否存在未下载完的文件
        _checkIsResumeAvailable();

        Response response = null;
        Call call = null;
        try {
            // 2.构建请求
            Request.Builder requestBuilder = new Request.Builder().url(mFileInfo.getUrl());
            requestBuilder.addHeader("Range", String.format(Locale.ENGLISH, "bytes=%d-", mFileInfo.getLoadBytes()));
            // 目前没有指定cache，下载任务非普通REST请求，用户已经有了存储的地方
            requestBuilder.cacheControl(CacheControl.FORCE_NETWORK);

            // 3.初始化请求
            Request request = requestBuilder.build();
            call = mClient.newCall(request);

            // 4.执行请求
            response = call.execute();

            final boolean isSucceedStart = response.code() == HttpURLConnection.HTTP_OK;
            final boolean isSucceedResume = response.code() == HttpURLConnection.HTTP_PARTIAL;

            if (isSucceedResume || isSucceedStart) {
                int total = mFileInfo.getTotalBytes();
                final String transferEncoding = response.header("Transfer-Encoding");

                // 5.获取文件长度
                if (isSucceedStart || total <= 0) {
                    if (transferEncoding == null) {
                        total = (int) response.body().contentLength();
                    } else {
                        // if transfer not nil, ignore content-length
                        total = -1;
                    }
                    mFileInfo.setTotalBytes(total);
                }
                if (total < 0) {
                    throw new DownloadException("Get content length error!");
                }

                // 6.网络状态已连接
                _onConnected();
                // 7.开始获取数据
                _onDownload(response);
            } else {
                if (!_onRetry()) {
                    mListener.onError(mFileInfo, "Numeric status code is error!");
                    _updateDb();
                }
            }
        } catch (Throwable e) {
            if (e instanceof DownloadException) {
                if (_onRetry()) {
                    return;
                }
            }
            mListener.onError(mFileInfo, e.toString());
            _updateDb();
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
            if (call != null) {
                call.cancel();
            }
        }
    }

    /**
     * 重试处理
     *
     * @return 是否进行处理
     */
    private boolean _onRetry() {
        mIsRunning = false;
        if (mRetryTimes > 0) {
            mRetryTimes--;
            DownloadTask runnable = new DownloadTask(mClient, mFileInfo, mRetryTimes, mListener);
            runnable.setRetry(true);
            DownloadThreadPool.getInstance().execute(runnable);
            return true;
        }
        return false;
    }

    /**
     * 开始下载
     *
     * @param response 应答
     */
    private void _onDownload(Response response) throws Throwable {
        InputStream inputStream = null;
        final RandomAccessFile accessFile = FileUtils.getRandomAccessFile(
                mFileInfo.getPath() + mFileInfo.getName() + ".tmp",
                mFileInfo.getLoadBytes(), mFileInfo.getTotalBytes());
        int loadBytes = mFileInfo.getLoadBytes();
        try {
            // 1.获取数据流
            inputStream = response.body().byteStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
            byte[] buff = new byte[BUFFER_SIZE];
            do {
                // 2.读数据
                int byteCount = bufferedInputStream.read(buff);
                if (byteCount == -1) {
                    break;
                }
                // 3.写文件
                accessFile.write(buff, 0, byteCount);
                // 4.累加下载量
                loadBytes += byteCount;

                // 5.检测文件是否被其他操作
                if (accessFile.length() < loadBytes) {
                    mIsRunning = false;
                    throw new RuntimeException(
                            FileUtils.formatString("the file was changed by others when" +
                                    " downloading. %d %d", accessFile.length(), loadBytes));
                } else {
                    mFileInfo.setLoadBytes(loadBytes);
                    _onProcess();
                }

                // 6.检测停止状态
                if (mIsCancel || mIsStop) {
                    // callback on paused
                    mIsRunning = false;
                    if (mIsCancel) {
                        mListener.onCancel(mFileInfo);
                    } else {
                        mListener.onStop(mFileInfo);
                    }
                    _updateDb();
                    return;
                }
            } while (mIsRunning);

            // 7.处理 transfer encoding = chunked 的情况
            if (mFileInfo.getTotalBytes() == -1) {
                mFileInfo.setTotalBytes(loadBytes);
            }

            // 8.判断下载成功还是失败
            if (mFileInfo.getLoadBytes() == mFileInfo.getTotalBytes()) {
                _onComplete();
            } else {
                throw new DownloadException(FileUtils.formatString(
                        "Unfinished: load[%d] is not equal total[%d]!",
                        mFileInfo.getLoadBytes(), mFileInfo.getTotalBytes()));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (accessFile != null) {
                accessFile.close();
            }
        }
    }

    /**
     * 下载完成
     */
    private void _onComplete() {
        // 重命名文件
        File tmpFile = new File(mFileInfo.getPath(), mFileInfo.getName() + ".tmp");
        File appFile = new File(mFileInfo.getPath(), mFileInfo.getName());
        tmpFile.renameTo(appFile);
        mListener.onComplete(mFileInfo);
//        // 下载完就从数据库删除
//        FileDAOImpl.getInstance().delete(mFileInfo.getUrl());
        _updateDb();
    }

    /**
     * 更新进度
     */
    private void _onProcess() {
        if (mFileInfo.getLoadBytes() == mFileInfo.getTotalBytes()) {
            // 下载完成
            mIsRunning = false;
            return;
        }

        long now = SystemClock.uptimeMillis();
        if (mLastCalcTime == 0) {
            mLastCalcTime = now;
            mLastCalcBytes = mFileInfo.getLoadBytes();
        }

        long diffBytes;
        long diffTimes = now - mLastCalcTime;
        if (diffTimes > CALC_SPEED_TIME) {
            diffBytes = mFileInfo.getLoadBytes() - mLastCalcBytes;
            int speed = (int) (diffBytes * 1000 / diffTimes);
            mFileInfo.setSpeed(speed);
            mLastCalcTime = now;
            mLastCalcBytes = mFileInfo.getLoadBytes();
        }
        mListener.onUpdate(mFileInfo);

        diffBytes = mFileInfo.getLoadBytes() - mLastUpdateBytes;
        diffTimes = now - mLastUpdateTime;
        if (diffBytes > MIN_PROCESS_STEP && diffTimes > MIN_PROCESS_TIME) {
            _updateDb();
            mLastUpdateBytes = mFileInfo.getLoadBytes();
            mLastUpdateTime = now;
        }


    }

    /**
     * 连接已建立，准备下载
     */
    private void _onConnected() {
        mIsRunning = true;
        if (!mIsRetry) {
            // 如果为异常重试则不进行回调
            mListener.onStart(mFileInfo);
        }
        if (!mIsResumeAvailable) {
            // 插入数据库
            mIsResumeAvailable = true;
            DownloadThreadPool.getInstance().update(new Runnable() {
                @Override
                public void run() {
                    FileDAOImpl.getInstance().insert(mFileInfo);
                }
            });
        }
    }

    /**
     * 检测是否存在资源
     */
    private void _checkIsResumeAvailable() {
        FileInfo info = FileDAOImpl.getInstance().query(mFileInfo.getUrl());
        if (info != null) {
            mIsResumeAvailable = true;
            if (info.getStatus() != DownloadStatus.COMPLETE && info.getLoadBytes() != info.getTotalBytes()) {
                mFileInfo = info;
            }
        }
    }

    /**
     * 判断是否正在执行
     *
     * @return
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * 线程的标识
     *
     * @return
     */
    public String tag() {
        return mFileInfo.getUrl();
    }

    /**
     * 取消
     */
    public void cancel() {
        mIsCancel = true;
    }

    /**
     * 停止
     */
    public void stop() {
        mIsStop = true;
    }

    /**
     * 更新数据库
     */
    private void _updateDb() {
        DownloadThreadPool.getInstance().update(new Runnable() {
            @Override
            public void run() {
                FileDAOImpl.getInstance().update(mFileInfo);
            }
        });
    }
}
