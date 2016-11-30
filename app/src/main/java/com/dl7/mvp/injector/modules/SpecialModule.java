package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.SpecialAdapter;
import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.news.special.SpecialActivity;
import com.dl7.mvp.module.news.special.SpecialPresenter;
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
