package com.dl7.downloaderlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.dl7.downloaderlib.db.FileDAOImpl;
import com.dl7.downloaderlib.entity.FileInfo;
import com.dl7.downloaderlib.helper.ListenerDecorator;
import com.dl7.downloaderlib.service.DownloadTask;
import com.dl7.downloaderlib.service.DownloadThreadPool;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by 95 on 2016/5/25.
 * 文件下载器
 */
public class FileDownloader {

    private static Context sContext;
    private static DownloadConfig sConfig;
    private static OkHttpClient sOkHttpClient;


    private FileDownloader() {
        throw new RuntimeException("FileDownloader cannot be initialized!");
    }

    public static void init(Context context) {
        sContext = context;
        sConfig = new DownloadConfig.Builder().build();
        sOkHttpClient = new OkHttpClient();
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     * 设置配置参数
     *
     * @param config
     */
    public static void setConfig(DownloadConfig config) {
        sConfig = config;
    }

    /**
     * 获取下载目录
     *
     * @return
     */
    public static String getDownloadDir() {
        return sConfig.getDownloadDir();
    }

    /**
     * 获取路径
     *
     * @param url url
     * @return
     */
    public static String getFilePathByUrl(String url) {
        FileInfo info = FileDAOImpl.getInstance().query(url);
        if (info == null) {
            return null;
        }
        File file = new File(info.getPath(), info.getName());
        return file.getAbsolutePath();
    }

    /**
     * 获取路径
     *
     * @param name 文件名
     * @return
     */
    public static String getFilePathByName(String name) {
        FileInfo info = FileDAOImpl.getInstance().queryPkg(name);
        File file = new File(info.getPath(), info.getName());
        return file.getAbsolutePath();
    }

    /**
     * 获取一个任务最大允许的异常重连次数
     *
     * @return
     */
    public static int getRetryTimes() {
        return sConfig.getRetryTimes();
    }

    /**
     * 启动下载
     *
     * @param url      url
     * @param name     文件名
     * @param listener 监听器
     */
    public static void start(String url, String name, DownloadListener listener) {
        start(new FileInfo(url, name), listener, false);
    }

    /**
     * 启动下载
     *
     * @param url                  url
     * @param name                 文件名
     * @param listener             监听器
     * @param callBackInMainThread 是否在主线程中执行回调，需要切换线程效率上会受点影响
     */
    public static void start(String url, String name, DownloadListener listener, boolean callBackInMainThread) {
        start(new FileInfo(url, name), listener, callBackInMainThread);
    }

    public static void start(FileInfo fileInfo, DownloadListener listener) {
        start(fileInfo, listener, false);
    }

    public static void start(FileInfo fileInfo, DownloadListener listener, boolean callBackInMainThread) {
        ListenerDecorator listenerDecorator = new ListenerDecorator(listener, callBackInMainThread);
        if (!_isNetworkAvailable(sContext)) {
            Toast.makeText(sContext, "请检查网络连接!!", Toast.LENGTH_SHORT).show();
            listenerDecorator.onError(fileInfo, "网络连接异常!!");
            return;
        }
        if (_checkFileIsExists(fileInfo, listenerDecorator)) {
            return;
        }


        DownloadThreadPool.getInstance().execute(new DownloadTask(sOkHttpClient,
                fileInfo, getRetryTimes(), listenerDecorator));
    }

    /**
     * 暂停下载
     *
     * @param url url
     */
    public static void stop(String url) {
        DownloadThreadPool.getInstance().cancel(url, false);
    }

    /**
     * 停止所有线程
     */
    public static void stopAll() {
        DownloadThreadPool.getInstance().cancelAll();
    }

    /**
     * 取消下载并删除文件
     *
     * @param url url
     */
    public static void cancel(String url) {
        DownloadThreadPool.getInstance().cancel(url, true);
        FileInfo info = FileDAOImpl.getInstance().query(url);
        if (info != null) {
            File tmpFile = new File(info.getPath(), info.getName() + ".tmp");
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            FileDAOImpl.getInstance().delete(url);
        }
    }

    /**
     * 判断文件是否已经下载或正在下载
     *
     * @param fileInfo
     * @return
     */
    private static boolean _checkFileIsExists(FileInfo fileInfo, DownloadListener listener) {
        File file = new File(getDownloadDir(), fileInfo.getName());
        if (file.exists()) {
            // 已经存在则删除重下
            file.delete();
            if (FileDAOImpl.getInstance().isExists(fileInfo.getUrl())) {
                // 删除数据库中的记录
                FileDAOImpl.getInstance().delete(fileInfo.getUrl());
            }
            return false;
        }
        // 线程池中是否存在相应任务
        DownloadTask runnable = DownloadThreadPool.getInstance().getRunnable(fileInfo.getUrl());
        if (runnable != null) {
            if (runnable.isRunning()) {
                Toast.makeText(sContext, "正在下载...", Toast.LENGTH_SHORT).show();
            } else {
                Log.w("FileDownloader", "DownloadThreadPool");
                DownloadThreadPool.getInstance().execute(runnable);
            }
            return true;
        }
        return false;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    private static boolean _isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return null != info && info.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
