package com.dl7.myapp.module.bigphoto;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.dl7.drag.DragSlopLayout;
import com.dl7.myapp.R;
import com.dl7.myapp.adapter.PhotoPagerAdapter;
import com.dl7.myapp.api.bean.BeautyPhotoBean;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.IBasePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BigPhotoActivity extends BaseActivity<IBasePresenter> {

    private static final String BIG_PHOTO_KEY = "BigPhotoKey";

    @BindView(R.id.vp_photo)
    ViewPager mVpPhoto;
    @BindView(R.id.iv_favorite)
    ImageView mIvFavorite;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.iv_praise)
    ImageView mIvPraise;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;

    private PhotoPagerAdapter mAdapter;
    private List<BeautyPhotoBean> mPhotoList;
    private boolean mIsHideToolbar = false;

    public static void launch(Context context, ArrayList<BeautyPhotoBean> datas) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.putParcelableArrayListExtra(BIG_PHOTO_KEY, datas);
        context.startActivity(intent);
    }

    @Override
    protected boolean isSystemBarTranslucent() {
        return true;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_big_photo;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initViews() {
        mPhotoList = getIntent().getParcelableArrayListExtra(BIG_PHOTO_KEY);
        initToolBar(mToolbar, true, "");
    }

    @Override
    protected void updateViews() {
//        mAdapter = new PhotoPagerAdapter(this, mImgUrls);
        mVpPhoto.setAdapter(mAdapter);
        mAdapter.setListener(new PhotoPagerAdapter.OnPhotoClickListener() {
            @Override
            public void onPhotoClick() {
                mIsHideToolbar = !mIsHideToolbar;
                if (mIsHideToolbar) {
                    mDragLayout.scrollOutScreen(300);
                    mToolbar.animate().translationY(-mToolbar.getBottom()).setDuration(300);
                } else {
                    mDragLayout.scrollInScreen(300);
                    mToolbar.animate().translationY(0).setDuration(300);
                }
            }
        });
    }

    @OnClick({R.id.iv_favorite, R.id.iv_download, R.id.iv_praise, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_favorite:
                break;
            case R.id.iv_download:
                break;
            case R.id.iv_praise:
                break;
            case R.id.iv_share:
                break;
        }
    }
}
