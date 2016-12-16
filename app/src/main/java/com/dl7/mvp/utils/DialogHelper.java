package com.dl7.mvp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.dl7.mvp.engine.DownloaderWrapper;
import com.dl7.mvp.local.table.VideoInfo;

/**
 * Created by Rukey7 on 2016/12/12.
 */

public final class DialogHelper {

    private DialogHelper() {
        throw new AssertionError();
    }


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
                        ToastUtils.showToast("确定");
                    }
                });
        builder.create().show();
    }

    public static void deleteDialog(Context context, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否删除?").setPositiveButton("确定", listener).setNegativeButton("取消", null);
        builder.create().show();
    }
}
