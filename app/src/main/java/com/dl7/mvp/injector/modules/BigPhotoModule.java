package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.PhotoPagerAdapter;
import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.local.table.BeautyPhotoInfo;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.photo.bigphoto.BigPhotoActivity;
import com.dl7.mvp.module.photo.bigphoto.BigPhotoPresenter;
import com.dl7.mvp.rxbus.RxBus;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/27.
 * 大图 Module
 */
@PerActivity
@Module
public class BigPhotoModule {

    private final BigPhotoActivity mView;
    private List<BeautyPhotoInfo> mPhotoList;

    public BigPhotoModule(BigPhotoActivity view, List<BeautyPhotoInfo> photoList) {
        this.mView = view;
        this.mPhotoList = photoList;
    }

    @PerActivity
    @Provides
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new BigPhotoPresenter(mView, daoSession.getBeautyPhotoInfoDao(), mPhotoList, rxBus);
    }

    @PerActivity
    @Provides
    public PhotoPagerAdapter provideAdapter() {
        return new PhotoPagerAdapter(mView);
    }

}
