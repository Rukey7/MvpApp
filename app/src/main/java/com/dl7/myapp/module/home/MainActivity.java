package com.dl7.myapp.module.home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.dl7.myapp.R;
import com.dl7.myapp.adapter.ViewPagerAdapter;
import com.dl7.myapp.injector.components.DaggerMainComponent;
import com.dl7.myapp.injector.modules.MainModule;
import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.module.base.BaseNavActivity;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.module.channel.ChannelActivity;
import com.dl7.myapp.module.newslist.NewsListFragment;
import com.dl7.myapp.rxbus.event.DbUpdateEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseNavActivity implements IMainView {

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
    IRxBusPresenter mPresenter;


    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

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
        initToolBar(mToolBar, true, "新闻");
        initDrawerLayout(mDrawerLayout, mNavView, mToolBar);
        _setCustomToolbar();
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mPresenter.registerRxBus(DbUpdateEvent.class);
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavView.setCheckedItem(R.id.nav_news);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    private void _setCustomToolbar() {
        View view = getLayoutInflater().inflate(R.layout.layout_custom_toolbar, mToolBar);
        ImageView ivChannel = (ImageView) view.findViewById(R.id.iv_channel);
        ivChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelActivity.launch(MainActivity.this);
            }
        });
    }
}
