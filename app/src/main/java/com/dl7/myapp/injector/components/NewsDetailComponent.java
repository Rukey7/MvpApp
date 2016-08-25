package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.injector.modules.NewsDetailModule;
import com.dl7.myapp.module.detail.NewsDetailActivity;

import dagger.Component;

/**
 * Created by long on 2016/8/25.
 * 新闻详情 Component
 */
@PerActivity
@Component(modules = NewsDetailModule.class)
public interface NewsDetailComponent {
    void inject(NewsDetailActivity activity);
}
