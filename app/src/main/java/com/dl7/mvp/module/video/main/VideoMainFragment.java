package com.dl7.mvp.module.video.main;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.components.DaggerVideoMainComponent;
import com.dl7.mvp.injector.modules.VideoMainModule;
import com.dl7.mvp.module.base.BaseFragment;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.manage.download.DownloadActivity;
import com.dl7.mvp.module.manage.love.LoveActivity;
import com.dl7.mvp.module.video.list.VideoListFragment;
import com.dl7.mvp.rxbus.event.VideoEvent;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by long on 2016/12/20.
 * video主界面
 */
public class VideoMainFragment extends BaseFragment<IRxBusPresenter> implements IVideoMainView {

    private final String[] VIDEO_ID = new String[]{
            "V9LG4B3A0", "V9LG4E6VR", "V9LG4CHOR", "00850FRB"
    };
    private final String[] VIDEO_TITLE = new String[]{
            "热点", "搞笑", "娱乐", "精品"
    };

    @BindView(R.id.iv_love_count)
    TextView mIvLoveCount;
    @BindView(R.id.fl_love_layout)
    FrameLayout mFlLoveLayout;
    @BindView(R.id.tv_download_count)
    TextView mTvDownloadCount;
    @BindView(R.id.fl_download_layout)
    FrameLayout mFlDownloadLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ViewPagerAdapter mPagerAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_video_main;
    }

    @Override
    protected void initInjector() {
        DaggerVideoMainComponent.builder()
                .applicationComponent(getAppComponent())
                .videoMainModule(new VideoMainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "视频");
        mPresenter.registerRxBus(VideoEvent.class, new Action1<VideoEvent>() {
            @Override
            public void call(VideoEvent videoEvent) {
                if (videoEvent.checkStatus == VideoEvent.CHECK_INVALID) {
                    mPresenter.getData(false);
                }
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < VIDEO_ID.length; i++) {
            fragments.add(VideoListFragment.newInstance(VIDEO_ID[i]));
        }
        mPagerAdapter.setItems(fragments, VIDEO_TITLE);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        mPresenter.getData(isRefresh);
    }

    @Override
    public void updateLovedCount(int lovedCount) {
        mIvLoveCount.setText(lovedCount + "");
    }

    @Override
    public void updateDownloadCount(int downloadCount) {
        mTvDownloadCount.setVisibility(downloadCount > 0 ? View.VISIBLE : View.GONE);
        mTvDownloadCount.setText(downloadCount + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    @OnClick({R.id.fl_love_layout, R.id.fl_download_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_love_layout:
                LoveActivity.launch(mContext, 1);
                break;
            case R.id.fl_download_layout:
                DownloadActivity.launch(mContext, 0);
                break;
        }
    }
}
