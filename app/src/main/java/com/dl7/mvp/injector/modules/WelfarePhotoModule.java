package com.dl7.mvp.injector.modules;

import com.dl7.mvp.adapter.WelfarePhotoAdapter;
import com.dl7.mvp.injector.PerFragment;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.photo.welfarephoto.WelfarePhotoFragment;
import com.dl7.mvp.module.photo.welfarephoto.WelfarePhotoPresenter;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/10/11.
 * 福利图片界面 Module
 */
@Module
public class WelfarePhotoModule {

    private final WelfarePhotoFragment mView;

    public WelfarePhotoModule(WelfarePhotoFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new WelfarePhotoPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new WelfarePhotoAdapter(mView.getContext());
    }
}
