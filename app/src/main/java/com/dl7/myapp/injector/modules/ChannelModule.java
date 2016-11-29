package com.dl7.myapp.injector.modules;

import com.dl7.myapp.adapter.ManageAdapter;
import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.module.base.ILocalPresenter;
import com.dl7.myapp.module.channel.ChannelActivity;
import com.dl7.myapp.module.channel.ChannelPresenter;
import com.dl7.myapp.rxbus.RxBus;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/8/31.
 * 管理
 */
@Module
public class ChannelModule {

    private final ChannelActivity mView;

    public ChannelModule(ChannelActivity view) {
        mView = view;
    }

    @Provides
    public BaseQuickAdapter provideManageAdapter() {
        return new ManageAdapter(mView);
    }

    @PerActivity
    @Provides
    public ILocalPresenter provideManagePresenter(DaoSession daoSession, RxBus rxBus) {
        return new ChannelPresenter(mView, daoSession.getNewsTypeBeanDao(), rxBus);
    }
}
