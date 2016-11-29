package com.dl7.myapp.module.video.player;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.VideoBean;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.player.media.IjkPlayerView;

import butterknife.BindView;

public class VideoPlayerActivity extends BaseActivity<IBasePresenter> {

    private static final String VIDEO_DATA_KEY = "VideoPlayerKey";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_player)
    IjkPlayerView mPlayerView;

    private VideoBean mVideoData;

    public static void launch(Context context, VideoBean data) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        context.startActivity(intent);
    }

    @Override
    protected boolean isSystemBarTranslucent() {
        return true;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initViews() {
        mVideoData = getIntent().getParcelableExtra(VIDEO_DATA_KEY);
        initToolBar(mToolbar, true, mVideoData.getTitle());
        mPlayerView.init()
                .setTitle(mVideoData.getTitle())
                .enableDanmaku()
                .setVideoSource(null, mVideoData.getM3u8_url(), mVideoData.getM3u8Hd_url(), null, null);
    }

    @Override
    protected void updateViews() {
        Glide.with(this).load(mVideoData.getCover()).fitCenter().into(mPlayerView.mPlayerThumb);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
