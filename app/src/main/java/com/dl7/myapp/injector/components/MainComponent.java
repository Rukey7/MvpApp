package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.injector.modules.MainModule;
import com.dl7.myapp.module.home.MainActivity;

import dagger.Component;

/**
 * Created by long on 2016/9/1.
 * 主页 Component
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
