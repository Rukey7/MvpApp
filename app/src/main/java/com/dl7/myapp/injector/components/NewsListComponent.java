package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.injector.modules.NewsListModule;
import com.dl7.myapp.module.newslist.NewsListFragment;

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
