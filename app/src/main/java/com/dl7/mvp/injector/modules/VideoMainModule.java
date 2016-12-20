package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.video.main.VideoMainFragment;
import com.dl7.mvp.module.video.main.VideoMainPresenter;
import com.dl7.mvp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/12/20.
 * 视频主界面 Module
 */
@Module
public class VideoMainModule {

    private final VideoMainFragment mView;

    public VideoMainModule(VideoMainFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }

    @PerFragment
    @Provides
    public IRxBusPresenter provideVideosPresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideoMainPresenter(mView, daoSession.getVideoInfoDao(), rxBus);
    }
}
