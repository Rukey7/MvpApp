package com.dl7.myapp.injector.modules;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.myapp.adapter.ManageAdapter;
import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.manage.ManageActivity;
import com.dl7.myapp.module.manage.ManagePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/8/31.
 * 管理
 */
@Module
public class ManageModule {

    private final ManageActivity mView;

    public ManageModule(ManageActivity view) {
        mView = view;
    }

    @Provides
    public BaseQuickAdapter provideManageAdapter() {
        return new ManageAdapter(mView);
    }

    @PerActivity
    @Provides
    public IBasePresenter provideManagePresenter(DaoSession daoSession) {
        return new ManagePresenter(mView, daoSession.getNewsTypeBeanDao());
    }
}
