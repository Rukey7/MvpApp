package com.dl7.myapp.local.dao;

import com.dl7.myapp.local.table.NewsTypeBean;

import java.util.List;

/**
 * Created by long on 2016/8/31.
 * 新闻分类数据访问
 */
public class NewsTypeDao {

    // 当前选中
    private List<NewsTypeBean> mCheckedList;
    // 未选中
    private List<NewsTypeBean> mUncheckedList;


    public NewsTypeDao() {

    }
}
