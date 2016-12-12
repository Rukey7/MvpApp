package com.dl7.mvp.module.video.videos;

import com.dl7.mvp.local.table.VideoBeanDao;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.photo.photos.IPhotosView;
import com.dl7.mvp.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by long on 2016/10/11.
 */

public class VideosPresenter implements IRxBusPresenter {

    private final IPhotosView mView;
    private final VideoBeanDao mDbDao;
    private final RxBus mRxBus;

    public VideosPresenter(IPhotosView view, VideoBeanDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void getData() {
        mView.updateCount((int) mDbDao.queryBuilder().where(VideoBeanDao.Properties.IsCollect.eq(true)).count());
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
