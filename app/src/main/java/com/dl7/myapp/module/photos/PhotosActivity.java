package com.dl7.myapp.module.photos;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.dl7.myapp.R;
import com.dl7.myapp.adapter.ViewPagerAdapter;
import com.dl7.myapp.injector.components.DaggerPhotosComponent;
import com.dl7.myapp.injector.modules.PhotosModule;
import com.dl7.myapp.module.base.BaseNavActivity;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.module.beautylist.BeautyListFragment;
import com.dl7.myapp.module.photonews.PhotoNewsFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

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
        initToolBar(mToolBar, true, "图片");
        initDrawerLayout(mDrawerLayout, mNavView, mToolBar);
    }

    @Override
    protected void updateViews() {
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("美女");
        titles.add("生活");
        fragments.add(new BeautyListFragment());
        fragments.add(new PhotoNewsFragment());
        mPagerAdapter.setDatas(fragments, titles);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavView.setCheckedItem(R.id.nav_photos);
    }
}
