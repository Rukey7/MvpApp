package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.news.home.MainActivity;
import com.dl7.mvp.module.news.home.MainPresenter;
import com.dl7.mvp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/1.
 * 主页 Module
 */
@Module
public class MainModule {

    private final MainActivity mView;

    public MainModule(MainActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public IRxBusPresenter provideMainPresenter(DaoSession daoSession, RxBus rxBus) {
        return new MainPresenter(mView, daoSession.getNewsTypeBeanDao(), rxBus);
    }

    @PerActivity
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getSupportFragmentManager());
    }
}
