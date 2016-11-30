package com.dl7.downloaderlib.model;

import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;

/**
 * Created by long on 2016/5/26.
 * 文件工具类
 */
public class FileUtils {

    private FileUtils() {
        throw new RuntimeException("FileUtils cannot be initialized!");
    }

    /**
     * 获取随机存取文件
     * @param path  文件路径
     * @param loadBytes 文件已下载大小
     * @param totalBytes    文件总大小
     * @return  文件
     * @throws IOException
     */
    public static RandomAccessFile getRandomAccessFile(String path, int loadBytes, int totalBytes) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("found invalid internal destination path, empty");
        }

        File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                throw new RuntimeException(
                        formatString("found invalid internal destination path[%s]," +
                                " & path is directory[%B]", path, file.isDirectory()));
        }

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException(formatString("create new file error %s",
                        file.getAbsolutePath()));
            }
        }

        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        if (totalBytes > 0) {
            final long breakpointBytes = accessFile.length();
            final long requiredSpaceBytes = totalBytes - breakpointBytes;

            final long freeSpaceBytes = getFreeSpaceBytes(path);

            if (freeSpaceBytes < requiredSpaceBytes) {
                accessFile.close();
                // throw a out of space exception.
                throw new RuntimeException(
                        formatString("The file is too large to store, breakpoint in bytes: " +
                        " %d, required space in bytes: %d, but free space in bytes: " +
                        "%d", breakpointBytes, requiredSpaceBytes, freeSpaceBytes));
            } else {
                // pre allocate.
                accessFile.setLength(totalBytes);
            }
        }

        if (loadBytes > 0) {
            accessFile.seek(loadBytes);
        }
        return accessFile;
    }

    /**
     * 格式化字符串
     * @param msg   格式数据
     * @param args  参数
     * @return  格式化字符串
     */
    public static String formatString(final String msg, Object... args) {
        return String.format(Locale.ENGLISH, msg, args);
    }

    /**
     * 获取空闲的空间大小
     * @param path  文件路径
     * @return  空间大小
     */
    public static long getFreeSpaceBytes(final String path) {
        long freeSpaceBytes;
        final StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            freeSpaceBytes = statFs.getAvailableBytes();
        } else {
            //noinspection deprecation
            freeSpaceBytes = statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
        }

        return freeSpaceBytes;
    }
}
