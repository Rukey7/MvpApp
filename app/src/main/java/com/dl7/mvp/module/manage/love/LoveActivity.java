package com.dl7.mvp.module.manage.love;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.manage.love.photo.LovePhotoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 收藏界面
 */
public class LoveActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    ViewPagerAdapter mPagerAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_love;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "收藏");
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void updateViews() {
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("图片");
        titles.add("视频");
        fragments.add(new LovePhotoFragment());
        fragments.add(new LovePhotoFragment());
        mPagerAdapter.setDatas(fragments, titles);
    }
}
