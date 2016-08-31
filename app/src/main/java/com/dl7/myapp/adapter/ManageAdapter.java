package com.dl7.myapp.adapter;

import android.content.Context;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.R;
import com.dl7.myapp.local.table.NewsTypeBean;

import java.util.List;

/**
 * Created by long on 2016/8/31.
 * 管理界面适配器
 */
public class ManageAdapter extends BaseQuickAdapter<NewsTypeBean> {

    public ManageAdapter(Context context) {
        super(context);
    }

    public ManageAdapter(Context context, List<NewsTypeBean> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_manage;
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsTypeBean item) {
        holder.setText(R.id.tv_channel_name, item.getName());
    }
}
