package com.dl7.mvp.module.video.videos;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.injector.components.DaggerVideosComponent;
import com.dl7.mvp.injector.modules.VideosModule;
import com.dl7.mvp.module.base.BaseNavActivity;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.manage.love.LoveActivity;
import com.dl7.mvp.module.photo.photos.IPhotosView;
import com.dl7.mvp.module.video.videolist.VideoListFragment;
import com.dl7.mvp.rxbus.event.VideoEvent;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

public class VideosActivity extends BaseNavActivity<IRxBusPresenter> implements IPhotosView {

    private final String[] VIDEO_ID = new String[]{
            "V9LG4B3A0", "V9LG4E6VR", "V9LG4CHOR", "00850FRB"
    };
    private final String[] VIDEO_TITLE = new String[]{
            "热点", "搞笑", "娱乐", "精品"
    };

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    ViewPagerAdapter mPagerAdapter;
    private TextView mTvDlCount;
    private Animator mLovedAnimator;


    public static void launch(Context context) {
        Intent intent = new Intent(context, VideosActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_videos;
    }

    @Override
    protected void initInjector() {
        DaggerVideosComponent.builder()
                .applicationComponent(getAppComponent())
                .videosModule(new VideosModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "视频");
        _setCustomToolbar();
        initDrawerLayout(mDrawerLayout, mNavView, mToolBar);
        mPresenter.registerRxBus(VideoEvent.class, new Action1<VideoEvent>() {
            @Override
            public void call(VideoEvent videoEvent) {
                mPresenter.getData();
            }
        });
    }

    @Override
    protected void updateViews() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < VIDEO_ID.length; i++) {
            fragments.add(VideoListFragment.newInstance(VIDEO_ID[i]));
        }
        mTabLayout.setViewPager(mViewPager, VIDEO_TITLE, this, fragments);

//        mViewPager.setAdapter(mPagerAdapter);
//        mTabLayout.setupWithViewPager(mViewPager);
//        List<Fragment> fragments = new ArrayList<>();
//        List<String> titles = new ArrayList<>();
//        for (int i = 0; i < VIDEO_ID.length; i++) {
//            titles.add(VIDEO_TITLE[i]);
//            fragments.add(VideoListFragment.newInstance(VIDEO_ID[i]));
//        }
//        mPagerAdapter.setItems(fragments, titles);
        mPresenter.getData();
    }

    @Override
    public void updateCount(int lovedCount) {
        mTvDlCount.setText(lovedCount + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavView.setCheckedItem(R.id.nav_videos);
//        if (mLovedAnimator == null) {
//            mTvDlCount.post(new Runnable() {
//                @Override
//                public void run() {
//                    mLovedAnimator = AnimateHelper.doHappyJump(mTvDlCount, mTvDlCount.getHeight() * 2/3, 3000);
//                }
//            });
//        } else {
//            AnimateHelper.startAnimator(mLovedAnimator);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AnimateHelper.stopAnimator(mLovedAnimator);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    private void _setCustomToolbar() {
        View view = getLayoutInflater().inflate(R.layout.layout_custom_toolbar, mToolBar);
        View countLayout = view.findViewById(R.id.fl_layout);
        mTvDlCount = (TextView) view.findViewById(R.id.iv_count);
        mTvDlCount.setBackgroundResource(R.mipmap.btn_star);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText("视频");
        countLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoveActivity.launch(VideosActivity.this, 1);
            }
        });
    }
}
