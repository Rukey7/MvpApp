package com.dl7.mvp.module.news.channel;

import com.dl7.mvp.local.table.NewsTypeInfo;

import java.util.List;

/**
 * Created by long on 2016/9/1.
 * 栏目管理接口
 */
public interface IChannelView {

    /**
     * 显示数据
     * @param checkList     选中栏目
     * @param uncheckList   未选中栏目
     */
    void loadData(List<NewsTypeInfo> checkList, List<NewsTypeInfo> uncheckList);
}
