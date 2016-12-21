package com.dl7.mvp.module.video.fullscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.dl7.downloaderlib.FileDownloader;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.player.media.IjkPlayerView;
import com.orhanobut.logger.Logger;

import static com.dl7.mvp.utils.CommonConstant.VIDEO_DATA_KEY;

/**
 * Created by long on 2016/12/19.
 * 固定全屏video界面，用来播放缓存的video
 */
public class VideoFullscreenActivity extends AppCompatActivity {

    private IjkPlayerView mPlayerView;
    private VideoInfo mVideoData;


    public static void launch(Context context, VideoInfo data) {
        Intent intent = new Intent(context, VideoFullscreenActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoData = getIntent().getParcelableExtra(VIDEO_DATA_KEY);
        mPlayerView = new IjkPlayerView(this);
        setContentView(mPlayerView);
        Logger.w(FileDownloader.getFilePathByUrl(mVideoData.getVideoUrl()));
        mPlayerView.init()
                .alwaysFullScreen()
                .enableOrientation()
                .setVideoPath(FileDownloader.getFilePathByUrl(mVideoData.getVideoUrl()))
                .setTitle(mVideoData.getTitle())
                .start();
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
