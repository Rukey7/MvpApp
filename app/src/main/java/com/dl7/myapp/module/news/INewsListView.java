package com.dl7.myapp.module.news;

import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.module.base.IBaseView;

import java.util.List;

/**
 * Created by long on 2016/8/23.
 * 新闻列表视图接口
 */
public interface INewsListView extends IBaseView {

    /**
     * 加载数据
     * @param newsList 新闻列表
     */
    void loadData(List<NewsBean> newsList);

    /**
     * 加载更多
     * @param newsList 新闻列表
     */
    void loadMoreData(List<NewsBean> newsList);

    /**
     * 没有数据
     */
    void loadNoData();
}
