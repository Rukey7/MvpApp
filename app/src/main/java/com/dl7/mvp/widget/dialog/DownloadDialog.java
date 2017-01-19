package com.dl7.mvp.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rukey7 on 2016/12/12.
 * 下载对话框
 */
public class DownloadDialog extends BottomBaseDialog<DownloadDialog> {

    @BindView(R.id.tv_download)
    TextView mTvDownload;
    @BindView(R.id.tag_layout)
    TagLayout mTagLayout;
    @BindView(R.id.tv_space)
    TextView mTvSpace;

    private List<String> mTags;

    public DownloadDialog(Context context, List<String> tags) {
        super(context);
        mTags = tags;
    }

    @Override
    public View onCreateView() {
//        widthScale(0.85f);
        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.dialog_download, null);
        ButterKnife.bind(this, inflate);
        inflate.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mTagLayout.setTags(mTags);
        if (mTags.size() > 0) {
            mTagLayout.setCheckTag(0);
        }
        mTagLayout.setTagCheckListener(new TagView.OnTagCheckListener() {
            @Override
            public void onTagCheck(int position, String text, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
    }
}
