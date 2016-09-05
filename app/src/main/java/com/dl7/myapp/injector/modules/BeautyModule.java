package com.dl7.myapp.injector.modules;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.myapp.adapter.BeautyPhotoAdapter;
import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.beauty.BeautyFragment;
import com.dl7.myapp.module.beauty.BeautyPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/5.
 * 美图 Module
 */
@Module
public class BeautyModule {

    private final BeautyFragment mView;

    public BeautyModule(BeautyFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new BeautyPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new BeautyPhotoAdapter(mView.getContext());
    }
}
