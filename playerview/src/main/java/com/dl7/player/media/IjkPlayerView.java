package com.dl7.player.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dl7.player.R;
import com.dl7.player.danmaku.BaseDanmakuConverter;
import com.dl7.player.danmaku.BiliDanmukuParser;
import com.dl7.player.danmaku.OnDanmakuListener;
import com.dl7.player.utils.AnimHelper;
import com.dl7.player.utils.MotionEventUtils;
import com.dl7.player.utils.NavUtils;
import com.dl7.player.utils.SDCardUtils;
import com.dl7.player.utils.SoftInputUtils;
import com.dl7.player.utils.StringUtils;
import com.dl7.player.utils.WindowUtils;
import com.dl7.player.widgets.MarqueeTextView;
import com.dl7.player.widgets.ShareDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.view.GestureDetector.OnGestureListener;
import static android.view.GestureDetector.SimpleOnGestureListener;
import static android.widget.SeekBar.OnSeekBarChangeListener;
import static com.dl7.player.utils.StringUtils.generateTime;
import static tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;

/**
 * Created by long on 2016/10/24.
 */
public class IjkPlayerView extends FrameLayout implements View.OnClickListener {

    // 进度条最大值
    private static final int MAX_VIDEO_SEEK = 1000;
    // 默认隐藏控制栏时间
    private static final int DEFAULT_HIDE_TIMEOUT = 5000;
    // 更新进度消息
    private static final int MSG_UPDATE_SEEK = 10086;
    // 使能翻转消息
    private static final int MSG_ENABLE_ORIENTATION = 10087;
    // 无效变量
    private static final int INVALID_VALUE = -1;

    // 原生的IjkPlayer
    private IjkVideoView mVideoView;
    // 视频开始前的缩略图，根据需要外部进行加载
    public ImageView mPlayerThumb;
    // 加载
    private ProgressBar mLoadingView;
    // 音量
    private TextView mTvVolume;
    // 亮度
    private TextView mTvBrightness;
    // 快进
    private TextView mTvFastForward;
    // 触摸信息布局
    private FrameLayout mFlTouchLayout;
    // 全屏下的后退键
    private ImageView mIvBack;
    // 全屏下的标题
    private MarqueeTextView mTvTitle;
    // 全屏下的TopBar
    private LinearLayout mFullscreenTopBar;
    // 窗口模式的后退键
    private ImageView mIvBackWindow;
    // 窗口模式的TopBar
    private FrameLayout mWindowTopBar;
    // 播放键
    private ImageView mIvPlay;
    private ImageView mIvPlayCircle;
    // 当前时间
    private TextView mTvCurTime;
    // 进度条
    private SeekBar mPlayerSeek;
    // 结束时间
    private TextView mTvEndTime;
    // 全屏切换按钮
    private ImageView mIvFullscreen;
    // BottomBar
    private LinearLayout mLlBottomBar;
    // 整个视频框架布局
    private FrameLayout mFlVideoBox;
    // 锁屏键
    private ImageView mIvPlayerLock;
    // 还原屏幕
    private TextView mTvRecoverScreen;
    // 宽高比选项
    private TextView mTvSettings;
    private RadioGroup mAspectRatioOptions;
    // 关联的Activity
    private AppCompatActivity mAttachActivity;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_SEEK) {
                final int pos = _setProgress();
                if (!mIsSeeking && mIsShowBar && mVideoView.isPlaying()) {
                    // 这里会重复发送MSG，已达到实时更新 Seek 的效果
                    msg = obtainMessage(MSG_UPDATE_SEEK);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
            } else if (msg.what == MSG_ENABLE_ORIENTATION) {
                if (mOrientationListener != null) {
                    mOrientationListener.enable();
                }
            }
        }
    };
    // 音量控制
    private AudioManager mAudioManager;
    // 手势控制
    private GestureDetector mGestureDetector;
    // 最大音量
    private int mMaxVolume;
    // 锁屏
    private boolean mIsForbidTouch = false;
    // 是否显示控制栏
    private boolean mIsShowBar = true;
    // 是否全屏
    private boolean mIsFullscreen;
    // 是否播放结束
    private boolean mIsPlayComplete = false;
    // 是否正在拖拽进度条
    private boolean mIsSeeking;
    // 目标进度
    private long mTargetPosition = INVALID_VALUE;
    // 当前进度
    private int mCurPosition = INVALID_VALUE;
    // 当前音量
    private int mCurVolume = INVALID_VALUE;
    // 当前亮度
    private float mCurBrightness = INVALID_VALUE;
    // 初始高度
    private int mInitHeight;
    // 屏幕宽/高度
    private int mWidthPixels;
    // 屏幕UI可见性
    private int mScreenUiVisibility;
    // 屏幕旋转角度监听
    private OrientationEventListener mOrientationListener;
    // 进来还未播放
    private boolean mIsNeverPlay = true;
    // 外部监听器
    private OnInfoListener mOutsideInfoListener;
    // 禁止翻转，默认为禁止
    private boolean mIsForbidOrientation = true;
    // 是否固定全屏状态
    private boolean mIsAlwaysFullScreen = false;
    // 记录按退出全屏时间
    private long mExitTime = 0;
    // 视频Matrix
    private Matrix mVideoMatrix = new Matrix();
    private Matrix mSaveMatrix = new Matrix();
    // 是否需要显示恢复屏幕按钮
    private boolean mIsNeedRecoverScreen = false;
    // 选项列表高度
    private int mAspectOptionsHeight;


    public IjkPlayerView(Context context) {
        this(context, null);
    }

    public IjkPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _initView(context);
    }

    private void _initView(Context context) {
        if (context instanceof AppCompatActivity) {
            mAttachActivity = (AppCompatActivity) context;
        } else {
            throw new IllegalArgumentException("Context must be AppCompatActivity");
        }
        View.inflate(context, R.layout.layout_player_view, this);
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mPlayerThumb = (ImageView) findViewById(R.id.iv_thumb);
        mLoadingView = (ProgressBar) findViewById(R.id.pb_loading);
        mTvVolume = (TextView) findViewById(R.id.tv_volume);
        mTvBrightness = (TextView) findViewById(R.id.tv_brightness);
        mTvFastForward = (TextView) findViewById(R.id.tv_fast_forward);
        mFlTouchLayout = (FrameLayout) findViewById(R.id.fl_touch_layout);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (MarqueeTextView) findViewById(R.id.tv_title);
        mFullscreenTopBar = (LinearLayout) findViewById(R.id.fullscreen_top_bar);
        mIvBackWindow = (ImageView) findViewById(R.id.iv_back_window);
        mWindowTopBar = (FrameLayout) findViewById(R.id.window_top_bar);
        mIvPlay = (ImageView) findViewById(R.id.iv_play);
        mTvCurTime = (TextView) findViewById(R.id.tv_cur_time);
        mPlayerSeek = (SeekBar) findViewById(R.id.player_seek);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mIvFullscreen = (ImageView) findViewById(R.id.iv_fullscreen);
        mLlBottomBar = (LinearLayout) findViewById(R.id.ll_bottom_bar);
        mFlVideoBox = (FrameLayout) findViewById(R.id.fl_video_box);
        mIvPlayerLock = (ImageView) findViewById(R.id.iv_player_lock);
        mIvPlayCircle = (ImageView) findViewById(R.id.iv_play_circle);
        mTvRecoverScreen = (TextView) findViewById(R.id.tv_recover_screen);
        // 视频宽高比设置
        mTvSettings = (TextView) findViewById(R.id.tv_settings);
        mAspectRatioOptions = (RadioGroup) findViewById(R.id.aspect_ratio_group);
        mAspectOptionsHeight = getResources().getDimensionPixelSize(R.dimen.aspect_btn_size) * 4;
        mAspectRatioOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.aspect_fit_parent) {
                    mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                } else if (checkedId == R.id.aspect_fit_screen) {
                    mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
                } else if (checkedId == R.id.aspect_16_and_9) {
                    mVideoView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
                } else if (checkedId == R.id.aspect_4_and_3) {
                    mVideoView.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
                }
                AnimHelper.doClipViewHeight(mAspectRatioOptions, mAspectOptionsHeight, 0, 150);
            }
        });
        _initMediaQuality();
        _initVideoSkip();
        _initReceiver();

        mIvPlay.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvFullscreen.setOnClickListener(this);
        mIvBackWindow.setOnClickListener(this);
        mIvPlayerLock.setOnClickListener(this);
        mIvPlayCircle.setOnClickListener(this);
        mTvRecoverScreen.setOnClickListener(this);
        mTvSettings.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void _initMediaPlayer() {
        // 加载 IjkMediaPlayer 库
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        // 声音
        mAudioManager = (AudioManager) mAttachActivity.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 亮度
        try {
            int e = Settings.System.getInt(mAttachActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            float progress = 1.0F * (float) e / 255.0F;
            WindowManager.LayoutParams layout = mAttachActivity.getWindow().getAttributes();
            layout.screenBrightness = progress;
            mAttachActivity.getWindow().setAttributes(layout);
        } catch (Settings.SettingNotFoundException var7) {
            var7.printStackTrace();
        }
        // 进度
        mPlayerSeek.setMax(MAX_VIDEO_SEEK);
        mPlayerSeek.setOnSeekBarChangeListener(mSeekListener);
        // 视频监听
        mVideoView.setOnInfoListener(mInfoListener);
        // 触摸控制
        mGestureDetector = new GestureDetector(mAttachActivity, mPlayerGestureListener);
        mFlVideoBox.setClickable(true);
        mFlVideoBox.setOnTouchListener(mPlayerTouchListener);
        // 屏幕翻转控制
        mOrientationListener = new OrientationEventListener(mAttachActivity) {
            @Override
            public void onOrientationChanged(int orientation) {
                _handleOrientation(orientation);
            }
        };
        if (mIsForbidOrientation) {
            // 禁止翻转
            mOrientationListener.disable();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mInitHeight == 0) {
            mInitHeight = getHeight();
            mWidthPixels = getResources().getDisplayMetrics().widthPixels;
        }
    }

    /**============================ 外部调用接口 ============================*/

    /**
     * Activity.onResume() 里调用
     */
    public void onResume() {
        Log.i("TTAG", "onResume");
        if (mIsScreenLocked) {
            // 如果出现锁屏则需要重新渲染器Render，不然会出现只有声音没有动画
            // 目前只在锁屏时会出现图像不动的情况，如果有遇到类似情况可以尝试按这个方法解决
            mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
            mIsScreenLocked = false;
        }
        mVideoView.resume();
        if (!mIsForbidTouch && !mIsForbidOrientation) {
            mOrientationListener.enable();
        }
        if (mCurPosition != INVALID_VALUE) {
            // 重进后 seekTo 到指定位置播放时，通常会回退到前几秒，关键帧??
            seekTo(mCurPosition);
            mCurPosition = INVALID_VALUE;
        }
    }

    /**
     * Activity.onPause() 里调用
     */
    public void onPause() {
        Log.i("TTAG", "onPause");
        mCurPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
        mIvPlay.setSelected(false);
        mOrientationListener.disable();
        _pauseDanmaku();
    }

    /**
     * Activity.onDestroy() 里调用
     *
     * @return 返回播放进度
     */
    public int onDestroy() {
        // 记录播放进度
        int curPosition = mVideoView.getCurrentPosition();
        mVideoView.destroy();
        IjkMediaPlayer.native_profileEnd();
        if (mDanmakuView != null) {
            // don't forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
        if (mShareDialog != null) {
            mShareDialog.dismiss();
            mShareDialog = null;
        }
        // 注销广播
        mAttachActivity.unregisterReceiver(mBatteryReceiver);
        mAttachActivity.unregisterReceiver(mScreenReceiver);
        // 关闭屏幕常亮
        mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return curPosition;
    }

    /**
     * 处理音量键，避免外部按音量键后导航栏和状态栏显示出来退不回去的状态
     *
     * @param keyCode
     * @return
     */
    public boolean handleVolumeKey(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            _setVolume(true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            _setVolume(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 回退，全屏时退回竖屏
     *
     * @return
     */
    public boolean onBackPressed() {
        if (recoverFromEditVideo()) {
            return true;
        }
        if (mIsAlwaysFullScreen) {
            _exit();
            return true;
        } else if (mIsFullscreen) {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mIsForbidTouch) {
                // 锁住状态则解锁
                mIsForbidTouch = false;
                mIvPlayerLock.setSelected(false);
                _setControlBarVisible(mIsShowBar);
            }
            return true;
        }
        return false;
    }

    /**
     * 初始化，必须要先调用
     *
     * @return
     */
    public IjkPlayerView init() {
        _initMediaPlayer();
        return this;
    }

    /**
     * 设置播放资源
     *
     * @param url
     * @return
     */
    public IjkPlayerView setVideoPath(String url) {
        return setVideoPath(Uri.parse(url));
    }

    /**
     * 设置播放资源
     *
     * @param uri
     * @return
     */
    public IjkPlayerView setVideoPath(Uri uri) {
        mVideoView.setVideoURI(uri);
        if (mCurPosition != INVALID_VALUE) {
            seekTo(mCurPosition);
            mCurPosition = INVALID_VALUE;
        } else {
            seekTo(0);
        }
        return this;
    }

    /**
     * 设置标题，全屏的时候可见
     *
     * @param title
     */
    public IjkPlayerView setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    /**
     * 设置只显示全屏状态
     */
    public IjkPlayerView alwaysFullScreen() {
        mIsAlwaysFullScreen = true;
        _setFullScreen(true);
        mIvFullscreen.setVisibility(GONE);
        mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        _setUiLayoutFullscreen();
        return this;
    }

    /**
     * 开始播放
     *
     * @return
     */
    public void start() {
        if (mIsPlayComplete) {
            if (mDanmakuView != null && mDanmakuView.isPrepared()) {
                mDanmakuView.seekTo((long) 0);
                mDanmakuView.pause();
            }
            mIsPlayComplete = false;
        }
        if (!mVideoView.isPlaying()) {
            mIvPlay.setSelected(true);
            mVideoView.start();
            // 更新进度
            mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
        }
        if (mIsNeverPlay) {
            mIsNeverPlay = false;
            mIvPlayCircle.setVisibility(GONE);
            mLoadingView.setVisibility(VISIBLE);
            mIsShowBar = false;
            // 放这边装载弹幕，不然会莫名其妙出现多切几次到首页会弹幕自动播放问题，这里处理下
            _loadDanmaku();
        }
        // 视频播放时开启屏幕常亮
        mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    /**
     * 暂停
     */
    public void pause() {
        mIvPlay.setSelected(false);
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
        _pauseDanmaku();
        // 视频暂停时关闭屏幕常亮
        mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 跳转
     *
     * @param position 位置
     */
    public void seekTo(int position) {
        mVideoView.seekTo(position);
        mDanmakuTargetPosition = position;
    }

    /**
     * 停止
     */
    public void stop() {
        pause();
        mVideoView.stopPlayback();
    }

    public void reset() {

    }

    /**============================ 控制栏处理 ============================*/

    /**
     * SeekBar监听
     */
    private final OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {

        private long curPosition;

        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            mIsSeeking = true;
            _showControlBar(3600000);
            mHandler.removeMessages(MSG_UPDATE_SEEK);
            curPosition = mVideoView.getCurrentPosition();
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            long duration = mVideoView.getDuration();
            // 计算目标位置
            mTargetPosition = (duration * progress) / MAX_VIDEO_SEEK;
            int deltaTime = (int) ((mTargetPosition - curPosition) / 1000);
            String desc;
            // 对比当前位置来显示快进或后退
            if (mTargetPosition > curPosition) {
                desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + "+" + deltaTime + "秒";
            } else {
                desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + deltaTime + "秒";
            }
            _setFastForward(desc);
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            _hideTouchView();
            mIsSeeking = false;
            // 视频跳转
            seekTo((int) mTargetPosition);
            mTargetPosition = INVALID_VALUE;
            _setProgress();
            _showControlBar(DEFAULT_HIDE_TIMEOUT);
        }
    };

    /**
     * 隐藏视图Runnable
     */
    private Runnable mHideBarRunnable = new Runnable() {
        @Override
        public void run() {
            _hideAllView(false);
        }
    };

    /**
     * 隐藏除视频外所有视图
     */
    private void _hideAllView(boolean isTouchLock) {
//        mPlayerThumb.setVisibility(View.GONE);
        mFlTouchLayout.setVisibility(View.GONE);
        mFullscreenTopBar.setVisibility(View.GONE);
        mWindowTopBar.setVisibility(View.GONE);
        mLlBottomBar.setVisibility(View.GONE);
        _showAspectRatioOptions(false);
        if (!isTouchLock) {
            mIvPlayerLock.setVisibility(View.GONE);
            mIsShowBar = false;
        }
        if (mIsEnableDanmaku) {
            mDanmakuPlayerSeek.setVisibility(GONE);
        }
        if (mIsNeedRecoverScreen) {
            mTvRecoverScreen.setVisibility(GONE);
        }
    }

    /**
     * 设置控制栏显示或隐藏
     *
     * @param isShowBar
     */
    private void _setControlBarVisible(boolean isShowBar) {
        if (mIsNeverPlay) {
            mIvPlayCircle.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
        } else if (mIsForbidTouch) {
            mIvPlayerLock.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
        } else {
            mLlBottomBar.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
            if (!isShowBar) {
                _showAspectRatioOptions(false);
            }
            // 全屏切换显示的控制栏不一样
            if (mIsFullscreen) {
                // 只在显示控制栏的时候才设置时间，因为控制栏通常不显示且单位为分钟，所以不做实时更新
                mTvSystemTime.setText(StringUtils.getCurFormatTime());
                mFullscreenTopBar.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                mWindowTopBar.setVisibility(View.GONE);
                mIvPlayerLock.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                if (mIsEnableDanmaku) {
                    mDanmakuPlayerSeek.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                }
                if (mIsNeedRecoverScreen) {
                    mTvRecoverScreen.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                }
            } else {
                mWindowTopBar.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                mFullscreenTopBar.setVisibility(View.GONE);
                mIvPlayerLock.setVisibility(View.GONE);
                if (mIsEnableDanmaku) {
                    mDanmakuPlayerSeek.setVisibility(GONE);
                }
                if (mIsNeedRecoverScreen) {
                    mTvRecoverScreen.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 开关控制栏，单击界面的时候
     */
    private void _toggleControlBar() {
        mIsShowBar = !mIsShowBar;
        _setControlBarVisible(mIsShowBar);
        if (mIsShowBar) {
            // 发送延迟隐藏控制栏的操作
            mHandler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);
            // 发送更新 Seek 消息
            mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
        }
    }

    /**
     * 显示控制栏
     *
     * @param timeout 延迟隐藏时间
     */
    private void _showControlBar(int timeout) {
        if (!mIsShowBar) {
            _setProgress();
            mIsShowBar = true;
        }
        _setControlBarVisible(true);
        mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
        // 先移除隐藏控制栏 Runnable，如果 timeout=0 则不做延迟隐藏操作
        mHandler.removeCallbacks(mHideBarRunnable);
        if (timeout != 0) {
            mHandler.postDelayed(mHideBarRunnable, timeout);
        }
    }

    /**
     * 切换播放状态，点击播放按钮时
     */
    private void _togglePlayStatus() {
        if (mVideoView.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    /**
     * 刷新隐藏控制栏的操作
     */
    private void _refreshHideRunnable() {
        mHandler.removeCallbacks(mHideBarRunnable);
        mHandler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);
    }

    /**
     * 切换控制锁
     */
    private void _togglePlayerLock() {
        mIsForbidTouch = !mIsForbidTouch;
        mIvPlayerLock.setSelected(mIsForbidTouch);
        if (mIsForbidTouch) {
            mOrientationListener.disable();
            _hideAllView(true);
        } else {
            if (!mIsForbidOrientation) {
                mOrientationListener.enable();
            }
            mFullscreenTopBar.setVisibility(View.VISIBLE);
            mLlBottomBar.setVisibility(View.VISIBLE);
            if (mIsEnableDanmaku) {
                mDanmakuPlayerSeek.setVisibility(VISIBLE);
            }
            if (mIsNeedRecoverScreen) {
                mTvRecoverScreen.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 切换视频分辨率控制
     */
    private void _toggleMediaQuality() {
        if (mFlMediaQuality.getVisibility() == GONE) {
            mFlMediaQuality.setVisibility(VISIBLE);
        }
        if (mIsShowQuality) {
            ViewCompat.animate(mFlMediaQuality).translationX(mFlMediaQuality.getWidth()).setDuration(DEFAULT_QUALITY_TIME);
            mIsShowQuality = false;
        } else {
            ViewCompat.animate(mFlMediaQuality).translationX(0).setDuration(DEFAULT_QUALITY_TIME);
            mIsShowQuality = true;
        }
    }

    /**
     * 显示宽高比设置
     *
     * @param isShow
     */
    private void _showAspectRatioOptions(boolean isShow) {
        if (isShow) {
            AnimHelper.doClipViewHeight(mAspectRatioOptions, 0, mAspectOptionsHeight, 150);
        } else {
            ViewGroup.LayoutParams layoutParams = mAspectRatioOptions.getLayoutParams();
            layoutParams.height = 0;
        }
    }

    @Override
    public void onClick(View v) {
        _refreshHideRunnable();
        int id = v.getId();
        if (id == R.id.iv_back) {
            if (mIsAlwaysFullScreen) {
                _exit();
                return;
            }
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (id == R.id.iv_back_window) {
            mAttachActivity.finish();
        } else if (id == R.id.iv_play || id == R.id.iv_play_circle) {
            _togglePlayStatus();
        } else if (id == R.id.iv_fullscreen) {
            _toggleFullScreen();
        } else if (id == R.id.iv_player_lock) {
            _togglePlayerLock();
        } else if (id == R.id.iv_media_quality) {
            if (!mIsShowQuality) {
                _toggleMediaQuality();
            }
        } else if (id == R.id.iv_cancel_skip) {
            mHandler.removeCallbacks(mHideSkipTipRunnable);
            _hideSkipTip();
        } else if (id == R.id.tv_do_skip) {
            mLoadingView.setVisibility(VISIBLE);
            // 视频跳转
            seekTo(mSkipPosition);
            mHandler.removeCallbacks(mHideSkipTipRunnable);
            _hideSkipTip();
            _setProgress();
        } else if (id == R.id.iv_danmaku_control) {
            _toggleDanmakuShow();
        } else if (id == R.id.tv_open_edit_danmaku) {
            if (mDanmakuListener == null || mDanmakuListener.isValid()) {
                editVideo();
                mEditDanmakuLayout.setVisibility(VISIBLE);
                SoftInputUtils.setEditFocusable(mAttachActivity, mEtDanmakuContent);
            }
        } else if (id == R.id.iv_cancel_send) {
            recoverFromEditVideo();
        } else if (id == R.id.iv_do_send) {
            recoverFromEditVideo();
            sendDanmaku(mEtDanmakuContent.getText().toString(), false);
            mEtDanmakuContent.setText("");
        } else if (id == R.id.input_options_more) {
            _toggleMoreColorOptions();
        } else if (id == R.id.iv_screenshot) {
            _doScreenshot();
        } else if (id == R.id.tv_recover_screen) {
            mVideoView.resetVideoView(true);
            mIsNeedRecoverScreen = false;
            mTvRecoverScreen.setVisibility(GONE);
        } else if (id == R.id.tv_settings) {
            _showAspectRatioOptions(true);
        }
    }

    /**==================== 屏幕翻转/切换处理 ====================*/

    /**
     * 使能视频翻转
     */
    public IjkPlayerView enableOrientation() {
        mIsForbidOrientation = false;
        mOrientationListener.enable();
        return this;
    }

    /**
     * 全屏切换，点击全屏按钮
     */
    private void _toggleFullScreen() {
        if (WindowUtils.getScreenOrientation(mAttachActivity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 设置全屏或窗口模式
     *
     * @param isFullscreen
     */
    private void _setFullScreen(boolean isFullscreen) {
        mIsFullscreen = isFullscreen;
        // 处理弹幕相关视图
        _toggleDanmakuView(isFullscreen);
        _handleActionBar(isFullscreen);
        _changeHeight(isFullscreen);
        mIvFullscreen.setSelected(isFullscreen);
        mHandler.post(mHideBarRunnable);
        mIvMediaQuality.setVisibility(isFullscreen ? VISIBLE : GONE);
        mLlBottomBar.setBackgroundResource(isFullscreen ? R.color.bg_video_view : android.R.color.transparent);
        if (mIsShowQuality && !isFullscreen) {
            _toggleMediaQuality();
        }
        // 处理三指旋转缩放，如果之前进行了相关操作则全屏时还原之前旋转缩放的状态，窗口模式则将整个屏幕还原为未操作状态
        if (mIsNeedRecoverScreen) {
            if (isFullscreen) {
                mVideoView.adjustVideoView(1.0f);
                mTvRecoverScreen.setVisibility(mIsShowBar ? View.VISIBLE : View.GONE);
            } else {
                mVideoView.resetVideoView(false);
                mTvRecoverScreen.setVisibility(GONE);
            }
        }
        // 非全屏隐藏宽高比设置
        if (!isFullscreen) {
            _showAspectRatioOptions(false);
        }
    }

    /**
     * 处理屏幕翻转
     *
     * @param orientation
     */
    private void _handleOrientation(int orientation) {
        if (mIsNeverPlay) {
            return;
        }
        if (mIsFullscreen && !mIsAlwaysFullScreen) {
            // 根据角度进行竖屏切换，如果为固定全屏则只能横屏切换
            if (orientation >= 0 && orientation <= 30 || orientation >= 330) {
                // 请求屏幕翻转
                mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            // 根据角度进行横屏切换
            if (orientation >= 60 && orientation <= 120) {
                mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else if (orientation >= 240 && orientation <= 300) {
                mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    /**
     * 当屏幕执行翻转操作后调用禁止翻转功能，延迟3000ms再使能翻转，避免不必要的翻转
     */
    private void _refreshOrientationEnable() {
        if (!mIsForbidOrientation) {
            mOrientationListener.disable();
            mHandler.removeMessages(MSG_ENABLE_ORIENTATION);
            mHandler.sendEmptyMessageDelayed(MSG_ENABLE_ORIENTATION, 3000);
        }
    }

    /**
     * 隐藏/显示 ActionBar
     *
     * @param isFullscreen
     */
    private void _handleActionBar(boolean isFullscreen) {
        ActionBar supportActionBar = mAttachActivity.getSupportActionBar();
        if (supportActionBar != null) {
            if (isFullscreen) {
                supportActionBar.hide();
            } else {
                supportActionBar.show();
            }
        }
    }

    /**
     * 改变视频布局高度
     *
     * @param isFullscreen
     */
    private void _changeHeight(boolean isFullscreen) {
        if (mIsAlwaysFullScreen) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (isFullscreen) {
            // 高度扩展为横向全屏
            layoutParams.height = mWidthPixels;
        } else {
            // 还原高度
            layoutParams.height = mInitHeight;
        }
        setLayoutParams(layoutParams);
    }

    /**
     * 设置UI沉浸式显示
     */
    private void _setUiLayoutFullscreen() {
        if (Build.VERSION.SDK_INT >= 14) {
            // 获取关联 Activity 的 DecorView
            View decorView = mAttachActivity.getWindow().getDecorView();
            // 沉浸式使用这些Flag
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 屏幕翻转后的处理，在 Activity.configurationChanged() 调用
     * SYSTEM_UI_FLAG_LAYOUT_STABLE：维持一个稳定的布局
     * SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉
     * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
     * SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)
     * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * SYSTEM_UI_FLAG_IMMERSIVE：沉浸式，从顶部下滑出现状态栏和导航栏会固定住
     * SYSTEM_UI_FLAG_IMMERSIVE_STICKY：黏性沉浸式，从顶部下滑出现状态栏和导航栏过几秒后会缩回去
     *
     * @param newConfig
     */
    public void configurationChanged(Configuration newConfig) {
        _refreshOrientationEnable();
        // 沉浸式只能在SDK19以上实现
        if (Build.VERSION.SDK_INT >= 14) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // 获取关联 Activity 的 DecorView
                View decorView = mAttachActivity.getWindow().getDecorView();
                // 保存旧的配置
                mScreenUiVisibility = decorView.getSystemUiVisibility();
                // 沉浸式使用这些Flag
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
                _setFullScreen(true);
                mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                View decorView = mAttachActivity.getWindow().getDecorView();
                // 还原
                decorView.setSystemUiVisibility(mScreenUiVisibility);
                _setFullScreen(false);
                mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }

    /**
     * 从总显示全屏状态退出处理{@link #alwaysFullScreen()}
     */
    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(mAttachActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            mAttachActivity.finish();
        }
    }

    /**============================ 触屏操作处理 ============================*/

    /**
     * 手势监听
     */
    private OnGestureListener mPlayerGestureListener = new SimpleOnGestureListener() {
        // 是否是按下的标识，默认为其他动作，true为按下标识，false为其他动作
        private boolean isDownTouch;
        // 是否声音控制,默认为亮度控制，true为声音控制，false为亮度控制
        private boolean isVolume;
        // 是否横向滑动，默认为纵向滑动，true为横向滑动，false为纵向滑动
        private boolean isLandscape;
        // 是否从弹幕编辑状态返回
        private boolean isRecoverFromDanmaku;

        @Override
        public boolean onDown(MotionEvent e) {
            isDownTouch = true;
            isRecoverFromDanmaku = recoverFromEditVideo();
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!mIsForbidTouch && !mIsNeverPlay) {
                float mOldX = e1.getX(), mOldY = e1.getY();
                float deltaY = mOldY - e2.getY();
                float deltaX = mOldX - e2.getX();
                if (isDownTouch) {
                    // 判断左右或上下滑动
                    isLandscape = Math.abs(distanceX) >= Math.abs(distanceY);
                    // 判断是声音或亮度控制
                    isVolume = mOldX > getResources().getDisplayMetrics().widthPixels * 0.5f;
                    isDownTouch = false;
                }

                if (isLandscape) {
                    _onProgressSlide(-deltaX / mVideoView.getWidth());
                } else {
                    float percent = deltaY / mVideoView.getHeight();
                    if (isVolume) {
                        _onVolumeSlide(percent);
                    } else {
                        _onBrightnessSlide(percent);
                    }
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // 弹幕编辑状态返回则不执行单击操作
            if (isRecoverFromDanmaku) {
                return true;
            }
            if (mIsShowQuality) {
                _toggleMediaQuality();
            } else {
                _toggleControlBar();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // 如果未进行播放或从弹幕编辑状态返回则不执行双击操作
            if (mIsNeverPlay || isRecoverFromDanmaku) {
                return true;
            }
            if (!mIsForbidTouch) {
                _refreshHideRunnable();
                _togglePlayStatus();
            }
            return true;
        }
    };

    /**
     * 隐藏视图Runnable
     */
    private Runnable mHideTouchViewRunnable = new Runnable() {
        @Override
        public void run() {
            _hideTouchView();
        }
    };

    /**
     * 触摸监听
     */
    private OnTouchListener mPlayerTouchListener = new OnTouchListener() {
        // 触摸模式：正常、无效、缩放旋转
        private static final int NORMAL = 1;
        private static final int INVALID_POINTER = 2;
        private static final int ZOOM_AND_ROTATE = 3;
        // 触摸模式
        private int mode = NORMAL;
        // 缩放的中点
        private PointF midPoint = new PointF(0, 0);
        // 旋转角度
        private float degree = 0;
        // 用来标识哪两个手指靠得最近，我的做法是取最近的两指中点和余下一指来控制旋转缩放
        private int fingerFlag = INVALID_VALUE;
        // 初始间距
        private float oldDist;
        // 缩放比例
        private float scale;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (MotionEventCompat.getActionMasked(event)) {
                case MotionEvent.ACTION_DOWN:
                    mode = NORMAL;
                    mHandler.removeCallbacks(mHideBarRunnable);
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    if (event.getPointerCount() == 3 && mIsFullscreen) {
                        _hideTouchView();
                        // 进入三指旋转缩放模式，进行相关初始化
                        mode = ZOOM_AND_ROTATE;
                        MotionEventUtils.midPoint(midPoint, event);
                        fingerFlag = MotionEventUtils.calcFingerFlag(event);
                        degree = MotionEventUtils.rotation(event, fingerFlag);
                        oldDist = MotionEventUtils.calcSpacing(event, fingerFlag);
                        // 获取视频的 Matrix
                        mSaveMatrix = mVideoView.getVideoTransform();
                    } else {
                        mode = INVALID_POINTER;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mode == ZOOM_AND_ROTATE) {
                        // 处理旋转
                        float newRotate = MotionEventUtils.rotation(event, fingerFlag);
                        mVideoView.setVideoRotation((int) (newRotate - degree));
                        // 处理缩放
                        mVideoMatrix.set(mSaveMatrix);
                        float newDist = MotionEventUtils.calcSpacing(event, fingerFlag);
                        scale = newDist / oldDist;
                        mVideoMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        mVideoView.setVideoTransform(mVideoMatrix);
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    if (mode == ZOOM_AND_ROTATE) {
                        // 调整视频界面，让界面居中显示在屏幕
                        mIsNeedRecoverScreen = mVideoView.adjustVideoView(scale);
                        if (mIsNeedRecoverScreen && mIsShowBar) {
                            mTvRecoverScreen.setVisibility(VISIBLE);
                        }
                    }
                    mode = INVALID_POINTER;
                    break;
            }
            // 触屏手势处理
            if (mode == NORMAL) {
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
                    _endGesture();
                }
            }
            return false;
        }
    };

    /**
     * 更新进度条
     *
     * @return
     */
    private int _setProgress() {
        if (mVideoView == null || mIsSeeking) {
            return 0;
        }
        // 视频播放的当前进度
        int position = mVideoView.getCurrentPosition();
        // 视频总的时长
        int duration = mVideoView.getDuration();
        if (duration > 0) {
            // 转换为 Seek 显示的进度值
            long pos = (long) MAX_VIDEO_SEEK * position / duration;
            mPlayerSeek.setProgress((int) pos);
            if (mIsEnableDanmaku) {
                mDanmakuPlayerSeek.setProgress((int) pos);
            }
        }
        // 获取缓冲的进度百分比，并显示在 Seek 的次进度
        int percent = mVideoView.getBufferPercentage();
        mPlayerSeek.setSecondaryProgress(percent * 10);
        if (mIsEnableDanmaku) {
            mDanmakuPlayerSeek.setSecondaryProgress(percent * 10);
        }
        // 更新播放时间
        mTvEndTime.setText(generateTime(duration));
        mTvCurTime.setText(generateTime(position));
        // 返回当前播放进度
        return position;
    }

    /**
     * 设置快进
     *
     * @param time
     */
    private void _setFastForward(String time) {
        if (mFlTouchLayout.getVisibility() == View.GONE) {
            mFlTouchLayout.setVisibility(View.VISIBLE);
        }
        if (mTvFastForward.getVisibility() == View.GONE) {
            mTvFastForward.setVisibility(View.VISIBLE);
        }
        mTvFastForward.setText(time);
    }

    /**
     * 隐藏触摸视图
     */
    private void _hideTouchView() {
        if (mFlTouchLayout.getVisibility() == View.VISIBLE) {
            mTvFastForward.setVisibility(View.GONE);
            mTvVolume.setVisibility(View.GONE);
            mTvBrightness.setVisibility(View.GONE);
            mFlTouchLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 快进或者快退滑动改变进度，这里处理触摸滑动不是拉动 SeekBar
     *
     * @param percent 拖拽百分比
     */
    private void _onProgressSlide(float percent) {
        int position = mVideoView.getCurrentPosition();
        long duration = mVideoView.getDuration();
        // 单次拖拽最大时间差为100秒或播放时长的1/2
        long deltaMax = Math.min(100 * 1000, duration / 2);
        // 计算滑动时间
        long delta = (long) (deltaMax * percent);
        // 目标位置
        mTargetPosition = delta + position;
        if (mTargetPosition > duration) {
            mTargetPosition = duration;
        } else if (mTargetPosition <= 0) {
            mTargetPosition = 0;
        }
        int deltaTime = (int) ((mTargetPosition - position) / 1000);
        String desc;
        // 对比当前位置来显示快进或后退
        if (mTargetPosition > position) {
            desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + "+" + deltaTime + "秒";
        } else {
            desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + deltaTime + "秒";
        }
        _setFastForward(desc);
    }

    /**
     * 设置声音控制显示
     *
     * @param volume
     */
    private void _setVolumeInfo(int volume) {
        if (mFlTouchLayout.getVisibility() == View.GONE) {
            mFlTouchLayout.setVisibility(View.VISIBLE);
        }
        if (mTvVolume.getVisibility() == View.GONE) {
            mTvVolume.setVisibility(View.VISIBLE);
        }
        mTvVolume.setText((volume * 100 / mMaxVolume) + "%");
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void _onVolumeSlide(float percent) {
        if (mCurVolume == INVALID_VALUE) {
            mCurVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mCurVolume < 0) {
                mCurVolume = 0;
            }
        }
        int index = (int) (percent * mMaxVolume) + mCurVolume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // 变更进度条
        _setVolumeInfo(index);
    }


    /**
     * 递增或递减音量，量度按最大音量的 1/15
     *
     * @param isIncrease 递增或递减
     */
    private void _setVolume(boolean isIncrease) {
        int curVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (isIncrease) {
            curVolume += mMaxVolume / 15;
        } else {
            curVolume -= mMaxVolume / 15;
        }
        if (curVolume > mMaxVolume) {
            curVolume = mMaxVolume;
        } else if (curVolume < 0) {
            curVolume = 0;
        }
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
        // 变更进度条
        _setVolumeInfo(curVolume);
        mHandler.removeCallbacks(mHideTouchViewRunnable);
        mHandler.postDelayed(mHideTouchViewRunnable, 1000);
    }

    /**
     * 设置亮度控制显示
     *
     * @param brightness
     */
    private void _setBrightnessInfo(float brightness) {
        if (mFlTouchLayout.getVisibility() == View.GONE) {
            mFlTouchLayout.setVisibility(View.VISIBLE);
        }
        if (mTvBrightness.getVisibility() == View.GONE) {
            mTvBrightness.setVisibility(View.VISIBLE);
        }
        mTvBrightness.setText(Math.ceil(brightness * 100) + "%");
    }

    /**
     * 滑动改变亮度大小
     *
     * @param percent
     */
    private void _onBrightnessSlide(float percent) {
        if (mCurBrightness < 0) {
            mCurBrightness = mAttachActivity.getWindow().getAttributes().screenBrightness;
            if (mCurBrightness < 0.0f) {
                mCurBrightness = 0.5f;
            } else if (mCurBrightness < 0.01f) {
                mCurBrightness = 0.01f;
            }
        }
        WindowManager.LayoutParams attributes = mAttachActivity.getWindow().getAttributes();
        attributes.screenBrightness = mCurBrightness + percent;
        if (attributes.screenBrightness > 1.0f) {
            attributes.screenBrightness = 1.0f;
        } else if (attributes.screenBrightness < 0.01f) {
            attributes.screenBrightness = 0.01f;
        }
        _setBrightnessInfo(attributes.screenBrightness);
        mAttachActivity.getWindow().setAttributes(attributes);
    }

    /**
     * 手势结束调用
     */
    private void _endGesture() {
        if (mTargetPosition >= 0 && mTargetPosition != mVideoView.getCurrentPosition()) {
            // 更新视频播放进度
            seekTo((int) mTargetPosition);
            mPlayerSeek.setProgress((int) (mTargetPosition * MAX_VIDEO_SEEK / mVideoView.getDuration()));
            if (mIsEnableDanmaku) {
                mDanmakuPlayerSeek.setProgress((int) (mTargetPosition * MAX_VIDEO_SEEK / mVideoView.getDuration()));
            }
            mTargetPosition = INVALID_VALUE;
        }
        // 隐藏触摸操作显示图像
        _hideTouchView();
        _refreshHideRunnable();
        mCurVolume = INVALID_VALUE;
        mCurBrightness = INVALID_VALUE;
    }

    /**
     * ============================ 播放状态控制 ============================
     */

    // 这个用来控制弹幕启动和视频同步
    private boolean mIsRenderingStart = false;
    // 缓冲开始，这个用来控制弹幕启动和视频同步
    private boolean mIsBufferingStart = false;

    // 视频播放状态监听
    private OnInfoListener mInfoListener = new OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int status, int extra) {
            _switchStatus(status);
            if (mOutsideInfoListener != null) {
                mOutsideInfoListener.onInfo(iMediaPlayer, status, extra);
            }
            return true;
        }
    };

    /**
     * 视频播放状态处理
     *
     * @param status
     */
    private void _switchStatus(int status) {
        Log.d("TTAG", "status " + status);
        switch (status) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                mIsBufferingStart = true;
                _pauseDanmaku();
                if (!mIsNeverPlay) {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
            case MediaPlayerParams.STATE_PREPARING:
                break;

            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                mIsRenderingStart = true;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                mIsBufferingStart = false;
                mLoadingView.setVisibility(View.GONE);
                mPlayerThumb.setVisibility(View.GONE);
                // 更新进度
                mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
                if (mSkipPosition != INVALID_VALUE) {
                    _showSkipTip(); // 显示跳转提示
                }
                if (mVideoView.isPlaying()) {
                    _resumeDanmaku();   // 开启弹幕
                }
                break;

            case MediaPlayerParams.STATE_PLAYING:
                if (mIsRenderingStart && !mIsBufferingStart) {
                    _resumeDanmaku();   // 开启弹幕
                }
                break;
            case MediaPlayerParams.STATE_ERROR:
                _pauseDanmaku();
                break;

            case MediaPlayerParams.STATE_COMPLETED:
                pause();
                mIsPlayComplete = true;
                break;
        }
    }

    /**============================ Listener ============================*/

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mVideoView.setOnPreparedListener(l);
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mVideoView.setOnCompletionListener(l);
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mVideoView.setOnErrorListener(l);
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    public void setOnInfoListener(OnInfoListener l) {
        mOutsideInfoListener = l;
    }

    /**
     * 设置弹幕监听器
     * @param danmakuListener
     */
    public void setDanmakuListener(OnDanmakuListener danmakuListener) {
        mDanmakuListener = danmakuListener;
    }

    /**
     * ============================ 播放清晰度 ============================
     */

    // 默认显示/隐藏选择分辨率界面时间
    private static final int DEFAULT_QUALITY_TIME = 300;
    /**
     * 依次分别为：流畅、清晰、高清、超清和1080P
     */
    public static final int MEDIA_QUALITY_SMOOTH = 0;
    public static final int MEDIA_QUALITY_MEDIUM = 1;
    public static final int MEDIA_QUALITY_HIGH = 2;
    public static final int MEDIA_QUALITY_SUPER = 3;
    public static final int MEDIA_QUALITY_BD = 4;

    private static final int QUALITY_DRAWABLE_RES[] = new int[]{
            R.mipmap.ic_media_quality_smooth, R.mipmap.ic_media_quality_medium, R.mipmap.ic_media_quality_high,
            R.mipmap.ic_media_quality_super, R.mipmap.ic_media_quality_bd
    };
    // 保存Video Url
    private SparseArray<String> mVideoSource = new SparseArray<>();
    // 描述信息
    private String[] mMediaQualityDesc;
    // 分辨率选择布局
    private View mFlMediaQuality;
    // 清晰度
    private TextView mIvMediaQuality;
    // 分辨率选择列表
    private ListView mLvMediaQuality;
    // 分辨率选择列表适配器
    private AdapterMediaQuality mQualityAdapter;
    // 列表数据
    private List<MediaQualityInfo> mQualityData;
    // 是否显示分辨率选择列表
    private boolean mIsShowQuality = false;
    // 当前选中的分辨率
    private
    @MediaQuality
    int mCurSelectQuality = MEDIA_QUALITY_SMOOTH;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @IntDef({MEDIA_QUALITY_SMOOTH, MEDIA_QUALITY_MEDIUM, MEDIA_QUALITY_HIGH, MEDIA_QUALITY_SUPER, MEDIA_QUALITY_BD})
    public @interface MediaQuality {
    }

    /**
     * 初始化视频分辨率处理
     */
    private void _initMediaQuality() {
        mMediaQualityDesc = getResources().getStringArray(R.array.media_quality);
        mFlMediaQuality = findViewById(R.id.fl_media_quality);
        mIvMediaQuality = (TextView) findViewById(R.id.iv_media_quality);
        mIvMediaQuality.setOnClickListener(this);
        mLvMediaQuality = (ListView) findViewById(R.id.lv_media_quality);
        mQualityAdapter = new AdapterMediaQuality(mAttachActivity);
        mLvMediaQuality.setAdapter(mQualityAdapter);
        mLvMediaQuality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurSelectQuality != mQualityAdapter.getItem(position).getIndex()) {
                    setMediaQuality(mQualityAdapter.getItem(position).getIndex());
                    mLoadingView.setVisibility(VISIBLE);
                    start();
                }
                _toggleMediaQuality();
            }
        });
    }

    /**
     * 设置视频源
     *
     * @param mediaSmooth 流畅
     * @param mediaMedium 清晰
     * @param mediaHigh   高清
     * @param mediaSuper  超清
     * @param mediaBd     1080P
     */
    public IjkPlayerView setVideoSource(String mediaSmooth, String mediaMedium, String mediaHigh, String mediaSuper, String mediaBd) {
        boolean isSelect = true;
        mQualityData = new ArrayList<>();
        if (mediaSmooth != null) {
            mVideoSource.put(MEDIA_QUALITY_SMOOTH, mediaSmooth);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_SMOOTH, mMediaQualityDesc[MEDIA_QUALITY_SMOOTH], isSelect));
            mCurSelectQuality = MEDIA_QUALITY_SMOOTH;
            isSelect = false;
        }
        if (mediaMedium != null) {
            mVideoSource.put(MEDIA_QUALITY_MEDIUM, mediaMedium);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_MEDIUM, mMediaQualityDesc[MEDIA_QUALITY_MEDIUM], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_MEDIUM;
            }
            isSelect = false;
        }
        if (mediaHigh != null) {
            mVideoSource.put(MEDIA_QUALITY_HIGH, mediaHigh);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_HIGH, mMediaQualityDesc[MEDIA_QUALITY_HIGH], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_HIGH;
            }
            isSelect = false;
        }
        if (mediaSuper != null) {
            mVideoSource.put(MEDIA_QUALITY_SUPER, mediaSuper);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_SUPER, mMediaQualityDesc[MEDIA_QUALITY_SUPER], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_SUPER;
            }
            isSelect = false;
        }
        if (mediaBd != null) {
            mVideoSource.put(MEDIA_QUALITY_BD, mediaBd);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_BD, mMediaQualityDesc[MEDIA_QUALITY_BD], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_BD;
            }
        }
        mQualityAdapter.updateItems(mQualityData);
        mIvMediaQuality.setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(mAttachActivity, QUALITY_DRAWABLE_RES[mCurSelectQuality]), null, null);
        mIvMediaQuality.setText(mMediaQualityDesc[mCurSelectQuality]);
        setVideoPath(mVideoSource.get(mCurSelectQuality));
        return this;
    }

    /**
     * 选择视频源
     *
     * @param quality 分辨率
     *                {@link #MEDIA_QUALITY_SMOOTH,#MEDIA_QUALITY_MEDIUM,#MEDIA_QUALITY_HIGH,#MEDIA_QUALITY_SUPER,#MEDIA_QUALITY_BD}
     * @return
     */
    public IjkPlayerView setMediaQuality(@MediaQuality int quality) {
        if (mCurSelectQuality == quality || mVideoSource.get(quality) == null) {
            return this;
        }
        mQualityAdapter.setMediaQuality(quality);
        mIvMediaQuality.setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(mAttachActivity, QUALITY_DRAWABLE_RES[quality]), null, null);
        mIvMediaQuality.setText(mMediaQualityDesc[quality]);
        mCurSelectQuality = quality;
        if (mVideoView.isPlaying()) {
            mCurPosition = mVideoView.getCurrentPosition();
            mVideoView.release(false);
        }
        mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
        setVideoPath(mVideoSource.get(quality));
        return this;
    }

    /**
     * ============================ 跳转提示 ============================
     */

    // 取消跳转
    private ImageView mIvCancelSkip;
    // 跳转时间
    private TextView mTvSkipTime;
    // 执行跳转
    private TextView mTvDoSkip;
    // 跳转布局
    private View mLlSkipLayout;
    // 跳转目标时间
    private int mSkipPosition = INVALID_VALUE;

    /**
     * 跳转提示初始化
     */
    private void _initVideoSkip() {
        mLlSkipLayout = findViewById(R.id.ll_skip_layout);
        mIvCancelSkip = (ImageView) findViewById(R.id.iv_cancel_skip);
        mTvSkipTime = (TextView) findViewById(R.id.tv_skip_time);
        mTvDoSkip = (TextView) findViewById(R.id.tv_do_skip);
        mIvCancelSkip.setOnClickListener(this);
        mTvDoSkip.setOnClickListener(this);
    }

    /**
     * 返回当前进度
     *
     * @return
     */
    public int getCurPosition() {
        return mVideoView.getCurrentPosition();
    }

    /**
     * 设置跳转提示
     *
     * @param targetPosition 目标进度,单位:ms
     */
    public IjkPlayerView setSkipTip(int targetPosition) {
        mSkipPosition = targetPosition;
        return this;
    }

    /**
     * 显示跳转提示
     */
    private void _showSkipTip() {
        if (mSkipPosition != INVALID_VALUE && mLlSkipLayout.getVisibility() == GONE) {
            mLlSkipLayout.setVisibility(VISIBLE);
            mTvSkipTime.setText(generateTime(mSkipPosition));
            AnimHelper.doSlideRightIn(mLlSkipLayout, mWidthPixels, 0, 800);
            mHandler.postDelayed(mHideSkipTipRunnable, DEFAULT_HIDE_TIMEOUT * 3);
        }
    }

    /**
     * 隐藏跳转提示
     */
    private void _hideSkipTip() {
        if (mLlSkipLayout.getVisibility() == GONE) {
            return;
        }
        ViewCompat.animate(mLlSkipLayout).translationX(-mLlSkipLayout.getWidth()).alpha(0).setDuration(500)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        mLlSkipLayout.setVisibility(GONE);
                    }
                }).start();
        mSkipPosition = INVALID_VALUE;
    }

    /**
     * 隐藏跳转提示线程
     */
    private Runnable mHideSkipTipRunnable = new Runnable() {
        @Override
        public void run() {
            _hideSkipTip();
        }
    };

    /**
     * ============================ 弹幕 ============================
     */

    /**
     * 视频编辑状态：正常未编辑状态、在播放时编辑、暂停时编辑
     */
    private static final int NORMAL_STATUS = 501;
    private static final int INTERRUPT_WHEN_PLAY = 502;
    private static final int INTERRUPT_WHEN_PAUSE = 503;

    private int mVideoStatus = NORMAL_STATUS;

    // 弹幕格式：B站、A站和自定义
    private static final int DANMAKU_TAG_BILI = 701;
    private static final int DANMAKU_TAG_ACFUN = 702;
    private static final int DANMAKU_TAG_CUSTOM = 703;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @IntDef({DANMAKU_TAG_BILI, DANMAKU_TAG_ACFUN, DANMAKU_TAG_CUSTOM})
    public @interface DanmakuTag {
    }

    private
    @DanmakuTag
    int mDanmakuTag = DANMAKU_TAG_BILI;

    // 弹幕开源控件
    private IDanmakuView mDanmakuView;
    // 弹幕显示/隐藏按钮
    private ImageView mIvDanmakuControl;
    // 弹幕编辑布局打开按钮
    private TextView mTvOpenEditDanmaku;
    // 使能弹幕才会显示的播放进度条
    private SeekBar mDanmakuPlayerSeek;
    // 使能弹幕才会显示时间分割线
    private TextView mTvTimeSeparator;
    // 弹幕编辑布局
    private View mEditDanmakuLayout;
    // 弹幕内容编辑框
    private EditText mEtDanmakuContent;
    // 取消弹幕发送
    private ImageView mIvCancelSend;
    // 发送弹幕
    private ImageView mIvDoSend;

    // 弹幕基础设置布局
    private View mDanmakuOptionsBasic;
    // 弹幕字体大小选项卡
    private RadioGroup mDanmakuTextSizeOptions;
    // 弹幕类型选项卡
    private RadioGroup mDanmakuTypeOptions;
    // 弹幕当前颜色
    private RadioButton mDanmakuCurColor;
    // 开关弹幕颜色选项卡
    private ImageView mDanmakuMoreColorIcon;
    // 弹幕更多颜色设置布局
    private View mDanmakuMoreOptions;
    // 弹幕颜色选项卡
    private RadioGroup mDanmakuColorOptions;

    // 弹幕控制相关
    private DanmakuContext mDanmakuContext;
    // 弹幕解析器
    private BaseDanmakuParser mDanmakuParser;
    // 弹幕加载器
    private ILoader mDanmakuLoader;
    // 弹幕数据转换器
    private BaseDanmakuConverter mDanmakuConverter;
    // 弹幕监听器
    private OnDanmakuListener mDanmakuListener;
    // 是否使能弹幕
    private boolean mIsEnableDanmaku = false;
    // 弹幕颜色
    private int mDanmakuTextColor = Color.WHITE;
    // 弹幕字体大小
    private float mDanmakuTextSize = INVALID_VALUE;
    // 弹幕类型
    private int mDanmakuType = BaseDanmaku.TYPE_SCROLL_RL;
    // 弹幕基础设置布局的宽度
    private int mBasicOptionsWidth = INVALID_VALUE;
    // 弹幕更多颜色设置布局宽度
    private int mMoreOptionsWidth = INVALID_VALUE;
    // 弹幕要跳转的目标位置，等视频播放再跳转，不然老出现只有弹幕在动的情况
    private long mDanmakuTargetPosition = INVALID_VALUE;

    /**
     * 弹幕初始化
     */
    private void _initDanmaku() {
        // 弹幕控制
        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
        mIvDanmakuControl = (ImageView) findViewById(R.id.iv_danmaku_control);
        mTvOpenEditDanmaku = (TextView) findViewById(R.id.tv_open_edit_danmaku);
        mTvTimeSeparator = (TextView) findViewById(R.id.tv_separator);
        mEditDanmakuLayout = findViewById(R.id.ll_edit_danmaku);
        mEtDanmakuContent = (EditText) findViewById(R.id.et_danmaku_content);
        mIvCancelSend = (ImageView) findViewById(R.id.iv_cancel_send);
        mIvDoSend = (ImageView) findViewById(R.id.iv_do_send);
        mDanmakuPlayerSeek = (SeekBar) findViewById(R.id.danmaku_player_seek);
        mDanmakuPlayerSeek.setMax(MAX_VIDEO_SEEK);
        mDanmakuPlayerSeek.setOnSeekBarChangeListener(mSeekListener);
        mIvDanmakuControl.setOnClickListener(this);
        mTvOpenEditDanmaku.setOnClickListener(this);
        mIvCancelSend.setOnClickListener(this);
        mIvDoSend.setOnClickListener(this);

        int navigationBarHeight = NavUtils.getNavigationBarHeight(mAttachActivity);
        if (navigationBarHeight > 0) {
            // 对于有虚拟键的设备需要将弹幕编辑布局右偏移防止被覆盖
            mEditDanmakuLayout.setPadding(0, 0, navigationBarHeight, 0);
        }

        // 这些为弹幕配置处理
        int oneBtnWidth = getResources().getDimensionPixelOffset(R.dimen.danmaku_input_options_color_radio_btn_size);
        // 布局宽度为每个选项卡宽度 * 12 个，有12种可选颜色
        mMoreOptionsWidth = oneBtnWidth * 12;
        mDanmakuOptionsBasic = findViewById(R.id.input_options_basic);
        mDanmakuMoreOptions = findViewById(R.id.input_options_more);
        mDanmakuMoreOptions.setOnClickListener(this);
        mDanmakuCurColor = (RadioButton) findViewById(R.id.input_options_color_current);
        mDanmakuMoreColorIcon = (ImageView) findViewById(R.id.input_options_color_more_icon);
        mDanmakuTextSizeOptions = (RadioGroup) findViewById(R.id.input_options_group_textsize);
        mDanmakuTypeOptions = (RadioGroup) findViewById(R.id.input_options_group_type);
        mDanmakuColorOptions = (RadioGroup) findViewById(R.id.input_options_color_group);
        mDanmakuTextSizeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.input_options_small_textsize) {
                    mDanmakuTextSize = 25f * (mDanmakuParser.getDisplayer().getDensity() - 0.6f) * 0.7f;
                } else if (checkedId == R.id.input_options_medium_textsize) {
                    mDanmakuTextSize = 25f * (mDanmakuParser.getDisplayer().getDensity() - 0.6f);
                }
            }
        });
        mDanmakuTypeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.input_options_rl_type) {
                    mDanmakuType = BaseDanmaku.TYPE_SCROLL_RL;
                } else if (checkedId == R.id.input_options_top_type) {
                    mDanmakuType = BaseDanmaku.TYPE_FIX_TOP;
                } else if (checkedId == R.id.input_options_bottom_type) {
                    mDanmakuType = BaseDanmaku.TYPE_FIX_BOTTOM;
                }
            }
        });
        mDanmakuColorOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 取的是 tag 字符串值，需转换为颜色
                String color = (String) findViewById(checkedId).getTag();
                mDanmakuTextColor = Color.parseColor(color);
                mDanmakuCurColor.setBackgroundColor(mDanmakuTextColor);
            }
        });
    }

    /**
     * 装载弹幕，在视频按了播放键才装载
     */
    private void _loadDanmaku() {
        if (mIsEnableDanmaku) {
            // 设置弹幕
            mDanmakuContext = DanmakuContext.create();
            //同步弹幕和video，貌似没法保持同步，可能我用的有问题，先注释掉- -
//            mDanmakuContext.setDanmakuSync(new VideoDanmakuSync(this));
            if (mDanmakuParser == null) {
                mDanmakuParser = new BaseDanmakuParser() {
                    @Override
                    protected Danmakus parse() {
                        return new Danmakus();
                    }
                };
            }
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    // 这里处理下有时调用 _resumeDanmaku() 时弹幕还没 prepared 的情况
                    if (mVideoView.isPlaying() && !mIsBufferingStart) {
                        mDanmakuView.start();
                    }
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                }

                @Override
                public void drawingFinished() {
                }
            });
            mDanmakuView.enableDanmakuDrawingCache(true);
            Log.e("IjkPlayerView", "_loadDanmaku");
            mDanmakuView.prepare(mDanmakuParser, mDanmakuContext);
        }
    }

    /**
     * 使能弹幕功能
     *
     * @return
     */
    public IjkPlayerView enableDanmaku() {
        mIsEnableDanmaku = true;
        _initDanmaku();
        if (mIsAlwaysFullScreen) {
            _toggleDanmakuView(true);
        }
        return this;
    }

    /**
     * 设置弹幕资源，默认资源格式需满足 bilibili 的弹幕文件格式，
     * 配合{@link #setDanmakuCustomParser}来进行自定义弹幕解析方式，{@link #setDanmakuCustomParser}必须先调用
     *
     * @param stream 弹幕资源
     * @return
     */
    public IjkPlayerView setDanmakuSource(InputStream stream) {
        if (stream == null) {
            return this;
        }
        if (!mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        }
        if (mDanmakuLoader == null) {
            mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        }
        try {
            mDanmakuLoader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        IDataSource<?> dataSource = mDanmakuLoader.getDataSource();
        if (mDanmakuParser == null) {
            mDanmakuParser = new BiliDanmukuParser();
        }
        mDanmakuParser.load(dataSource);
        return this;
    }

    /**
     * 设置弹幕资源，默认资源格式需满足 bilibili 的弹幕文件格式，
     * 配合{@link #setDanmakuCustomParser}来进行自定义弹幕解析方式，{@link #setDanmakuCustomParser}必须先调用
     *
     * @param uri 弹幕资源
     * @return
     */
    public IjkPlayerView setDanmakuSource(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return this;
        }
        if (!mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        }
        if (mDanmakuLoader == null) {
            mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        }
        try {
            mDanmakuLoader.load(uri);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        IDataSource<?> dataSource = mDanmakuLoader.getDataSource();
        if (mDanmakuParser == null) {
            mDanmakuParser = new BiliDanmukuParser();
        }
        mDanmakuParser.load(dataSource);
        return this;
    }

    /**
     * 自定义弹幕解析器，配合{@link #setDanmakuSource}使用，先于{@link #setDanmakuSource}调用
     *
     * @param parser    解析器
     * @param loader    加载器
     * @param converter 转换器
     * @return
     */
    public IjkPlayerView setDanmakuCustomParser(BaseDanmakuParser parser, ILoader loader, BaseDanmakuConverter converter) {
        mDanmakuParser = parser;
        mDanmakuLoader = loader;
        mDanmakuConverter = converter;
        return this;
    }

    /**
     * 显示/隐藏弹幕
     *
     * @param isShow 是否显示
     * @return
     */
    public IjkPlayerView showOrHideDanmaku(boolean isShow) {
        if (isShow) {
            mIvDanmakuControl.setSelected(false);
            mDanmakuView.show();
        } else {
            mIvDanmakuControl.setSelected(true);
            mDanmakuView.hide();
        }
        return this;
    }

    /**
     * 发射弹幕
     *
     * @param text   内容
     * @param isLive 是否直播
     * @return  弹幕数据
     */
    public void sendDanmaku(String text, boolean isLive) {
        if (!mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        }
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(mAttachActivity, "内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mDanmakuView.isPrepared()) {
            Toast.makeText(mAttachActivity, "弹幕还没准备好", Toast.LENGTH_SHORT).show();
            return;
        }
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(mDanmakuType);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        if (mDanmakuTextSize == INVALID_VALUE) {
            mDanmakuTextSize = 25f * (mDanmakuParser.getDisplayer().getDensity() - 0.6f);
        }
        danmaku.text = text;
        danmaku.padding = 5;
        danmaku.isLive = isLive;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.textSize = mDanmakuTextSize;
        danmaku.textColor = mDanmakuTextColor;
        danmaku.underlineColor = Color.GREEN;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 500);
        mDanmakuView.addDanmaku(danmaku);

        if (mDanmakuListener != null) {
            if (mDanmakuConverter != null) {
                mDanmakuListener.onDataObtain(mDanmakuConverter.convertDanmaku(danmaku));
            } else {
                mDanmakuListener.onDataObtain(danmaku);
            }
        }
    }

    /**
     * 编辑操作前调用，会控制视频的播放状态，如在编辑弹幕前调用，配合{@link #recoverFromEditVideo()}使用
     */
    public void editVideo() {
        if (mVideoView.isPlaying()) {
            pause();
            mVideoStatus = INTERRUPT_WHEN_PLAY;
        } else {
            mVideoStatus = INTERRUPT_WHEN_PAUSE;
        }
        _hideAllView(false);
    }

    /**
     * 从编辑状态返回，如取消编辑或发射弹幕后配合{@link #editVideo()}调用
     *
     * @return 是否从编辑状态回退
     */
    public boolean recoverFromEditVideo() {
        if (mVideoStatus == NORMAL_STATUS) {
            return false;
        }
        if (mIsFullscreen) {
            _recoverScreen();
        }
        if (mVideoStatus == INTERRUPT_WHEN_PLAY) {
            start();
        }
        mVideoStatus = NORMAL_STATUS;
        return true;
    }

    /**
     * 激活弹幕
     */
    private void _resumeDanmaku() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            if (mDanmakuTargetPosition != INVALID_VALUE) {
                mDanmakuView.seekTo(mDanmakuTargetPosition);
                mDanmakuTargetPosition = INVALID_VALUE;
            } else {
                mDanmakuView.resume();
            }
        }
    }

    /**
     * 暂停弹幕
     */
    private void _pauseDanmaku() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    /**
     * 切换弹幕的显示/隐藏
     */
    private void _toggleDanmakuShow() {
        if (mIvDanmakuControl.isSelected()) {
            showOrHideDanmaku(true);
        } else {
            showOrHideDanmaku(false);
        }
    }

    /**
     * 切换弹幕相关控件View的显示/隐藏
     *
     * @param isShow 是否显示
     */
    private void _toggleDanmakuView(boolean isShow) {
        if (mIsEnableDanmaku) {
            if (isShow) {
                mIvDanmakuControl.setVisibility(VISIBLE);
                mTvOpenEditDanmaku.setVisibility(VISIBLE);
                mTvTimeSeparator.setVisibility(VISIBLE);
                mDanmakuPlayerSeek.setVisibility(VISIBLE);
                mPlayerSeek.setVisibility(GONE);
            } else {
                mIvDanmakuControl.setVisibility(GONE);
                mTvOpenEditDanmaku.setVisibility(GONE);
                mTvTimeSeparator.setVisibility(GONE);
                mDanmakuPlayerSeek.setVisibility(GONE);
                mPlayerSeek.setVisibility(VISIBLE);
            }
        }

    }

    /**
     * 从弹幕编辑状态复原界面
     */
    private void _recoverScreen() {
        // 清除焦点
        mEditDanmakuLayout.clearFocus();
        mEditDanmakuLayout.setVisibility(GONE);
        // 关闭软键盘
        SoftInputUtils.closeSoftInput(mAttachActivity);
        // 重新设置全屏界面UI标志位
        _setUiLayoutFullscreen();
        if (mDanmakuColorOptions.getWidth() != 0) {
            _toggleMoreColorOptions();
        }
    }

    /**
     * 动画切换弹幕颜色选项卡显示
     */
    private void _toggleMoreColorOptions() {
        if (mBasicOptionsWidth == INVALID_VALUE) {
            mBasicOptionsWidth = mDanmakuOptionsBasic.getWidth();
        }
        if (mDanmakuColorOptions.getWidth() == 0) {
            AnimHelper.doClipViewWidth(mDanmakuOptionsBasic, mBasicOptionsWidth, 0, 300);
            AnimHelper.doClipViewWidth(mDanmakuColorOptions, 0, mMoreOptionsWidth, 300);
            ViewCompat.animate(mDanmakuMoreColorIcon).rotation(180).setDuration(150).setStartDelay(250).start();
        } else {
            AnimHelper.doClipViewWidth(mDanmakuOptionsBasic, 0, mBasicOptionsWidth, 300);
            AnimHelper.doClipViewWidth(mDanmakuColorOptions, mMoreOptionsWidth, 0, 300);
            ViewCompat.animate(mDanmakuMoreColorIcon).rotation(0).setDuration(150).setStartDelay(250).start();
        }
    }

    /**
     * ============================ 电量、时间、锁屏、截屏 ============================
     */

    // 电量显示
    private ProgressBar mPbBatteryLevel;
    // 系统时间显示
    private TextView mTvSystemTime;
    // 截图按钮
    private ImageView mIvScreenshot;
    // 电量变化广播接收器
    private BatteryBroadcastReceiver mBatteryReceiver;
    // 锁屏状态广播接收器
    private ScreenBroadcastReceiver mScreenReceiver;
    // 判断是否出现锁屏,有则需要重新设置渲染器，不然视频会没有动画只有声音
    private boolean mIsScreenLocked = false;
    // 截图分享弹框
    private ShareDialog mShareDialog;
    // 对话框点击监听，内部和外部
    private ShareDialog.OnDialogClickListener mDialogClickListener;
    private ShareDialog.OnDialogClickListener mInsideDialogClickListener = new ShareDialog.OnDialogClickListener() {
        @Override
        public void onShare(Bitmap bitmap, Uri uri) {
            if (mDialogClickListener != null) {
                mDialogClickListener.onShare(bitmap, mVideoView.getUri());
            }
            File file = new File(mSaveDir, System.currentTimeMillis() + ".jpg");
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                Toast.makeText(mAttachActivity, "保存成功，路径为:" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(mAttachActivity, "保存本地失败", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private ShareDialog.OnDialogDismissListener mDialogDismissListener = new ShareDialog.OnDialogDismissListener() {
        @Override
        public void onDismiss() {
            recoverFromEditVideo();
        }
    };
    // 截图保存路径
    private File mSaveDir;

    /**
     * 初始化电量、锁屏、时间处理
     */
    private void _initReceiver() {
        mPbBatteryLevel = (ProgressBar) findViewById(R.id.pb_battery);
        mTvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        mTvSystemTime.setText(StringUtils.getCurFormatTime());
        mBatteryReceiver = new BatteryBroadcastReceiver();
        mScreenReceiver = new ScreenBroadcastReceiver();
        //注册接受广播
        mAttachActivity.registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        mAttachActivity.registerReceiver(mScreenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        mIvScreenshot = (ImageView) findViewById(R.id.iv_screenshot);
        mIvScreenshot.setOnClickListener(this);
        if (SDCardUtils.isAvailable()) {
            _createSaveDir(SDCardUtils.getRootPath() + File.separator + "IjkPlayView");
        }
    }

    /**
     * 截图
     */
    private void _doScreenshot() {
        editVideo();
        _showShareDialog(mVideoView.getScreenshot());
    }

    /**
     * 显示对话框
     *
     * @param bitmap
     */
    private void _showShareDialog(Bitmap bitmap) {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog();
            mShareDialog.setClickListener(mInsideDialogClickListener);
            mShareDialog.setDismissListener(mDialogDismissListener);
            if (mDialogClickListener != null) {
                mShareDialog.setShareMode(true);
            }
        }
        mShareDialog.setScreenshotPhoto(bitmap);
        mShareDialog.show(mAttachActivity.getSupportFragmentManager(), "share");
    }

    /**
     * 设置截图分享监听
     *
     * @param dialogClickListener
     * @return
     */
    public IjkPlayerView setDialogClickListener(ShareDialog.OnDialogClickListener dialogClickListener) {
        mDialogClickListener = dialogClickListener;
        if (mShareDialog != null) {
            mShareDialog.setShareMode(true);
        }
        return this;
    }

    /**
     * 创建目录
     *
     * @param path
     */
    private void _createSaveDir(String path) {
        mSaveDir = new File(path);
        if (!mSaveDir.exists()) {
            mSaveDir.mkdirs();
        } else if (!mSaveDir.isDirectory()) {
            mSaveDir.delete();
            mSaveDir.mkdirs();
        }
    }

    /**
     * 设置截图保存路径
     *
     * @param path
     */
    public IjkPlayerView setSaveDir(String path) {
        _createSaveDir(path);
        return this;
    }

    /**
     * 接受电量改变广播
     */
    class BatteryBroadcastReceiver extends BroadcastReceiver {

        // 低电量临界值
        private static final int BATTERY_LOW_LEVEL = 15;

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收电量变化信息
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                // 电量百分比
                int curPower = level * 100 / scale;
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_HEALTH_UNKNOWN);
                // SecondaryProgress 用来展示低电量，Progress 用来展示正常电量
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    mPbBatteryLevel.setSecondaryProgress(0);
                    mPbBatteryLevel.setProgress(curPower);
                    mPbBatteryLevel.setBackgroundResource(R.mipmap.ic_battery_charging);
                } else if (curPower < BATTERY_LOW_LEVEL) {
                    mPbBatteryLevel.setProgress(0);
                    mPbBatteryLevel.setSecondaryProgress(curPower);
                    mPbBatteryLevel.setBackgroundResource(R.mipmap.ic_battery_red);
                } else {
                    mPbBatteryLevel.setSecondaryProgress(0);
                    mPbBatteryLevel.setProgress(curPower);
                    mPbBatteryLevel.setBackgroundResource(R.mipmap.ic_battery);
                }
            }
        }
    }

    /**
     * 锁屏状态广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                mIsScreenLocked = true;
            }
        }
    }
}
