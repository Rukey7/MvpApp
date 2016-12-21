package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.PhotoSetModule;
import com.dl7.mvp.module.news.photoset.PhotoSetActivity;

import dagger.Component;

/**
 * Created by long on 2016/8/29.
 * 图集 Component
 */
@PerActivity
@Component(modules = PhotoSetModule.class)
public interface PhotoSetComponent {
    void inject(PhotoSetActivity activity);
}
