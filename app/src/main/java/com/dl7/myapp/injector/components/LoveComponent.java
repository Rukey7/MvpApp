package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.injector.modules.LoveModule;
import com.dl7.myapp.module.love.LoveActivity;

import dagger.Component;

/**
 * Created by long on 2016/9/28.
 * 收藏 Component
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = LoveModule.class)
public interface LoveComponent {
    void inject(LoveActivity activity);
}
