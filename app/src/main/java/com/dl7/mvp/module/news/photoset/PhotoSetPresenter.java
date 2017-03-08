package com.dl7.mvp.module.news.photoset;

import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.api.bean.PhotoSetInfo;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.widget.EmptyLayout;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by long on 2016/8/29.
 * 图集 Presenter
 */
public class PhotoSetPresenter implements IBasePresenter {

    private final IPhotoSetView mView;
    private final String mPhotoSetId;

    public PhotoSetPresenter(IPhotoSetView view, String photoSetId) {
        mView = view;
        mPhotoSetId = photoSetId;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getPhotoSet(mPhotoSetId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<PhotoSetInfo>bindToLife())
                .subscribe(new Subscriber<PhotoSetInfo>() {
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
                                getData(false);
                            }
                        });
                    }

                    @Override
                    public void onNext(PhotoSetInfo photoSetBean) {
                        mView.loadData(photoSetBean);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }
}
