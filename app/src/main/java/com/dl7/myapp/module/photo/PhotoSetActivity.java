package com.dl7.myapp.module.photo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dl7.myapp.R;
import com.dl7.myapp.adapter.PhotoPagerAdapter;
import com.dl7.myapp.api.bean.PhotoSetBean;
import com.dl7.myapp.api.bean.PhotoSetBean.PhotosEntity;
import com.dl7.myapp.injector.components.DaggerPhotoSetComponent;
import com.dl7.myapp.injector.modules.PhotoSetModule;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.views.EmptyLayout;
import com.dl7.myapp.views.ScrollOverLayout;

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
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.scroll_layout)
    ScrollOverLayout mScrollLayout;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;

    @Inject
    IBasePresenter mPresenter;

    private String mPhotoSetId;
    private PhotoPagerAdapter mAdapter;
    private List<PhotosEntity> mPhotosEntities;

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
        mPhotosEntities = photoSetBean.getPhotos();
        for (PhotosEntity entity : mPhotosEntities) {
            imgUrls.add(entity.getImgurl());
        }
        mAdapter = new PhotoPagerAdapter(this, imgUrls);
        mAdapter.setListener(new PhotoPagerAdapter.OnPhotoClickListener() {
            @Override
            public void onPhotoClick() {
                if (mScrollLayout.getScrollStatus() == ScrollOverLayout.STATUS_EXIT) {
                    mScrollLayout.setScrollStatus(ScrollOverLayout.STATUS_COLLAPSED);
                } else {
                    mScrollLayout.setScrollStatus(ScrollOverLayout.STATUS_EXIT);
                }
            }
        });
        mVpPhoto.setAdapter(mAdapter);
        mVpPhoto.setOffscreenPageLimit(imgUrls.size());
        mTvTitle.setText(photoSetBean.getSetname());
        mTvContent.setText(mPhotosEntities.get(0).getNote());
        mTvIndex.setText("1/" + mPhotosEntities.size());
        mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTvContent.setText(mPhotosEntities.get(position).getNote());
                mTvIndex.setText((position + 1) + "/" + mPhotosEntities.size());
            }
        });
    }

}
