package com.dl7.downloaderlib.model;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 95 on 2016/5/25.
 * 下载状态
 */
public class DownloadStatus {

    private DownloadStatus() {
        throw new RuntimeException("DownloadStatus cannot be initialized!");
    }

    // 未下载
    public static final int NORMAL = 0;
    // 等待下载
    public static final int WAIT = 1;
    // 开始下载
    public static final int START = 2;
    // 正在下载
    public static final int DOWNLOADING = 3;
    // 停止下载
    public static final int STOP = 4;
    // 下载失败
    public static final int ERROR = 5;
    // 下载完成
    public static final int COMPLETE = 6;
    // 取消下载
    public static final int CANCEL = 7;
    // 安装中
    public static final int INSTALLING = 8;
    // 已经安装
    public static final int INSTALLED = 9;
    // 收藏状态
    public static final int COLLECT = 10;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @IntDef({NORMAL, WAIT, START, DOWNLOADING, STOP, COMPLETE, CANCEL, ERROR, INSTALLING, INSTALLED, COLLECT})
    public @interface DlStatus {}
}
