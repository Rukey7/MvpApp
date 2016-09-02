package com.dl7.myapp.module.news;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.dl7.myapp.R;
import com.dl7.myapp.adapter.ViewPagerAdapter;
import com.dl7.myapp.injector.components.DaggerNewsComponent;
import com.dl7.myapp.injector.modules.NewsModule;
import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.module.base.BaseFragment;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.module.channel.ChannelActivity;
import com.dl7.myapp.module.newslist.NewsListFragment;
import com.dl7.myapp.rxbus.event.DbUpdateEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by long on 2016/9/2.
 * 新闻
 */
public class NewsFragment extends BaseFragment implements INewsView {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ViewPagerAdapter mPagerAdapter;
    @Inject
    IRxBusPresenter mPresenter;


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initViews() {
        DaggerNewsComponent.builder()
                .applicationComponent(getAppComponent())
                .newsModule(new NewsModule(this))
                .build()
                .inject(this);
        initToolBar(mToolBar, true, "网易新闻");
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    private void _setCustomToolbar() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_toolbar, mToolBar);
        ImageView ivChannel = (ImageView) view.findViewById(R.id.iv_channel);
        ivChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelActivity.launch(getContext());
            }
        });
    }

}
