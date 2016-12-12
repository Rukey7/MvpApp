package com.dl7.mvp.views.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.dl7.mvp.R;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import butterknife.ButterKnife;

/**
 * Created by Rukey7 on 2016/12/12.
 * 下载对话框
 */
public class DownloadDialog extends BottomBaseDialog<DownloadDialog> {


    public DownloadDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.activity_big_photo, null);
        ButterKnife.bind(this, inflate);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {

    }
}
