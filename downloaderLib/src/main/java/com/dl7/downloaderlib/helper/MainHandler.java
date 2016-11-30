package com.dl7.downloaderlib.helper;

import android.os.Handler;
import android.os.Looper;

/**
 * 主线程处理工具
 */
public class MainHandler {

    private static final Handler mHandler = new Handler(Looper.getMainLooper());


    private MainHandler() {
        throw new RuntimeException("MainHandler cannot be initialized!");
    }

    /**
     * 在主线程执行
     * @param runnable
     */
    public static void runInMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
