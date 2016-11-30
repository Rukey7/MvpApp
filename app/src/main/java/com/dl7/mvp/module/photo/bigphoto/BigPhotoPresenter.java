package com.dl7.mvp.module.photo.bigphoto;

import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.local.table.BeautyPhotoBean;
import com.dl7.mvp.local.table.BeautyPhotoBeanDao;
import com.dl7.mvp.module.base.ILoadDataView;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.mvp.rxbus.event.LoveEvent;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by long on 2016/9/27.
 * 大图 Presenter
 */
public class BigPhotoPresenter implements ILocalPresenter<BeautyPhotoBean> {

    private final ILoadDataView mView;
    private final BeautyPhotoBeanDao mDbDao;
    private final List<BeautyPhotoBean> mPhotoList;
    private final RxBus mRxBus;
    private List<BeautyPhotoBean> mDbLovedData;


    public BigPhotoPresenter(ILoadDataView view, BeautyPhotoBeanDao dbDao, List<BeautyPhotoBean> photoList, RxBus rxBus) {
        this.mView = view;
        this.mDbDao = dbDao;
        this.mPhotoList = photoList;
        this.mRxBus = rxBus;
        mDbLovedData = mDbDao.queryBuilder().list();
    }

    @Override
    public void getData() {
        // 因为网易这个原接口参数一大堆，我只传了部分参数，返回的数据会出现图片重复的情况，请不要在意这个问题- -
        Observable.from(mPhotoList)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                // 判断数据库是否有数据，有则设置对应参数
                .doOnNext(new Action1<BeautyPhotoBean>() {
                    BeautyPhotoBean tmpBean;

                    @Override
                    public void call(BeautyPhotoBean bean) {
                        if (mDbLovedData.contains(bean)) {
                            tmpBean = mDbLovedData.get(mDbLovedData.indexOf(bean));
                            bean.setLove(tmpBean.isLove());
                            bean.setPraise(tmpBean.isPraise());
                            bean.setDownload(tmpBean.isDownload());
                        }
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<BeautyPhotoBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List<BeautyPhotoBean> photoList) {
                        mView.loadData(photoList);
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getMoreBeautyPhoto()
                .flatMap(new Func1<List<BeautyPhotoBean>, Observable<BeautyPhotoBean>>() {
                    @Override
                    public Observable<BeautyPhotoBean> call(List<BeautyPhotoBean> photoList) {
                        return Observable.from(photoList);
                    }
                })
                // 判断数据库是否有数据，有则设置对应参数
                .doOnNext(new Action1<BeautyPhotoBean>() {
                    BeautyPhotoBean tmpBean;

                    @Override
                    public void call(BeautyPhotoBean bean) {
                        if (mDbLovedData.contains(bean)) {
                            tmpBean = mDbLovedData.get(mDbLovedData.indexOf(bean));
                            bean.setLove(tmpBean.isLove());
                            bean.setPraise(tmpBean.isPraise());
                            bean.setDownload(tmpBean.isDownload());
                        }
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<BeautyPhotoBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List<BeautyPhotoBean> photoList) {
                        mView.loadMoreData(photoList);
                    }
                });
    }

    @Override
    public void insert(BeautyPhotoBean data) {
        if (mDbLovedData.contains(data)) {
            mDbDao.update(data);
        } else {
            mDbDao.insert(data);
            mDbLovedData.add(data);
        }
        mRxBus.post(new LoveEvent());
    }

    @Override
    public void delete(BeautyPhotoBean data) {
        if (!data.isLove() && !data.isDownload() && !data.isPraise()) {
            mDbDao.delete(data);
            mDbLovedData.remove(data);
        } else {
            mDbDao.update(data);
        }
        mRxBus.post(new LoveEvent());
    }

    @Override
    public void update(List<BeautyPhotoBean> list) {
    }
}
