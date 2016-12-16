package com.dl7.mvp.module.base;

import java.util.List;

/**
 * Created by long on 2016/9/28.
 * 和本地数据关联的界面接口
 */
public interface ILocalView<T> {

    /**
     * 显示数据
     * @param dataList 数据
     */
    void loadData(List<T> dataList);

    /**
     * 没有数据
     */
    void noData();
}
