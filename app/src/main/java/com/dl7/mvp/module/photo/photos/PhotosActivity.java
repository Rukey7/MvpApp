package com.dl7.mvp.module.photo.photos;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.components.DaggerPhotosComponent;
import com.dl7.mvp.injector.modules.PhotosModule;
import com.dl7.mvp.module.base.BaseNavActivity;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.manage.love.LoveActivity;
import com.dl7.mvp.module.news.photonews.PhotoNewsFragment;
import com.dl7.mvp.module.photo.beautylist.BeautyListFragment;
import com.dl7.mvp.module.photo.welfarephoto.WelfarePhotoFragment;
import com.dl7.mvp.rxbus.event.LoveEvent;
import com.dl7.mvp.utils.AnimateHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * 图片界面，包括美女和生活两块
 */
public class PhotosActivity extends BaseNavActivity<IRxBusPresenter> implements IPhotosView {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    ViewPagerAdapter mPagerAdapter;
    private TextView mTvLovedCount;
    private Animator mLovedAnimator;


    public static void launch(Context context) {
        Intent intent = new Intent(context, PhotosActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_photos;
    }

    @Override
    protected void initInjector() {
        DaggerPhotosComponent.builder()
                .applicationComponent(getAppComponent())
                .photosModule(new PhotosModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "");
        _setCustomToolbar();
        initDrawerLayout(mDrawerLayout, mNavView, mToolBar);
        mPresenter.registerRxBus(LoveEvent.class, new Action1<LoveEvent>() {
            @Override
            public void call(LoveEvent loveEvent) {
                mPresenter.getData();
            }
        });
    }

    @Override
    protected void updateViews() {
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("美女");
        titles.add("福利");
        titles.add("生活");
        fragments.add(new BeautyListFragment());
        fragments.add(new WelfarePhotoFragment());
        fragments.add(new PhotoNewsFragment());
        mPagerAdapter.setItems(fragments, titles);
        mPresenter.getData();
    }

    @Override
    public void updateCount(int lovedCount) {
        mTvLovedCount.setText(lovedCount+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavView.setCheckedItem(R.id.nav_photos);
        if (mLovedAnimator == null) {
            mTvLovedCount.post(new Runnable() {
                @Override
                public void run() {
                    mLovedAnimator = AnimateHelper.doHappyJump(mTvLovedCount, mTvLovedCount.getHeight() * 2/3, 3000);
                }
            });
        } else {
            AnimateHelper.startAnimator(mLovedAnimator);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnimateHelper.stopAnimator(mLovedAnimator);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    private void _setCustomToolbar() {
        View view = getLayoutInflater().inflate(R.layout.layout_custom_toolbar, mToolBar);
        View loveLayout = view.findViewById(R.id.fl_layout);
        mTvLovedCount = (TextView) view.findViewById(R.id.iv_count);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText("图片");
        loveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoveActivity.launch(PhotosActivity.this, 0);
            }
        });
    }
}
