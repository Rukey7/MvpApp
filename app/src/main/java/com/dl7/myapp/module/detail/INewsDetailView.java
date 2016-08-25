package com.dl7.myapp.module.detail;

import com.dl7.myapp.api.bean.NewsDetailBean;
import com.dl7.myapp.module.base.IBaseView;

/**
 * Created by long on 2016/8/25.
 * 新闻详情接口
 */
public interface INewsDetailView extends IBaseView {

    /**
     * 显示数据
     * @param newsDetailBean 新闻详情
     */
    void loadData(NewsDetailBean newsDetailBean);
}
