package com.dl7.mvp.module.manage.download;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.components.DaggerDownloadComponent;
import com.dl7.mvp.injector.modules.DownloadModule;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.manage.download.cache.VideoCacheFragment;
import com.dl7.mvp.module.manage.download.complete.VideoCompleteFragment;
import com.dl7.mvp.rxbus.event.VideoEvent;
import com.dl7.mvp.views.FlexibleViewPager;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class DownloadActivity extends BaseActivity<IRxBusPresenter> {

    private static final String DL_INDEX_KEY = "DownloadIndexKey";

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    FlexibleViewPager mViewPager;
    @BindView(R.id.btn_select_all)
    TextView mBtnSelectAll;
    @BindView(R.id.btn_select_del)
    TextView mBtnSelectDel;
    @BindView(R.id.fl_del_layout)
    FrameLayout mFlDelLayout;

    @Inject
    ViewPagerAdapter mPagerAdapter;
    private VideoCompleteFragment mCompleteFragment;
    private int mIndex;

    public static void launch(Context context, int index) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.putExtra(DL_INDEX_KEY, index);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_download;
    }

    @Override
    protected void initInjector() {
        DaggerDownloadComponent.builder()
                .applicationComponent(getAppComponent())
                .downloadModule(new DownloadModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        mIndex = getIntent().getIntExtra(DL_INDEX_KEY, 0);
        initToolBar(mToolBar, true, "下载管理");
        mViewPager.setAdapter(mPagerAdapter);
        mPresenter.registerRxBus(VideoEvent.class, new Action1<VideoEvent>() {
            @Override
            public void call(VideoEvent videoEvent) {
                _handleVideoEvent(videoEvent);
            }
        });
    }

    @Override
    protected void updateViews() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        mCompleteFragment = new VideoCompleteFragment();
        fragments.add(mCompleteFragment);
        fragments.add(new VideoCacheFragment());
        titles.add("已缓存");
        titles.add("缓存中");
        mPagerAdapter.setItems(fragments, titles);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(mIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    @Override
    public void onBackPressed() {
        if (mCompleteFragment.onBackPressed()) {
            enableEditMode(false);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 使能编辑状态
     * @param isEnable
     */
    public void enableEditMode(boolean isEnable) {
        mViewPager.setCanScroll(!isEnable);
        if (isEnable) {
            mFlDelLayout.setVisibility(View.VISIBLE);
        } else {
            mFlDelLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 处理 VideoEvent，来改变编辑状态UI
     * @param videoEvent
     */
    private void _handleVideoEvent(VideoEvent videoEvent) {
        switch (videoEvent.checkStatus) {
            case VideoEvent.CHECK_NONE:
                mBtnSelectDel.setEnabled(false);
                mBtnSelectAll.setText("全选");
                mBtnSelectAll.setSelected(false);
                break;
            case VideoEvent.CHECK_SOME:
                mBtnSelectAll.setText("全选");
                mBtnSelectAll.setSelected(false);
                mBtnSelectDel.setEnabled(true);
                break;
            case VideoEvent.CHECK_ALL:
                mBtnSelectAll.setText("取消全选");
                mBtnSelectAll.setSelected(true);
                mBtnSelectDel.setEnabled(true);
                break;
        }
    }

    @OnClick({R.id.btn_select_all, R.id.btn_select_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_all:
                if (mCompleteFragment.isEditMode()) {
                    if (mBtnSelectAll.isSelected()) {
                        mCompleteFragment.checkAllOrNone(false);
                    } else {
                        mCompleteFragment.checkAllOrNone(true);
                    }
                }
                break;
            case R.id.btn_select_del:
                if (mCompleteFragment.isEditMode()) {
                    mCompleteFragment.deleteChecked();
                }
                break;
        }
    }
}
