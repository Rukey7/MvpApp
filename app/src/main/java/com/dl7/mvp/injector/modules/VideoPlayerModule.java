package com.dl7.mvp.injector.modules;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.local.table.VideoBean;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.video.player.VideoPlayerActivity;
import com.dl7.mvp.module.video.player.VideoPlayerPresenter;
import com.dl7.mvp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/11/30.
 * Video Module
 */
@Module
public class VideoPlayerModule {

    private final VideoPlayerActivity mView;
    private final VideoBean mVideoData;

    public VideoPlayerModule(VideoPlayerActivity view, VideoBean videoData) {
        this.mView = view;
        this.mVideoData = videoData;
    }

    @PerActivity
    @Provides
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideoPlayerPresenter(mView, daoSession.getVideoBeanDao(), rxBus, mVideoData);
    }

}
