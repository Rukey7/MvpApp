package com.dl7.myapp.injector.modules;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.news.INewsListView;
import com.dl7.myapp.module.news.NewsListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/8/23.
 * 新闻列表 Module
 */
@Module
public class NewsListModule {

    private final INewsListView mView;

    public NewsListModule(INewsListView view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new NewsListPresenter(mView);
    }

}
