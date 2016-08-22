package com.dl7.myapp.adapter;

import android.content.Context;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.R;

import java.util.List;

/**
 * Created by long on 2016/8/22.
 */
public class ListAdapter extends BaseQuickAdapter<String> {

    public ListAdapter(Context context) {
        super(context);
    }

    public ListAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_list;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_title, item);
    }

}
