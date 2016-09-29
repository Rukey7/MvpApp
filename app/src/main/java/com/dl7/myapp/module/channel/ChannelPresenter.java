package com.dl7.myapp.module.channel;

import com.dl7.myapp.local.dao.NewsTypeDao;
import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.local.table.NewsTypeBeanDao;
import com.dl7.myapp.module.base.ILocalPresenter;
import com.dl7.myapp.rxbus.RxBus;
import com.dl7.myapp.rxbus.event.ChannelEvent;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by long on 2016/9/1.
 * 栏目管理 Presenter
 */
public class ChannelPresenter implements ILocalPresenter<NewsTypeBean> {

    private final IChannelView mView;
    private final NewsTypeBeanDao mDbDao;
    private final RxBus mRxBus;

    public ChannelPresenter(IChannelView view, NewsTypeBeanDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }


    @Override
    public void getData() {
        // 从数据库获取
        final List<NewsTypeBean> checkList = mDbDao.queryBuilder().list();
        final List<String> typeList = new ArrayList<>();
        for (NewsTypeBean bean : checkList) {
            typeList.add(bean.getTypeId());
        }
        Observable.from(NewsTypeDao.getAllChannels())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<NewsTypeBean, Boolean>() {
                    @Override
                    public Boolean call(NewsTypeBean newsTypeBean) {
                        // 过滤掉已经选中的栏目
                        return !typeList.contains(newsTypeBean.getTypeId());
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<NewsTypeBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List<NewsTypeBean> uncheckList) {
                        mView.loadData(checkList, uncheckList);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public void insert(NewsTypeBean data) {
        mDbDao.rx().insert(data)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewsTypeBean>() {
                    @Override
                    public void onCompleted() {
                        mRxBus.post(new ChannelEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(NewsTypeBean newsTypeBean) {
                        Logger.w(newsTypeBean.toString());
                    }
                });
    }

    @Override
    public void delete(NewsTypeBean data) {
        mDbDao.rx().delete(data)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        mRxBus.post(new ChannelEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    @Override
    public void update(List<NewsTypeBean> list) {
        // 这做法不太妥当，而且列表在交互位置时可能产生很多无用的交互没处理掉，暂时没想到简便的方法来处理，以后有想法再改。
        Observable.from(list)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 清空数据库
                        mDbDao.deleteAll();
                    }
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(new Subscriber<NewsTypeBean>() {
                    @Override
                    public void onCompleted() {
                        mRxBus.post(new ChannelEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(NewsTypeBean newsTypeBean) {
                        // 把ID清除再加入数据库会从新按列表顺序递增ID
                        newsTypeBean.setId(null);
                        mDbDao.save(newsTypeBean);
                    }
                });
    }

}
