package com.dl7.myapp.injector.modules;

import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.local.table.VideoBean;
import com.dl7.myapp.module.base.ILocalPresenter;
import com.dl7.myapp.module.video.player.VideoPlayerActivity;
import com.dl7.myapp.module.video.player.VideoPlayerPresenter;
import com.dl7.myapp.rxbus.RxBus;

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
