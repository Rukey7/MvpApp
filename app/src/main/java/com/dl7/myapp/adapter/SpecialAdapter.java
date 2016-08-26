package com.dl7.myapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dl7.helperlibrary.adapter.BaseSectionQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.R;
import com.dl7.myapp.api.NewsUtils;
import com.dl7.myapp.entity.SpecialItem;
import com.dl7.myapp.module.detail.NewsDetailActivity;
import com.dl7.myapp.module.special.SpecialActivity;
import com.dl7.myapp.utils.ImageLoader;

import java.util.List;

/**
 * Created by long on 2016/8/26.
 * 专题列表适配器
 */
public class SpecialAdapter extends BaseSectionQuickAdapter<SpecialItem> {


    public SpecialAdapter(Context context, List<SpecialItem> data) {
        super(context, data);
    }

    public SpecialAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_news_list;
    }

    @Override
    protected int attachHeadLayoutRes() {
        return R.layout.adapter_special_head;
    }

    @Override
    protected void convertHead(BaseViewHolder holder, SpecialItem item) {
        holder.setText(R.id.tv_head, item.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, final SpecialItem item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadFit(mContext, item.t.getImgsrc(), newsIcon, R.mipmap.icon_default);
        holder.setText(R.id.tv_title, item.t.getTitle())
                .setText(R.id.tv_source, _clipSource(item.t.getSource()))
                .setText(R.id.tv_time, item.t.getPtime());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NewsUtils.isNewsSpecial(item.t.getSkipType())) {
                    SpecialActivity.launch(mContext, item.t.getSpecialID());
                } else {
                    NewsDetailActivity.launch(mContext, item.t.getPostid());
                }
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
