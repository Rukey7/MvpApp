package com.dl7.mvp.module.video.player;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.mvp.local.table.DanmakuInfo;
import com.dl7.mvp.local.table.DanmakuInfoDao;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.local.table.VideoInfoDao;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.mvp.rxbus.event.VideoEvent;
import com.dl7.mvp.utils.GsonHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by long on 2016/11/30.
 * Video Presenter
 */
public class VideoPlayerPresenter implements IVideoPresenter {

    private final IVideoView mView;
    private final VideoInfoDao mDbDao;
    private final RxBus mRxBus;
    private final VideoInfo mVideoData;
    private final DanmakuInfoDao mDanmakuDao;
    // 是否数据库有记录
    private boolean mIsContains = false;

    public VideoPlayerPresenter(IVideoView view, VideoInfoDao dbDao, RxBus rxBus, VideoInfo videoData, DanmakuInfoDao danmakuDao) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
        mVideoData = videoData;
        mDanmakuDao = danmakuDao;
        mIsContains = mDbDao.queryBuilder().list().contains(videoData);
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder().rx()
                .oneByOne()
                .filter(new Func1<VideoInfo, Boolean>() {
                    @Override
                    public Boolean call(VideoInfo videoBean) {
                        return mVideoData.equals(videoBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.<VideoInfo>bindToLife())
                .subscribe(new Action1<VideoInfo>() {
                    @Override
                    public void call(VideoInfo videoBean) {
                        mIsContains = true;
                        mView.loadData(videoBean);
                    }
                });
        mDanmakuDao.queryBuilder().where(DanmakuInfoDao.Properties.Vid.eq(mVideoData.getVid()))
                .rx()
                .list()
                .map(new Func1<List<DanmakuInfo>, InputStream>() {
                    @Override
                    public InputStream call(List<DanmakuInfo> danmakuInfos) {
                        // 有同志提交了关于 Gson 解析的问题处理，这里去掉之前的 Fastjson
//                        String jsonStr = JSON.toJSONString(danmakuInfos);
                        String jsonStr = GsonHelper.object2JsonStr(danmakuInfos);
                        // 将 String 转为 InputStream
                        InputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes());
                        return inputStream;
                    }
                })
                .compose(mView.<InputStream>bindToLife())
                .subscribe(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        mView.loadDanmakuData(inputStream);
                    }
                });
    }

    @Override
    public void getMoreData() {

    }

    @Override
    public void insert(VideoInfo data) {
        if (mIsContains) {
            mDbDao.update(data);
        } else {
            mDbDao.insert(data);
        }
        mRxBus.post(new VideoEvent());
    }

    @Override
    public void delete(VideoInfo data) {
        if (!data.isCollect() && data.getDownloadStatus() == DownloadStatus.NORMAL) {
            mDbDao.delete(data);
            mIsContains = false;
        } else {
            mDbDao.update(data);
        }
        mRxBus.post(new VideoEvent());
    }

    @Override
    public void update(List<VideoInfo> list) {
    }

    @Override
    public void addDanmaku(DanmakuInfo danmakuInfo) {
        mDanmakuDao.insert(danmakuInfo);
    }

    @Override
    public void cleanDanmaku() {
        mDanmakuDao.queryBuilder().where(DanmakuInfoDao.Properties.Vid.eq(mVideoData.getVid()))
                .rx()
                .list()
                .compose(mView.<List<DanmakuInfo>>bindToLife())
                .subscribe(new Action1<List<DanmakuInfo>>() {
                    @Override
                    public void call(List<DanmakuInfo> danmakuInfos) {
                        mDanmakuDao.deleteInTx(danmakuInfos);
                    }
                });
    }
}
