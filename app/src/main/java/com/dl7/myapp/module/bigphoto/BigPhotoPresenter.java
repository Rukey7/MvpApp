package com.dl7.myapp.module.bigphoto;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.BeautyPhotoBean;
import com.dl7.myapp.api.bean.BeautyPhotoBeanDao;
import com.dl7.myapp.module.base.ILoadDataView;
import com.dl7.myapp.module.base.ILocalPresenter;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by long on 2016/9/27.
 * 大图 Presenter
 */
public class BigPhotoPresenter implements ILocalPresenter<BeautyPhotoBean> {

    private final ILoadDataView mView;
    private final BeautyPhotoBeanDao mDbDao;
    private final List<BeautyPhotoBean> mPhotoList;
    private List<BeautyPhotoBean> mDbLovedData;


    public BigPhotoPresenter(ILoadDataView view, BeautyPhotoBeanDao dbDao, List<BeautyPhotoBean> photoList) {
        this.mView = view;
        this.mDbDao = dbDao;
        this.mPhotoList = photoList;
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
                .distinct()
                .map(new Func1<BeautyPhotoBean, String>() {
                    @Override
                    public String call(BeautyPhotoBean beautyPhotoBean) {
                        return beautyPhotoBean.getImgsrc();
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(List<String> imgUrls) {
                        mView.loadData(imgUrls);
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
                .map(new Func1<BeautyPhotoBean, String>() {
                    @Override
                    public String call(BeautyPhotoBean beautyPhotoBean) {
                        return beautyPhotoBean.getImgsrc();
                    }
                })
                .distinct()
                .toList()
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<String> photoList) {
                        mView.loadMoreData(photoList);
                    }
                });
    }

    @Override
    public void insert(BeautyPhotoBean data) {
        data.setLove(true);
        if (!mDbLovedData.contains(data)) {
            mDbDao.save(data);
        }
    }

    @Override
    public void delete(BeautyPhotoBean data) {
        mDbDao.delete(data);
        data.setLove(false);
    }

    @Override
    public void update(List<BeautyPhotoBean> list) {
    }
}
