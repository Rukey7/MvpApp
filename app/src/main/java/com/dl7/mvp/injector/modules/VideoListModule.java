package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.VideoListAdapter;
import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.video.list.VideoListFragment;
import com.dl7.mvp.module.video.list.VideoListPresenter;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/10/11.
 * video列表
 */
@Module
public class VideoListModule {

    private final VideoListFragment mView;
    private final String mVideoId;

    public VideoListModule(VideoListFragment view, String videoId) {
        this.mView = view;
        this.mVideoId = videoId;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new VideoListPresenter(mView, mVideoId);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new VideoListAdapter(mView.getContext());
    }
}
