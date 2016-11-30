package com.dl7.mvp.module.base;

import java.util.List;

/**
 * Created by long on 2016/9/1.
 * 提供本地数据库操作的 Presenter
 */
public interface ILocalPresenter<T> extends IBasePresenter {

    /**
     * 插入数据
     * @param data  数据
     */
    void insert(T data);

    /**
     * 删除数据
     * @param data  数据
     */
    void delete(T data);

    /**
     * 更新数据
     * @param list   所有数据
     */
    void update(List<T> list);
}
