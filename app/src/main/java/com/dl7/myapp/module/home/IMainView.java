package com.dl7.myapp.module.home;

import com.dl7.myapp.local.table.NewsTypeBean;

import java.util.List;

/**
 * Created by long on 2016/9/1.
 * 主页接口
 */
public interface IMainView {

    /**
     * 显示数据
     * @param checkList     选中栏目
     */
    void loadData(List<NewsTypeBean> checkList);
}
