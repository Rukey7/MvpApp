package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.injector.modules.LovePhotoModule;
import com.dl7.mvp.module.manage.love.photo.LovePhotoFragment;

import dagger.Component;

/**
 * Created by long on 2016/12/13.
 * 图片收藏界面 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = LovePhotoModule.class)
public interface LovePhotoComponent {
    void inject(LovePhotoFragment fragment);
}
