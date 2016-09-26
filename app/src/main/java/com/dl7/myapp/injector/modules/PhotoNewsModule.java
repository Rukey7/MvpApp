package com.dl7.myapp.injector.modules;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.myapp.adapter.PhotoListAdapter;
import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.photonews.PhotoNewsFragment;
import com.dl7.myapp.module.photonews.PhotoNewsPresenter;

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
