package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.BeautyPhotosAdapter;
import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.photo.beauty.BeautyListFragment;
import com.dl7.mvp.module.photo.beauty.BeautyListPresenter;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/9/5.
 * 美图 Module
 */
@Module
public class BeautyListModule {

    private final BeautyListFragment mView;

    public BeautyListModule(BeautyListFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new BeautyListPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new BeautyPhotosAdapter(mView.getContext());
    }
}
