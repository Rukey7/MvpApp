package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.injector.modules.VideoMainModule;
import com.dl7.mvp.module.video.main.VideoMainFragment;

import dagger.Component;

/**
 * Created by long on 2016/12/20.
 * 视频主界面 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoMainModule.class)
public interface VideoMainComponent {
    void inject(VideoMainFragment fragment);
}
