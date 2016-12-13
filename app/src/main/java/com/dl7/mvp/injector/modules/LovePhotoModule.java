package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.BeautyPhotosAdapter;
import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.manage.love.photo.LovePhotoPresenter;
import com.dl7.mvp.module.manage.love.photo.LovePhotoFragment;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/12/13.
 * 图片收藏界面 Module
 */
@Module
public class LovePhotoModule {

    private final LovePhotoFragment mView;

    public LovePhotoModule(LovePhotoFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new LovePhotoPresenter(mView, daoSession.getBeautyPhotoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new BeautyPhotosAdapter(mView);
    }
}
