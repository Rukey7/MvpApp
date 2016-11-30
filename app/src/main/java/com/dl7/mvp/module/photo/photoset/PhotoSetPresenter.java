package com.dl7.mvp.module.photo.photoset;

import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.api.bean.PhotoSetBean;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.views.EmptyLayout;
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
    public void getData() {
        RetrofitService.getPhotoSet(mPhotoSetId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .subscribe(new Subscriber<PhotoSetBean>() {
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
                    public void onNext(PhotoSetBean photoSetBean) {
                        mView.loadData(photoSetBean);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }
}
