package com.dl7.myapp.injector.modules;

import com.dl7.myapp.adapter.ViewPagerAdapter;
import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.module.news.NewsFragment;
import com.dl7.myapp.module.news.NewsPresenter;
import com.dl7.myapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/2.
 * 新闻 Module
 */
@Module
public class NewsModule {

    private final NewsFragment mView;

    public NewsModule(NewsFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter provideNewsPresenter(DaoSession daoSession, RxBus rxBus) {
        return new NewsPresenter(mView, daoSession.getNewsTypeBeanDao(), rxBus);
    }

    @PerFragment
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }
}
