package com.dl7.downloaderlib;

import com.dl7.downloaderlib.entity.FileInfo;

/**
 * Created by long on 2016/5/26.
 * 下载监听器
 */
public interface DownloadListener {
    /**
     * 开始下载
     * @param fileInfo
     */
    void onStart(FileInfo fileInfo);
    /**
     * 更新下载进度
     * @param fileInfo
     */
    void onUpdate(FileInfo fileInfo);
    /**
     * 停止下载
     * @param fileInfo
     */
    void onStop(FileInfo fileInfo);
    /**
     * 下载成功
     * @param fileInfo
     */
    void onComplete(FileInfo fileInfo);
    /**
     * 取消下载
     * @param fileInfo
     */
    void onCancel(FileInfo fileInfo);
    /**
     * 下载失败
     * @param fileInfo
     */
    void onError(FileInfo fileInfo, String errorMsg);
}
