package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.injector.modules.VideoListModule;
import com.dl7.myapp.module.videolist.VideoListFragment;

import dagger.Component;

/**
 * Created by long on 2016/10/11.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoListModule.class)
public interface VideoListComponent {
    void inject(VideoListFragment fragment);
}
