package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.injector.modules.PhotoNewsModule;
import com.dl7.myapp.module.photonews.PhotoNewsFragment;

import dagger.Component;

/**
 * Created by long on 2016/9/5.
 * 图片新闻列表 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = PhotoNewsModule.class)
public interface PhotoNewsComponent {
    void inject(PhotoNewsFragment fragment);
}
