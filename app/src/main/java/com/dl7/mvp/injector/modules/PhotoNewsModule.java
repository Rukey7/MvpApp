package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.PhotoListAdapter;
import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.photo.news.PhotoNewsFragment;
import com.dl7.mvp.module.photo.news.PhotoNewsPresenter;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/5.
 * 图片新闻列表 Module
 */
@Module
public class PhotoNewsModule {

    private final PhotoNewsFragment mView;

    public PhotoNewsModule(PhotoNewsFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new PhotoNewsPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new PhotoListAdapter(mView.getContext());
    }
}
