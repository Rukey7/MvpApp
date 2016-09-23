package com.dl7.myapp.module.photos;

import com.dl7.myapp.local.table.NewsTypeBeanDao;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.rxbus.RxBus;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by long on 2016/9/23.
 * 图片专栏 Presenter
 */
public class PhotosPresenter implements IRxBusPresenter {

    private final IPhotosView mView;
    private final NewsTypeBeanDao mDbDao;
    private final RxBus mRxBus;

    public PhotosPresenter(IPhotosView view, NewsTypeBeanDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }


    @Override
    public void getData() {
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType) {
        Subscription subscription = mRxBus.doSubscribe(eventType, new Action1<T>() {
            @Override
            public void call(T t) {
                getData();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        mRxBus.addSubscription(this, subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }
}
