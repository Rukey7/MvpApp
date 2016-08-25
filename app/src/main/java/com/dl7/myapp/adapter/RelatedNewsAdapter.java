package com.dl7.myapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.NewsDetailBean.RelativeSysEntity;
import com.dl7.myapp.module.detail.NewsDetailActivity;
import com.dl7.myapp.utils.ImageLoader;

import java.util.List;

/**
 * Created by long on 2016/8/25.
 * 相关新闻适配器
 */
public class RelatedNewsAdapter extends BaseQuickAdapter<RelativeSysEntity> {

    public RelatedNewsAdapter(Context context) {
        super(context);
    }

    public RelatedNewsAdapter(Context context, List<RelativeSysEntity> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_news_list;
    }

    @Override
    protected void convert(BaseViewHolder holder, final RelativeSysEntity item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadCenterInside(mContext, item.getImgsrc(), newsIcon, R.mipmap.icon_default);
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, _clipSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailActivity.launch(mContext, item.getId());
            }
        });
    }

    private String _clipSource(String source) {
        int i = source.indexOf("-");
        if (i != -1) {
            return source.substring(0, i);
        }
        return source;
    }
}
