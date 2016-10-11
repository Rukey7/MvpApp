package com.dl7.myapp.injector.modules;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.myapp.adapter.VideoListAdapter;
import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.videolist.VideoListFragment;
import com.dl7.myapp.module.videolist.VideoListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/10/11.
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
