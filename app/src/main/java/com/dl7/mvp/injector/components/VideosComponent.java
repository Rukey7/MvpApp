package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.VideosModule;
import com.dl7.mvp.module.video.videos.VideosActivity;

import dagger.Component;

/**
 * Created by long on 2016/10/11.
 *
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = VideosModule.class)
public interface VideosComponent {
    void inject(VideosActivity activity);
}
