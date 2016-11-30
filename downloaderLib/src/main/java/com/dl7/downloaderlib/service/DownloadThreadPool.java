package com.dl7.downloaderlib.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by long on 2016/5/26.
 * 下载线程池
 */
public class DownloadThreadPool {

    private final static int MAX_DOWNLOAD_THREAD = 5;
    private Map<String, DownloadTask> mRunnableHolder = new HashMap<>();
    // 下载线程池
    private final ThreadPoolExecutor mRunnablePool = (ThreadPoolExecutor)
            Executors.newFixedThreadPool(MAX_DOWNLOAD_THREAD);
    // 更新数据库线程池
    private final ThreadPoolExecutor mDbPool = (ThreadPoolExecutor)
            Executors.newFixedThreadPool(MAX_DOWNLOAD_THREAD);


    private DownloadThreadPool() {}

    private static class HolderClass {
        private static final DownloadThreadPool instance = new DownloadThreadPool();
    }

    public static DownloadThreadPool getInstance() {
        return HolderClass.instance;
    }

    /**
     * 执行线程
     * @param task  线程
     */
    public void execute(DownloadTask task) {
        if (task.isRunning()) {
            return;
        }
        cancel(task);
        synchronized (this) {
            mRunnableHolder.put(task.tag(), task);
        }
        mRunnablePool.execute(task);
    }

    /**
     * 取消线程
     * @param url  线程url
     * @param isDel  是否删除任务
     */
    public void cancel(String url, boolean isDel) {
        synchronized (this) {
            if (mRunnableHolder.containsKey(url)) {
                DownloadTask runnable = mRunnableHolder.get(url);
                mRunnablePool.remove(runnable);
                if (runnable != null) {
                    if (isDel) {
                        runnable.cancel();
                    } else {
                        runnable.stop();
                    }
                }
                mRunnableHolder.remove(url);
            }
        }
    }

    public void cancel(DownloadTask task) {
        cancel(task.tag(), false);
    }

    /**
     * 取消所有线程
     */
    public void cancelAll() {
        synchronized (this) {
            for (DownloadTask task : mRunnableHolder.values()) {
                mRunnablePool.remove(task);
                if (task != null) {
                    task.cancel();
                }
            }
            mRunnableHolder.clear();
        }
    }

    /**
     * 获取Runnable
     * @param url   url
     * @return
     */
    public DownloadTask getRunnable(String url) {
        if (mRunnableHolder.containsKey(url)) {
            return mRunnableHolder.get(url);
        }
        return null;
    }

    /**
     * 更新数据库
     * @param runnable
     */
    public void update(Runnable runnable) {
        mDbPool.execute(runnable);
    }
}
