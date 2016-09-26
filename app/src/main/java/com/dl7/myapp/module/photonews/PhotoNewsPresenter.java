package com.dl7.myapp.module.photonews;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.PhotoBean;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.base.ILoadDataView;
import com.dl7.myapp.views.EmptyLayout;
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
    public void getData() {
        RetrofitService.getPhotoList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .subscribe(new Subscriber<List<PhotoBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.showNetError(new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getData();
                            }
                        });
                    }

                    @Override
                    public void onNext(List<PhotoBean> photoList) {
                        mView.loadData(photoList);
                        mNextSetId = photoList.get(photoList.size() - 1).getSetid();
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getPhotoMoreList(mNextSetId)
                .subscribe(new Subscriber<List<PhotoBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<PhotoBean> photoList) {
                        mView.loadMoreData(photoList);
                    }
                });
    }
}
