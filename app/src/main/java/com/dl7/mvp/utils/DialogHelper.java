package com.dl7.mvp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Rukey7 on 2016/12/12.
 */

public final class DialogHelper {

    private DialogHelper() {
        throw new AssertionError();
    }


    public static void downloadDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("下载")
                .setMessage("这是 android.support.v7.app.AlertDialog 中的样式")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtils.showToast("确定");
                    }
                });
        builder.create().show();
    }
}
