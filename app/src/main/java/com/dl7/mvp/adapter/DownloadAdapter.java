package com.dl7.mvp.adapter;

import android.content.Context;

import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Created by long on 2016/12/15.
 */

public class DownloadAdapter extends BaseQuickAdapter<VideoInfo> {


    public DownloadAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return 0;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoInfo item) {

    }
}
