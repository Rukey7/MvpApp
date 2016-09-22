package com.dl7.myapp.adapter;

import android.content.Context;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.api.bean.BeautyPhotoBean;

import java.util.List;

/**
 * Created by long on 2016/9/22.
 * 美图 Adapter
 */
public class BeautyPhotosAdapter extends BaseQuickAdapter<BeautyPhotoBean> {

    public BeautyPhotosAdapter(Context context) {
        super(context);
    }

    public BeautyPhotosAdapter(Context context, List<BeautyPhotoBean> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return 0;
    }

    @Override
    protected void convert(BaseViewHolder holder, BeautyPhotoBean item) {

    }
}
