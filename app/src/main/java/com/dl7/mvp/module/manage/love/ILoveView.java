package com.dl7.mvp.module.manage.love;

import com.dl7.mvp.local.table.BeautyPhotoBean;

import java.util.List;

/**
 * Created by long on 2016/9/28.
 * 收藏界面接口
 */
public interface ILoveView {

    /**
     * 显示数据
     * @param photoList 图片数据
     */
    void loadData(List<BeautyPhotoBean> photoList);

    /**
     * 没有数据
     */
    void noData();
}
