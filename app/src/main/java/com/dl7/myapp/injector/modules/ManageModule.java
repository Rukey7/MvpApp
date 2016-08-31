package com.dl7.myapp.injector.modules;

import android.content.Context;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.myapp.adapter.ManageAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/8/31.
 * 管理
 */
@Module
public class ManageModule {

    private final Context mContext;

    public ManageModule(Context context) {
        mContext = context;
    }

    @Provides
    public BaseQuickAdapter provideManageAdapter() {
        return new ManageAdapter(mContext);
    }
}
