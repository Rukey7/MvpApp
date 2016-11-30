package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.SpecialModule;
import com.dl7.mvp.module.news.special.SpecialActivity;

import dagger.Component;

/**
 * Created by long on 2016/8/26.
 * 专题 Component
 */
@PerActivity
@Component(modules = SpecialModule.class)
public interface SpecialComponent {
    void inject(SpecialActivity activity);
}
