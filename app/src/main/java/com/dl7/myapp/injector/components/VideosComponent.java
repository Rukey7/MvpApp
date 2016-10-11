package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.injector.modules.VideosModule;
import com.dl7.myapp.module.videos.VideosActivity;

import dagger.Component;

/**
 * Created by long on 2016/10/11.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = VideosModule.class)
public interface VideosComponent {
    void inject(VideosActivity activity);
}
