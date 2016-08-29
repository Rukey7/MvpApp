package com.dl7.myapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dl7.helperlibrary.adapter.BaseMultiItemQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.R;
import com.dl7.myapp.api.NewsUtils;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.entity.NewsMultiItem;
import com.dl7.myapp.module.detail.NewsDetailActivity;
import com.dl7.myapp.module.photo.PhotoSetActivity;
import com.dl7.myapp.module.special.SpecialActivity;
import com.dl7.myapp.utils.ImageLoader;
import com.dl7.myapp.utils.StringUtils;
import com.flyco.labelview.LabelView;

import java.util.List;

/**
 * Created by long on 2016/8/24.
 * 新闻列表适配器
 */
public class NewsMultiListAdapter extends BaseMultiItemQuickAdapter<NewsMultiItem> {

    public NewsMultiListAdapter(Context context, List<NewsMultiItem> data) {
        super(context, data);
    }

    public NewsMultiListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void attachItemType() {
        addItemType(NewsMultiItem.ITEM_TYPE_NORMAL, R.layout.adapter_news_list);
        addItemType(NewsMultiItem.ITEM_TYPE_PHOTO_SET, R.layout.adapter_news_photo_set);
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsMultiItem item) {
        switch (item.getItemType()) {
            case NewsMultiItem.ITEM_TYPE_NORMAL:
                _handleNewsNormal(holder, item.getNewsBean());
                break;
            case NewsMultiItem.ITEM_TYPE_PHOTO_SET:
                _handleNewsPhotoSet(holder, item.getNewsBean());
                break;
        }
    }

    /**
     * 处理正常的新闻
     * @param holder
     * @param item
     */
    private void _handleNewsNormal(BaseViewHolder holder, final NewsBean item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadFit(mContext, item.getImgsrc(), newsIcon, R.mipmap.icon_default);
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());

        if (NewsUtils.isNewsSpecial(item.getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setText("专题");
        } else {
            holder.setVisible(R.id.label_view, false);
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NewsUtils.isNewsSpecial(item.getSkipType())) {
                    SpecialActivity.launch(mContext, item.getSpecialID());
                } else {
                    NewsDetailActivity.launch(mContext, item.getPostid());
                }
            }
        });
    }

    /**
     * 处理图片的新闻
     * @param holder
     * @param item
     */
    private void _handleNewsPhotoSet(BaseViewHolder holder, final NewsBean item) {
        ImageView[] newsPhoto = new ImageView[3];
        newsPhoto[0] = holder.getView(R.id.iv_icon_1);
        newsPhoto[1] = holder.getView(R.id.iv_icon_2);
        newsPhoto[2] = holder.getView(R.id.iv_icon_3);
        ImageLoader.loadFit(mContext, item.getImgsrc(), newsPhoto[0], R.mipmap.icon_default);
        for (int i = 0; i < Math.min(2, item.getImgextra().size()); i++) {
            ImageLoader.loadFit(mContext, item.getImgextra().get(i).getImgsrc(),
                    newsPhoto[i+1], R.mipmap.icon_default);
        }
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoSetActivity.launch(mContext, item.getPhotosetID());
            }
        });
    }
}
