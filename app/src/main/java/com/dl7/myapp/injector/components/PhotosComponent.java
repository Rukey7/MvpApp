package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.injector.modules.PhotosModule;
import com.dl7.myapp.module.photos.PhotosActivity;

import dagger.Component;

/**
 * Created by long on 2016/9/5.
 * 图片 Component
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = PhotosModule.class)
public interface PhotosComponent {
    void inject(PhotosActivity activity);
}
