package com.dl7.mvp.module.manage.download;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.manage.love.photo.LovePhotoFragment;
import com.dl7.mvp.module.manage.love.video.LoveVideoFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class DownloadActivity extends BaseActivity<ILocalPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    ViewPagerAdapter mPagerAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, DownloadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_download;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "下载管理");
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void updateViews() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LovePhotoFragment());
        fragments.add(new LoveVideoFragment());
        mTabLayout.setViewPager(mViewPager, new String[] {"图片", "视频"}, this, fragments);
    }
}
