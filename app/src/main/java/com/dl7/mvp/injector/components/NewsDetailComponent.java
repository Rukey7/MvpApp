package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.NewsDetailModule;
import com.dl7.mvp.module.news.detail.NewsDetailActivity;

import dagger.Component;

/**
 * Created by long on 2016/8/25.
 * 新闻详情 Component
 */
@Deprecated
@PerActivity
@Component(modules = NewsDetailModule.class)
public interface NewsDetailComponent {
    void inject(NewsDetailActivity activity);
}
