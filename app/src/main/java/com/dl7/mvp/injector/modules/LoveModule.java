package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.module.manage.love.LoveActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/28.
 * 收藏 Module
 */
@Module
public class LoveModule {

    private final LoveActivity mView;

    public LoveModule(LoveActivity view) {
        this.mView = view;
    }

    @PerActivity
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getSupportFragmentManager());
    }

}
