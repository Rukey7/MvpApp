package com.dl7.mvp.module.photo.main;

import com.dl7.mvp.local.table.BeautyPhotoInfoDao;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by long on 2016/9/23.
 * 图片专栏 Presenter
 */
public class PhotoMainPresenter implements IRxBusPresenter {

    private final IPhotoMainView mView;
    private final BeautyPhotoInfoDao mDbDao;
    private final RxBus mRxBus;

    public PhotoMainPresenter(IPhotoMainView view, BeautyPhotoInfoDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
        mView.updateCount((int) mDbDao.queryBuilder().where(BeautyPhotoInfoDao.Properties.IsLove.eq(true)).count());
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
