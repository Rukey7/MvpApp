package com.dl7.myapp.injector.modules;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.myapp.adapter.BeautyPhotosAdapter;
import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.module.base.ILocalPresenter;
import com.dl7.myapp.module.love.LoveActivity;
import com.dl7.myapp.module.love.LovePresenter;

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
    public ILocalPresenter providePresenter(DaoSession daoSession) {
        return new LovePresenter(mView, daoSession.getBeautyPhotoBeanDao());
    }

    @PerActivity
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new BeautyPhotosAdapter(mView);
    }
}
