package com.dl7.myapp.module.video.videos;

import com.dl7.myapp.local.table.BeautyPhotoBeanDao;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.module.photos.IPhotosView;
import com.dl7.myapp.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by long on 2016/10/11.
 */

public class VideosPresenter implements IRxBusPresenter {

    private final IPhotosView mView;
    private final BeautyPhotoBeanDao mDbDao;
    private final RxBus mRxBus;

    public VideosPresenter(IPhotosView view, BeautyPhotoBeanDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void getData() {
        mView.updateCount((int) mDbDao.queryBuilder().where(BeautyPhotoBeanDao.Properties.IsLove.eq(true)).count());
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
