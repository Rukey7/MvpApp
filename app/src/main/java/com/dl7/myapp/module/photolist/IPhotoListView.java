package com.dl7.myapp.module.photolist;

import com.dl7.myapp.module.base.IBaseView;

import java.util.List;

/**
 * Created by long on 2016/9/5.
 * 图片列表接口
 */
public interface IPhotoListView<T> extends IBaseView {

    /**
     * 加载数据
     * @param photoList 新闻列表
     */
    void loadData(List<T> photoList);

    /**
     * 加载更多
     * @param photoList 新闻列表
     */
    void loadMoreData(List<T> photoList);

    /**
     * 没有数据
     */
    void loadNoData();
}
