package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.photo.photos.PhotosActivity;
import com.dl7.mvp.module.photo.photos.PhotosPresenter;
import com.dl7.mvp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/5.
 * 图片界面 Module
 */
@Module
public class PhotosModule {

    private final PhotosActivity mView;

    public PhotosModule(PhotosActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getSupportFragmentManager());
    }

    @PerActivity
    @Provides
    public IRxBusPresenter providePhotosPresenter(DaoSession daoSession, RxBus rxBus) {
        return new PhotosPresenter(mView, daoSession.getBeautyPhotoBeanDao(), rxBus);
    }
}
