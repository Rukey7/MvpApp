package com.dl7.myapp;

import android.app.Application;
import android.content.Context;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.injector.components.ApplicationComponent;
import com.dl7.myapp.injector.components.DaggerApplicationComponent;
import com.dl7.myapp.injector.modules.ApplicationModule;
import com.dl7.myapp.local.dao.NewsTypeDao;
import com.dl7.myapp.local.table.DaoMaster;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.greendao.database.Database;

/**
 * Created by long on 2016/8/19.
 * Application
 */
public class AndroidApplication extends Application {

    private static final String DB_NAME = "news-db";

    private ApplicationComponent mAppComponent;
    private static Context sContext;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        _initConfig();
        _initDatabase();
        _initInjector();
    }


    public ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     * 初始化注射器
     */
    private void _initInjector() {
        mAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this, mDaoSession))
                .build();
    }

    /**
     * 初始化数据库
     */
    private void _initDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database database = helper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
        NewsTypeDao.updateLocalData(this, mDaoSession);
    }

    /**
     * 初始化配置
     */
    private void _initConfig() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            Logger.init("LogTAG");
        }
        RetrofitService.init();
        ToastUtils.init(this);
    }
}
