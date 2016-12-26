package com.dl7.player.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by long on 2016/10/18.
 */

public final class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }


    /**
     * 时长格式化显示
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
//        int minutes = (totalSeconds / 60) % 60;
//        int hours = totalSeconds / 3600;
        return minutes > 99 ? String.format("%d:%02d", minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 下载速度格式化显示
     */
    public static String getFormatSize(int size) {
        long fileSize = (long) size;
        String showSize = "";
        if (fileSize >= 0 && fileSize < 1024) {
            showSize = fileSize + "Kb/s";
        } else if (fileSize >= 1024 && fileSize < (1024 * 1024)) {
            showSize = Long.toString(fileSize / 1024) + "KB/s";
        } else if (fileSize >= (1024 * 1024) && fileSize < (1024 * 1024 * 1024)) {
            showSize = Long.toString(fileSize / (1024 * 1024)) + "MB/s";
        }
        return showSize;
    }

    /**
     * 获取格式化当前时间
     * @return
     */
    public static String getCurFormatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
