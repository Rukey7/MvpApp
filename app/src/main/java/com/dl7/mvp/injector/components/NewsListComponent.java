package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.injector.modules.NewsListModule;
import com.dl7.mvp.module.news.newslist.NewsListFragment;

import dagger.Component;

/**
 * Created by long on 2016/8/23.
 * 新闻列表 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsListModule.class)
public interface NewsListComponent {
    void inject(NewsListFragment fragment);
}
