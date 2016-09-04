package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.injector.modules.PhotoSetModule;
import com.dl7.myapp.module.photoset.PhotoSetActivity;

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
