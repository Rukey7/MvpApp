package com.dl7.myapp.injector.modules;

import com.dl7.myapp.adapter.SpecialAdapter;
import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.special.SpecialActivity;
import com.dl7.myapp.module.special.SpecialPresenter;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/8/26.
 * 专题 Module
 */
@Module
public class SpecialModule {

    private final SpecialActivity mView;
    private final String mSpecialId;

    public SpecialModule(SpecialActivity view, String specialId) {
        mView = view;
        mSpecialId = specialId;
    }

    @PerActivity
    @Provides
    public IBasePresenter provideSpecialPresent() {
        return new SpecialPresenter(mView, mSpecialId);
    }

    @PerActivity
    @Provides
    public BaseQuickAdapter provideSpecialAdapter() {
        return new SpecialAdapter(mView);
    }
}
