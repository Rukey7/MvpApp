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
import com.dl7.myapp.injector.components.DaggerMainComponent;
import com.dl7.myapp.injector.modules.MainModule;
import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.channel.ChannelActivity;
import com.dl7.myapp.module.news.NewsListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements IMainView, NavigationView.OnNavigationItemSelectedListener {

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
    @Inject
    IBasePresenter mPresenter;


    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        DaggerMainComponent.builder()
                .applicationComponent(getAppComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
        initToolBar(mToolBar, true, "网易新闻");
        _initDrawerLayout();
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void loadData(List<NewsTypeBean> checkList) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (NewsTypeBean bean : checkList) {
            titles.add(bean.getName());
            fragments.add(NewsListFragment.newInstance(bean.getTypeId()));
        }
        mPagerAdapter.setDatas(fragments, titles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(checkList.size());
        mTabLayout.setupWithViewPager(mViewPager);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_gallery:
                break;
            case R.id.nav_manage:
                ChannelActivity.launch(this);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
