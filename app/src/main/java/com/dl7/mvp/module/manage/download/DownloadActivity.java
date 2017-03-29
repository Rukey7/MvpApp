package com.dl7.mvp.module.manage.download;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.components.DaggerDownloadComponent;
import com.dl7.mvp.injector.modules.DownloadModule;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.base.BaseVideoDLFragment;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.manage.download.cache.VideoCacheFragment;
import com.dl7.mvp.module.manage.download.complete.VideoCompleteFragment;
import com.dl7.mvp.rxbus.event.VideoEvent;
import com.dl7.mvp.widget.FlexibleViewPager;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.dl7.mvp.utils.CommonConstant.INDEX_KEY;

/**
 * 下载管理界面，仿Bilibili
 */
public class DownloadActivity extends BaseActivity<IRxBusPresenter> {

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
    @BindView(R.id.tv_close_edit)
    TextView mTvCloseEdit;

    @Inject
    ViewPagerAdapter mPagerAdapter;
    private BaseVideoDLFragment mCompleteFragment;
    private BaseVideoDLFragment mCacheFragment;
    private int mIndex;

    public static void launch(Context context, int index) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.putExtra(INDEX_KEY, index);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.zoom_in_entry, R.anim.hold);
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
        mIndex = getIntent().getIntExtra(INDEX_KEY, 0);
        initToolBar(mToolBar, true, "下载管理");
        mViewPager.setAdapter(mPagerAdapter);
        mPresenter.registerRxBus(VideoEvent.class, new Action1<VideoEvent>() {
            @Override
            public void call(VideoEvent videoEvent) {
                if (videoEvent.checkStatus != VideoEvent.CHECK_INVALID) {
                    _handleVideoEvent(videoEvent);
                }
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        mCompleteFragment = new VideoCompleteFragment();
        mCacheFragment = new VideoCacheFragment();
        fragments.add(mCompleteFragment);
        fragments.add(mCacheFragment);
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
        if (mCompleteFragment.exitEditMode() || mCacheFragment.exitEditMode()) {
            enableEditMode(false);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 使能编辑状态
     *
     * @param isEnable
     */
    public void enableEditMode(boolean isEnable) {
        mViewPager.setCanScroll(!isEnable);
        mFlDelLayout.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        mTvCloseEdit.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        _setTabLayoutCanClick(!isEnable);
    }

    /**
     * 处理 VideoEvent，来改变编辑状态UI
     *
     * @param videoEvent
     */
    private void _handleVideoEvent(VideoEvent videoEvent) {
        mBtnSelectDel.setEnabled(videoEvent.checkStatus != VideoEvent.CHECK_NONE);
        mBtnSelectAll.setText(videoEvent.checkStatus == VideoEvent.CHECK_ALL ? "取消全选" : "全选");
        mBtnSelectAll.setSelected(videoEvent.checkStatus == VideoEvent.CHECK_ALL);
    }

    /**
     * 控制 TabLayout 点击使能
     * @param canClick
     */
    private void _setTabLayoutCanClick(boolean canClick){
        LinearLayout tabStrip= (LinearLayout) mTabLayout.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if(tabView !=null){
                tabView.setClickable(canClick);
            }
        }
    }

    @OnClick({R.id.btn_select_all, R.id.btn_select_del, R.id.tv_close_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_all:
                if (mCompleteFragment.isEditMode()) {
                    mCompleteFragment.checkAllOrNone(!mBtnSelectAll.isSelected());
                }
                if (mCacheFragment.isEditMode()) {
                    mCacheFragment.checkAllOrNone(!mBtnSelectAll.isSelected());
                }
                break;
            case R.id.btn_select_del:
                if (mCompleteFragment.isEditMode()) {
                    mCompleteFragment.deleteChecked();
                }
                if (mCacheFragment.isEditMode()) {
                    mCacheFragment.deleteChecked();
                }
                break;
            case R.id.tv_close_edit:
                if (mCompleteFragment.exitEditMode() || mCacheFragment.exitEditMode()) {
                    enableEditMode(false);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && mCompleteFragment.exitEditMode() || mCacheFragment.exitEditMode()) {
            enableEditMode(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_in_exit);
    }
}
