package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.injector.modules.BeautyModule;
import com.dl7.myapp.module.beauty.BeautyFragment;

import dagger.Component;

/**
 * Created by long on 2016/9/5.
 * 美图 PerFragment
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = BeautyModule.class)
public interface BeautyComponent {
    void inject(BeautyFragment fragment);
}
