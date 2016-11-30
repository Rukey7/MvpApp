package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.BeautyPhotosAdapter;
import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.local.table.DaoSession;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.manage.love.LoveActivity;
import com.dl7.mvp.module.manage.love.LovePresenter;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.recycler.adapter.BaseQuickAdapter;

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
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new LovePresenter(mView, daoSession.getBeautyPhotoBeanDao(), rxBus);
    }

    @PerActivity
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new BeautyPhotosAdapter(mView);
    }
}
