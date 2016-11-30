package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.BigPhotoModule;
import com.dl7.mvp.module.photo.bigphoto.BigPhotoActivity;

import dagger.Component;

/**
 * Created by long on 2016/9/27.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = BigPhotoModule.class)
public interface BigPhotoComponent {
    void inject(BigPhotoActivity activity);
}
