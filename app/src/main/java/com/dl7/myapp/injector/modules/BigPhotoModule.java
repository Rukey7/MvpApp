package com.dl7.myapp.injector.modules;

import com.dl7.myapp.adapter.PhotoPagerAdapter;
import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.local.table.BeautyPhotoBean;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.module.base.ILocalPresenter;
import com.dl7.myapp.module.bigphoto.BigPhotoActivity;
import com.dl7.myapp.module.bigphoto.BigPhotoPresenter;
import com.dl7.myapp.rxbus.RxBus;

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
    private List<BeautyPhotoBean> mPhotoList;

    public BigPhotoModule(BigPhotoActivity view, List<BeautyPhotoBean> photoList) {
        this.mView = view;
        this.mPhotoList = photoList;
    }

    @PerActivity
    @Provides
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new BigPhotoPresenter(mView, daoSession.getBeautyPhotoBeanDao(), mPhotoList, rxBus);
    }

    @PerActivity
    @Provides
    public PhotoPagerAdapter provideAdapter() {
        return new PhotoPagerAdapter(mView);
    }

}
