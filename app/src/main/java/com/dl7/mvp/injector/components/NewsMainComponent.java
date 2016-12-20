package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.injector.modules.NewsMainModule;
import com.dl7.mvp.module.news.main.NewsMainFragment;

import dagger.Component;

/**
 * Created by long on 2016/12/20.
 * 主页 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsMainModule.class)
public interface NewsMainComponent {
    void inject(NewsMainFragment fragment);
}
