package com.dl7.mvp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dl7.mvp.R;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.utils.DefIconFactory;
import com.dl7.mvp.utils.ImageLoader;
import com.dl7.mvp.utils.StringUtils;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Created by long on 2016/12/16.
 * video 缓存完成适配器
 */
public class VideoCompleteAdapter extends BaseQuickAdapter<VideoInfo> {



    public VideoCompleteAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_video_complete;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoInfo item) {
        ImageView ivThumb = holder.getView(R.id.iv_thumb);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivThumb, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_size, StringUtils.convertStorageNoB(item.getTotalSize()))
                .setText(R.id.tv_title, item.getTitle());
        holder.getView(R.id.tv_show_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
