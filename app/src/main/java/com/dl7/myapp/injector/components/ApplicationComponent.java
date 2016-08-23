package com.dl7.myapp.injector.components;

import android.content.Context;

import com.dl7.myapp.injector.modules.ApplicationModule;
import com.dl7.myapp.rxbus.RxBus;
import com.dl7.myapp.module.base.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by long on 2016/8/19.
 * Application Component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);

    // provide
    Context getContext();
    RxBus getRxBus();
}
