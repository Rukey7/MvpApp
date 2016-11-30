package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.VideoPlayerModule;
import com.dl7.mvp.module.video.player.VideoPlayerActivity;

import dagger.Component;

/**
 * Created by long on 2016/11/30.
 * Video Component
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = VideoPlayerModule.class)
public interface VideoPlayerComponent {
    void inject(VideoPlayerActivity activity);
}
