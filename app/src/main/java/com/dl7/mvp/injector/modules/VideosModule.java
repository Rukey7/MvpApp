package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.video.videos.VideosActivity;
import com.dl7.mvp.module.video.videos.VideosPresenter;
import com.dl7.mvp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/10/11.
 */
@Module
public class VideosModule {

    private final VideosActivity mView;

    public VideosModule(VideosActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getSupportFragmentManager());
    }

    @PerActivity
    @Provides
    public IRxBusPresenter provideVideosPresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideosPresenter(mView, daoSession.getVideoInfoDao(), rxBus);
    }
}
