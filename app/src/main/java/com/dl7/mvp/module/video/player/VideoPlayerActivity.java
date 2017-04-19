package com.dl7.mvp.module.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.mvp.R;
import com.dl7.mvp.engine.DownloaderWrapper;
import com.dl7.mvp.engine.danmaku.DanmakuConverter;
import com.dl7.mvp.engine.danmaku.DanmakuLoader;
import com.dl7.mvp.engine.danmaku.DanmakuParser;
import com.dl7.mvp.injector.components.DaggerVideoPlayerComponent;
import com.dl7.mvp.injector.modules.VideoPlayerModule;
import com.dl7.mvp.local.table.DanmakuInfo;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.utils.CommonConstant;
import com.dl7.mvp.utils.DialogHelper;
import com.dl7.mvp.utils.SnackbarUtils;
import com.dl7.mvp.widget.SimpleButton;
import com.dl7.mvp.widget.dialog.ShareBottomDialog;
import com.dl7.player.danmaku.OnDanmakuListener;
import com.dl7.player.media.IjkPlayerView;
import com.dl7.player.utils.SoftInputUtils;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dl7.mvp.utils.CommonConstant.VIDEO_DATA_KEY;

public class VideoPlayerActivity extends BaseActivity<IVideoPresenter> implements IVideoView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_player)
    IjkPlayerView mPlayerView;
    @BindView(R.id.iv_video_share)
    ImageView mIvVideoShare;
    @BindView(R.id.iv_video_collect)
    ShineButton mIvVideoCollect;
    @BindView(R.id.iv_video_download)
    ImageView mIvVideoDownload;
    @BindView(R.id.ll_operate)
    LinearLayout mLlOperate;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.ll_edit_layout)
    LinearLayout mLlEditLayout;
    @BindView(R.id.sb_send)
    SimpleButton mSbSend;

    private VideoInfo mVideoData;

    public static void launch(Context context, VideoInfo data) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    public static void launchForResult(Fragment fragment, VideoInfo data) {
        Intent intent = new Intent(fragment.getContext(), VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        fragment.startActivityForResult(intent, CommonConstant.VIDEO_REQUEST_CODE);
        fragment.getActivity().overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initInjector() {
        mVideoData = getIntent().getParcelableExtra(VIDEO_DATA_KEY);
        DaggerVideoPlayerComponent.builder()
                .applicationComponent(getAppComponent())
                .videoPlayerModule(new VideoPlayerModule(this, mVideoData))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, true, mVideoData.getTitle());
        mPlayerView.init()
                .setTitle(mVideoData.getTitle())
                .setVideoSource(null, mVideoData.getMp4_url(), mVideoData.getMp4Hd_url(), null, null)
                .enableDanmaku()
                .setDanmakuCustomParser(new DanmakuParser(), DanmakuLoader.instance(), DanmakuConverter.instance())
                .setDanmakuListener(new OnDanmakuListener<DanmakuInfo>() {
                    @Override
                    public boolean isValid() {
                        return true;
                    }

                    @Override
                    public void onDataObtain(DanmakuInfo danmakuInfo) {
                        Logger.w(danmakuInfo.toString());
                        danmakuInfo.setUserName("Long");
                        danmakuInfo.setVid(mVideoData.getVid());
                        mPresenter.addDanmaku(danmakuInfo);
                    }
                });
        mIvVideoCollect.init(this);
        mIvVideoCollect.setShapeResource(R.drawable.ic_video_collect);
        mIvVideoCollect.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                // 这里直接点击就处理，通常是需要和服务器交互返回成功才处理的，但是这个库内部直接受理了点击事件，没法方便地
                // 来控制它，需要改代码
                mVideoData.setCollect(checked);
                if (mVideoData.isCollect()) {
                    mPresenter.insert(mVideoData);
                } else {
                    mPresenter.delete(mVideoData);
                }
            }
        });
        Glide.with(this).load(mVideoData.getCover()).fitCenter().into(mPlayerView.mPlayerThumb);
        mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPlayerView.editVideo();
                }
                mLlOperate.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
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

    @Override
    public void loadData(VideoInfo data) {
        mVideoData = data;
        mIvVideoCollect.setChecked(data.isCollect());
        mIvVideoDownload.setSelected(data.getDownloadStatus() != DownloadStatus.NORMAL);
    }

    @Override
    public void loadDanmakuData(InputStream inputStream) {
        mPlayerView.setDanmakuSource(inputStream);
    }

    @OnClick({R.id.iv_video_share, R.id.iv_video_download, R.id.sb_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_share:
                new ShareBottomDialog(this).show();
                break;
            case R.id.iv_video_download:
                if (view.isSelected()) {
                    DialogHelper.checkDialog(this, mVideoData);
                } else {
                    DialogHelper.downloadDialog(this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DownloaderWrapper.start(mVideoData);
                            mIvVideoDownload.setSelected(true);
                            SnackbarUtils.showDownloadSnackbar(VideoPlayerActivity.this, "任务正在下载", true);
                        }
                    });
                }
                break;
            case R.id.sb_send:
                mPlayerView.sendDanmaku(mEtContent.getText().toString(), false);
                mEtContent.setText("");
                _closeSoftInput();
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(CommonConstant.RESULT_KEY, mVideoData.isCollect());
        setResult(RESULT_OK, intent);
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_bottom_exit);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (_isHideSoftInput(view, (int) ev.getX(), (int) ev.getY())) {
            _closeSoftInput();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void _closeSoftInput() {
        mEtContent.clearFocus();
        SoftInputUtils.closeSoftInput(this);
        mPlayerView.recoverFromEditVideo();
    }

    private boolean _isHideSoftInput(View view, int x, int y) {
        if (view == null || !(view instanceof EditText) || !mEtContent.isFocused()) {
            return false;
        }
        return x < mLlEditLayout.getLeft() ||
                x > mLlEditLayout.getRight() ||
                y > mLlEditLayout.getBottom() ||
                y < mLlEditLayout.getTop();
    }
}
