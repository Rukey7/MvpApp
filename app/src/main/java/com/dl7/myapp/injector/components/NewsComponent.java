package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.injector.modules.NewsModule;
import com.dl7.myapp.module.news.NewsFragment;

import dagger.Component;

/**
 * Created by long on 2016/9/2.
 * 新闻 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsModule.class)
public interface NewsComponent {
    void inject(NewsFragment fragment);
}
