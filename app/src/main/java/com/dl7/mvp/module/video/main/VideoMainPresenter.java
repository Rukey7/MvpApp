package com.dl7.mvp.module.video.main;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.mvp.local.table.VideoInfoDao;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by long on 2016/10/11.
 * Video 主界面 Presenter
 */
public class VideoMainPresenter implements IRxBusPresenter {

    private final IVideoMainView mView;
    private final VideoInfoDao mDbDao;
    private final RxBus mRxBus;

    public VideoMainPresenter(IVideoMainView view, VideoInfoDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
        mView.updateLovedCount((int) mDbDao.queryBuilder().where(VideoInfoDao.Properties.IsCollect.eq(true)).count());
        mView.updateDownloadCount((int) mDbDao.queryBuilder()
                .where(VideoInfoDao.Properties.DownloadStatus.notIn(DownloadStatus.NORMAL, DownloadStatus.COMPLETE)).count());
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.e(throwable.toString());
            }
        });
        mRxBus.addSubscription(this, subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }
}
