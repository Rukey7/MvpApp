package com.dl7.mvp.module.news.photoset;

import com.dl7.mvp.api.bean.PhotoSetInfo;
import com.dl7.mvp.module.base.IBaseView;

/**
 * Created by long on 2016/8/29.
 * 图集界面接口
 */
public interface IPhotoSetView extends IBaseView {

    /**
     * 显示数据
     * @param photoSetBean 图集
     */
    void loadData(PhotoSetInfo photoSetBean);
}
