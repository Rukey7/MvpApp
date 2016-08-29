package com.dl7.myapp.module.photo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.dl7.myapp.R;
import com.dl7.myapp.adapter.PhotoPagerAdapter;
import com.dl7.myapp.api.bean.PhotoSetBean;
import com.dl7.myapp.api.bean.PhotoSetBean.PhotosEntity;
import com.dl7.myapp.injector.components.DaggerPhotoSetComponent;
import com.dl7.myapp.injector.modules.PhotoSetModule;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.views.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PhotoSetActivity extends BaseActivity implements IPhotoSetView {

    private static final String PHOTO_SET_KEY = "PhotoSetKey";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vp_photo)
    ViewPager mVpPhoto;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;

    @Inject
    IBasePresenter mPresenter;

    private String mPhotoSetId;

    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, PhotoSetActivity.class);
        intent.putExtra(PHOTO_SET_KEY, newsId);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_photo_set;
    }

    @Override
    protected void initViews() {
        mPhotoSetId = getIntent().getStringExtra(PHOTO_SET_KEY);
        DaggerPhotoSetComponent.builder()
                .photoSetModule(new PhotoSetModule(this, mPhotoSetId))
                .build()
                .inject(this);
        initToolBar(mToolbar, true, "");
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void showLoading() {
        mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
    }

    @Override
    public void hideLoading() {
        mEmptyLayout.hide();
    }

    @Override
    public void showNetError(final EmptyLayout.OnRetryListener onRetryListener) {
        mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
        mEmptyLayout.setRetryListener(onRetryListener);
    }

    @Override
    public void loadData(PhotoSetBean photoSetBean) {
        List<String> imgUrls = new ArrayList<>();
        List<PhotosEntity> entities = photoSetBean.getPhotos();
        for (PhotosEntity entity : entities) {
            imgUrls.add(entity.getImgurl());
        }
        mVpPhoto.setAdapter(new PhotoPagerAdapter(this, imgUrls));
        mVpPhoto.setOffscreenPageLimit(imgUrls.size());
    }
}
