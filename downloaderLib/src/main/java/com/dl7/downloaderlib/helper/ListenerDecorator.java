package com.dl7.downloaderlib.helper;

import com.dl7.downloaderlib.DownloadListener;
import com.dl7.downloaderlib.entity.FileInfo;
import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.downloaderlib.service.DownloadThreadPool;

/**
 * Created by long on 2016/5/26.
 * 封装下载监听器
 */
public final class ListenerDecorator implements DownloadListener {

    private final DownloadListener mListener;
    private final boolean mIsUiThread;

    public ListenerDecorator(DownloadListener listener, boolean isUiThread) {
        this.mListener = listener;
        this.mIsUiThread = isUiThread;
    }


    @Override
    public void onStart(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.START);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onStart(fileInfo);
                }
            });
        } else {
            mListener.onStart(fileInfo);
        }
    }

    @Override
    public void onUpdate(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.DOWNLOADING);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onUpdate(fileInfo);
                }
            });
        } else {
            mListener.onUpdate(fileInfo);
        }
    }

    @Override
    public void onStop(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.STOP);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onStop(fileInfo);
                }
            });
        } else {
            mListener.onStop(fileInfo);
        }
    }

    @Override
    public void onComplete(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.COMPLETE);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onComplete(fileInfo);
                }
            });
        } else {
            mListener.onComplete(fileInfo);
        }
        DownloadThreadPool.getInstance().cancel(fileInfo.getUrl(), false);
    }

    @Override
    public void onCancel(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.CANCEL);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onCancel(fileInfo);
                }
            });
        } else {
            mListener.onCancel(fileInfo);
        }
        DownloadThreadPool.getInstance().cancel(fileInfo.getUrl(), true);
    }

    @Override
    public void onError(final FileInfo fileInfo, final String errorMsg) {
        fileInfo.setStatus(DownloadStatus.ERROR);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onError(fileInfo, errorMsg);
                }
            });
        } else {
            mListener.onError(fileInfo, errorMsg);
        }
        DownloadThreadPool.getInstance().cancel(fileInfo.getUrl(), false);
    }
}
