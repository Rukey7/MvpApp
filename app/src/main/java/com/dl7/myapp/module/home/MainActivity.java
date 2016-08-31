package com.dl7.myapp.module.home;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.dl7.myapp.R;
import com.dl7.myapp.adapter.ViewPagerAdapter;
import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.manage.ManageActivity;
import com.dl7.myapp.module.news.NewsListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "MVP");
        _initTabLayout();
        _initDrawerLayout();
    }

    @Override
    protected void updateViews() {
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void _initDrawerLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            mDrawerLayout.setClipToPadding(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
    }

    private void _initTabLayout() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("头条");
        titles.add("精选");
        titles.add("娱乐");
        titles.add("体育");
        fragments.add(NewsListFragment.newInstance(RetrofitService.NEWS_HEAD_LINE));
        fragments.add(NewsListFragment.newInstance(RetrofitService.NEWS_BEST));
        fragments.add(NewsListFragment.newInstance(RetrofitService.NEWS_ENTERTAINMENT));
        fragments.add(NewsListFragment.newInstance(RetrofitService.NEWS_SPORT));

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setDatas(fragments, titles);
//        mPagerAdapter.setFragments(fragments);
//        mPagerAdapter.setTitles(titles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_gallery:
                break;
            case R.id.nav_manage:
                ManageActivity.launch(this);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
