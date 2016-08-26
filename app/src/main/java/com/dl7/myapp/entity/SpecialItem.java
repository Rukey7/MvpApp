package com.dl7.myapp.entity;

import com.dl7.helperlibrary.entity.SectionEntity;
import com.dl7.myapp.api.bean.NewsItemBean;

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
