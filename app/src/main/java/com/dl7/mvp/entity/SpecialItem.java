package com.dl7.mvp.entity;

import com.dl7.mvp.api.bean.NewsItemBean;
import com.dl7.recycler.entity.SectionEntity;

/**
 * Created by long on 2016/8/26.
 * 专题列表项
 */
public class SpecialItem extends SectionEntity<NewsItemBean> {

    public SpecialItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SpecialItem(NewsItemBean newsItemBean) {
        super(newsItemBean);
    }
}
