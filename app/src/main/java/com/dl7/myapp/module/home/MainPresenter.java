package com.dl7.myapp.module.home;

import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.local.table.NewsTypeBeanDao;
import com.dl7.myapp.module.base.IRxBusPresenter;
import com.dl7.myapp.rxbus.RxBus;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by long on 2016/9/1.
 * 主页 Presenter
 */
public class MainPresenter implements IRxBusPresenter {

    private final IMainView mView;
    private final NewsTypeBeanDao mDbDao;
    private final RxBus mRxBus;
    private Subscription mRxSubscription;

    public MainPresenter(IMainView view, NewsTypeBeanDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }


    @Override
    public void getData() {
        mDbDao.queryBuilder().rx().list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<NewsTypeBean>>() {
                    @Override
                    public void call(List<NewsTypeBean> newsTypeBeen) {
                        mView.loadData(newsTypeBeen);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType) {
        mRxSubscription = mRxBus.toObservable(eventType)
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        getData();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    public void unregisterRxBus() {
        if (!mRxSubscription.isUnsubscribed()) {
            mRxSubscription.unsubscribe();
        }
    }
}
