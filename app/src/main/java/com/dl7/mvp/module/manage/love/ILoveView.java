package com.dl7.mvp.module.manage.love;

import java.util.List;

/**
 * Created by long on 2016/9/28.
 * 收藏界面接口
 */
public interface ILoveView<T> {

    /**
     * 显示数据
     * @param photoList 图片数据
     */
    void loadData(List<T> photoList);

    /**
     * 没有数据
     */
    void noData();
}
