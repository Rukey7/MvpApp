package com.dl7.myapp.injector.modules;

import com.dl7.myapp.adapter.ViewPagerAdapter;
import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.module.photos.PhotosActivity;
import com.dl7.myapp.module.photos.PhotosPresenter;
import com.dl7.myapp.rxbus.RxBus;

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
        return new PhotosPresenter(mView, daoSession.getNewsTypeBeanDao(), rxBus);
    }
}
