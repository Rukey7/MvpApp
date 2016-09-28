package com.dl7.myapp.module.love;

import com.dl7.myapp.local.table.BeautyPhotoBean;

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
