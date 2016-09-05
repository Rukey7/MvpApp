package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.injector.modules.PhotoListModule;
import com.dl7.myapp.module.photolist.PhotoListFragment;

import dagger.Component;

/**
 * Created by long on 2016/9/5.
 * 图片列表 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = PhotoListModule.class)
public interface PhotoListComponent {
    void inject(PhotoListFragment fragment);
}
