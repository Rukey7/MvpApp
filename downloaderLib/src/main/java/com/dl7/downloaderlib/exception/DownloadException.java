package com.dl7.downloaderlib.exception;

/**
 * Created by long on 2016/5/26.
 * 下载异常
 */
public class DownloadException extends RuntimeException {

    public DownloadException(String detailMessage) {
        super(detailMessage);
    }
}
