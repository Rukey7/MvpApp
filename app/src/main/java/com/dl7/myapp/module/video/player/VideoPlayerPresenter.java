package com.dl7.myapp.module.video.player;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.myapp.local.table.VideoBean;
import com.dl7.myapp.local.table.VideoBeanDao;
import com.dl7.myapp.module.base.ILoadDataView;
import com.dl7.myapp.module.base.ILocalPresenter;
import com.dl7.myapp.rxbus.RxBus;

import java.util.List;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by long on 2016/11/30.
 * Video Presenter
 */
public class VideoPlayerPresenter implements ILocalPresenter<VideoBean> {

    private final ILoadDataView mView;
    private final VideoBeanDao mDbDao;
    private final RxBus mRxBus;
    private final VideoBean mVideoData;
    private boolean mIsContains = false;

    public VideoPlayerPresenter(ILoadDataView view, VideoBeanDao dbDao, RxBus rxBus, VideoBean videoData) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
        mVideoData = videoData;
        mIsContains = mDbDao.queryBuilder().list().contains(videoData);
    }

    @Override
    public void getData() {
        mDbDao.queryBuilder().rx()
                .oneByOne()
                .filter(new Func1<VideoBean, Boolean>() {
                    @Override
                    public Boolean call(VideoBean videoBean) {
                        return mVideoData.equals(videoBean);
                    }
                })
                .subscribe(new Action1<VideoBean>() {
                    @Override
                    public void call(VideoBean videoBean) {
                        mIsContains = true;
                        mView.loadData(videoBean);
                    }
                });
    }

    @Override
    public void getMoreData() {

    }

    @Override
    public void insert(VideoBean data) {
        if (mIsContains) {
            mDbDao.update(data);
        } else {
            mDbDao.insert(data);
        }
    }

    @Override
    public void delete(VideoBean data) {
        if (!data.isCollect() && data.getDownloadStatus() == DownloadStatus.NORMAL) {
            mDbDao.delete(data);
            mIsContains = false;
        } else {
            mDbDao.update(data);
        }
    }

    @Override
    public void update(List<VideoBean> list) {

    }
}
