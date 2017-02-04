package com.dl7.mvp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dl7.mvp.R;
import com.dl7.mvp.api.bean.PhotoInfo;
import com.dl7.mvp.module.news.photoset.PhotoSetActivity;
import com.dl7.mvp.utils.DefIconFactory;
import com.dl7.mvp.utils.ImageLoader;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by long on 2016/9/5.
 * 图片列表适配器
 */
public class PhotoListAdapter extends BaseQuickAdapter<PhotoInfo> {

    private final static String PREFIX_PHOTO_ID = "0096";


    public PhotoListAdapter(Context context) {
        super(context);
    }

    public PhotoListAdapter(Context context, List<PhotoInfo> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_photo_list;
    }

    @Override
    protected void convert(BaseViewHolder holder, final PhotoInfo item) {
        ImageView ivPhoto1 = holder.getView(R.id.iv_photo_1);
        ImageView ivPhoto2 = holder.getView(R.id.iv_photo_2);
        ImageView ivPhoto3 = holder.getView(R.id.iv_photo_3);
        ImageLoader.loadCenterCrop(mContext, item.getPics().get(0), ivPhoto1, DefIconFactory.provideIcon());
        ImageLoader.loadCenterCrop(mContext, item.getPics().get(1), ivPhoto2, DefIconFactory.provideIcon());
        ImageLoader.loadCenterCrop(mContext, item.getPics().get(2), ivPhoto3, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getSetname());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoSetActivity.launch(mContext, _mergePhotoId(item.getSetid()));
            }
        });
    }

    private String _mergePhotoId(String setId) {
        return PREFIX_PHOTO_ID + "|" + setId;
    }
}
