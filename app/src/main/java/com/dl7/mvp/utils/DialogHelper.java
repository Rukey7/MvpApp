package com.dl7.mvp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.mvp.engine.DownloaderWrapper;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.module.manage.download.DownloadActivity;
import com.flyco.dialog.widget.popup.BubblePopup;

/**
 * Created by Rukey7 on 2016/12/12.
 */

public final class DialogHelper {

    private DialogHelper() {
        throw new AssertionError();
    }

    /**
     * 下载对话框
     * @param context
     * @param data
     */
    public static void downloadDialog(Context context, final VideoInfo data) {
        String title = "剩余容量(" + StringUtils.convertStorageNoB(SDCardUtils.getFreeSpaceBytes()) + ")";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage("下载需要大量流量，在非wifi下请慎重，是否下载?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DownloaderWrapper.start(data);
                    }
                });
        builder.create().show();
    }

    /**
     * 下载对话框
     * @param context
     * @param listener
     */
    public static void downloadDialog(Context context, DialogInterface.OnClickListener listener) {
        String title = "剩余容量(" + StringUtils.convertStorageNoB(SDCardUtils.getFreeSpaceBytes()) + ")";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage("下载需要大量流量，在非wifi下请慎重，是否下载?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", listener);
        builder.create().show();
    }

    /**
     * 查阅对话框
     * @param context
     * @param data
     */
    public static void checkDialog(final Context context, final VideoInfo data) {
        String msg;
        if (data.getDownloadStatus() == DownloadStatus.COMPLETE) {
            msg = "任务已缓存，点击查看";
        } else {
            msg = "任务正在缓存，点击查看";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage(msg)
                .setNegativeButton("取消", null)
                .setPositiveButton("查看任务", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (data.getDownloadStatus() == DownloadStatus.COMPLETE) {
                            DownloadActivity.launch(context, 0);
                        } else {
                            DownloadActivity.launch(context, 1);
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 删除
     * @param context
     * @param listener
     */
    public static void deleteDialog(Context context, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否删除?").setPositiveButton("确定", listener).setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 生成 Popup
     * @param context
     * @param layoutResId
     * @return
     */
    public static BubblePopup createPopup(Context context, int layoutResId) {
        View inflate = View.inflate(context, layoutResId, null);
        return new BubblePopup(context, inflate);
    }
}
