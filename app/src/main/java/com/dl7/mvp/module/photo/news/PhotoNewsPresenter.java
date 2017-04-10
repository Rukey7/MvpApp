package com.dl7.mvp.module.photo.news;

import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.api.bean.PhotoInfo;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.base.ILoadDataView;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by long on 2016/9/5.
 * 图片新闻列表 Presenter
 */
public class PhotoNewsPresenter implements IBasePresenter {

    private String mNextSetId;
    private ILoadDataView mView;


    public PhotoNewsPresenter(ILoadDataView view) {
        this.mView = view;
    }


    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getPhotoList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<List<PhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.showNetError();
                    }

                    @Override
                    public void onNext(List<PhotoInfo> photoList) {
                        mView.loadData(photoList);
                        mNextSetId = photoList.get(photoList.size() - 1).getSetid();
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getPhotoMoreList(mNextSetId)
                .compose(mView.<List<PhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<PhotoInfo> photoList) {
                        mView.loadMoreData(photoList);
                    }
                });
    }
}
