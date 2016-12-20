package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.injector.modules.PhotoMainModule;
import com.dl7.mvp.module.photo.main.PhotoMainFragment;

import dagger.Component;

/**
 * Created by long on 2016/12/20.
 * 图片 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = PhotoMainModule.class)
public interface PhotoMainComponent {
    void inject(PhotoMainFragment fragment);
}
