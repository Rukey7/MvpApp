package com.dl7.mvp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.dl7.mvp.R;
import com.dl7.mvp.api.bean.NewsBean;
import com.dl7.mvp.utils.DefIconFactory;
import com.dl7.mvp.utils.ImageLoader;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by long on 2016/8/23.
 * 新闻列表适配器
 */
@Deprecated
public class NewsListAdapter extends BaseQuickAdapter<NewsBean> {

    public NewsListAdapter(Context context) {
        super(context);
    }

    public NewsListAdapter(Context context, List<NewsBean> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_news_list;
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsBean item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadFit(mContext, item.getImgsrc(), newsIcon, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, _clipSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());
    }

    private String _clipSource(String source) {
        int i = source.indexOf("-");
        if (i != -1) {
            return source.substring(0, i);
        }
        return source;
    }
}
