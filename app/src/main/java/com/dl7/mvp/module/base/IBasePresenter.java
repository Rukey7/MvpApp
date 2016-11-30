package com.dl7.mvp.module.base;

/**
 * Created by long on 2016/8/23.
 * 基础 Presenter
 */
public interface IBasePresenter {

    /**
     * 获取网络数据，更新界面
     */
    void getData();

    /**
     * 加载更多数据
     */
    void getMoreData();
}
