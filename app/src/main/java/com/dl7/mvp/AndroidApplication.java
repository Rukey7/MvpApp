package com.dl7.mvp;

import android.app.Application;
import android.content.Context;

import com.dl7.downloaderlib.DownloadConfig;
import com.dl7.downloaderlib.FileDownloader;
import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.engine.DownloaderWrapper;
import com.dl7.mvp.injector.components.ApplicationComponent;
import com.dl7.mvp.injector.components.DaggerApplicationComponent;
import com.dl7.mvp.injector.modules.ApplicationModule;
import com.dl7.mvp.local.dao.NewsTypeDao;
import com.dl7.mvp.local.table.DaoMaster;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.mvp.utils.DownloadUtils;
import com.dl7.mvp.utils.PreferencesUtils;
import com.dl7.mvp.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.greendao.database.Database;

import java.io.File;

/**
 * Created by long on 2016/8/19.
 * Application
 */
public class AndroidApplication extends Application {

    private static final String DB_NAME = "news-db";

    private ApplicationComponent mAppComponent;
    private static Context sContext;
    private DaoSession mDaoSession;
    // 因为下载那边需要用，这里在外面实例化在通过 ApplicationModule 设置
    private RxBus mRxBus = new RxBus();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        _initDatabase();
        _initInjector();
        _initConfig();
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
        // 这里不做注入操作，只提供一些全局单例数据
        mAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this, mDaoSession, mRxBus))
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
        DownloadUtils.init(mDaoSession.getBeautyPhotoInfoDao());
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
        DownloaderWrapper.init(mRxBus, mDaoSession.getVideoInfoDao());
        FileDownloader.init(this);
        DownloadConfig config = new DownloadConfig.Builder()
                .setDownloadDir(PreferencesUtils.getSavePath(this) + File.separator + "video/").build();
        FileDownloader.setConfig(config);
    }
}
