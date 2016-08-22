package com.dl7.myapp.api.bean;

import java.util.List;

/**
 * Created by long on 2016/8/22.
 * 新闻列表
 */
public class NewList {

    private List<NewBean> news;

    public List<NewBean> getNews() {
        return news;
    }

    public void setNews(List<NewBean> news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "NewList{" +
                "news=" + news +
                '}';
    }
}
