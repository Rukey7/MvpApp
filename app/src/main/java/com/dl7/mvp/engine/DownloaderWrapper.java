package com.dl7.mvp.engine;

import android.text.TextUtils;
import android.util.Log;

import com.dl7.downloaderlib.DownloadListener;
import com.dl7.downloaderlib.FileDownloader;
import com.dl7.downloaderlib.entity.FileInfo;
import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.mvp.utils.StringUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by long on 2016/12/16.
 * 下载器封装
 */
public final class DownloaderWrapper {

    private static RxBus sRxBus;

    private DownloaderWrapper() {
        throw new AssertionError();
    }

    public static void init(RxBus rxBus) {
        sRxBus = rxBus;
    }

    /**
     * 开始下载
     * @param info
     */
    public static void start(final VideoInfo info) {
        // 在真正处理前状态设为等待
        info.setDownloadStatus(DownloadStatus.WAIT);
        // 有高清就用高清的，没有用另一个
        if (TextUtils.isEmpty(info.getMp4Hd_url())) {
            info.setVideoUrl(info.getMp4_url());
        } else {
            info.setVideoUrl(info.getMp4Hd_url());
        }
        // 启动下载
        FileDownloader.start(info.getVideoUrl(), StringUtils.clipFileName(info.getVideoUrl()), new ListenerWrapper());
    }

    /**
     * 暂停下载
     * @param info
     */
    public static void stop(VideoInfo info) {
        FileDownloader.stop(info.getVideoUrl());
    }

    /**
     * 取消下载
     *
     * @param info
     */
    public static void cancel(VideoInfo info) {
        FileDownloader.cancel(info.getVideoUrl());
//        sRxBus.post(new FileInfo(DownloadStatus.CANCEL, info.getVideoUrl(),
//                StringUtils.clipFileName(info.getVideoUrl()), (int) info.getTotalSize()));
    }

    /**
     * 监听器封装类
     */
    static class ListenerWrapper implements DownloadListener {

        @Override
        public void onStart(FileInfo fileInfo) {
            sRxBus.post(fileInfo);
        }

        @Override
        public void onUpdate(FileInfo fileInfo) {
            sRxBus.post(fileInfo);
            Log.i("ListenerWrapper", fileInfo.toString());
        }

        @Override
        public void onStop(FileInfo fileInfo) {
            sRxBus.post(fileInfo);
        }

        @Override
        public void onComplete(FileInfo fileInfo) {
            sRxBus.post(fileInfo);
            Logger.e("onComplete " + fileInfo.toString());
        }

        @Override
        public void onCancel(FileInfo fileInfo) {
            sRxBus.post(fileInfo);
        }

        @Override
        public void onError(FileInfo fileInfo, String s) {
            Logger.e(s);
            sRxBus.post(fileInfo);
        }
    }

}
