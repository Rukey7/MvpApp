package com.dl7.mvp.injector.components;

import android.content.Context;

import com.dl7.mvp.injector.modules.ApplicationModule;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.rxbus.RxBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by long on 2016/8/19.
 * Application Component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

//    void inject(BaseActivity baseActivity);

    // provide
    Context getContext();
    RxBus getRxBus();
    DaoSession getDaoSession();
}
