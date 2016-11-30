package com.dl7.mvp.module.photo.welfarephoto;

import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.api.bean.WelfarePhotoBean;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.utils.ImageLoader;
import com.dl7.mvp.views.EmptyLayout;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by long on 2016/10/11.
 * 福利图片界面 Presenter
 */
public class WelfarePhotoPresenter implements IBasePresenter {

    private WelfarePhotoFragment mView;


    public WelfarePhotoPresenter(WelfarePhotoFragment view) {
        this.mView = view;
    }


    @Override
    public void getData() {
        RetrofitService.getWelfarePhoto()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .observeOn(Schedulers.io())
                // 接口返回的数据是没有宽高参数的，所以这里设置图片的宽度和高度，速度会慢一点
                .filter(new Func1<WelfarePhotoBean, Boolean>() {
                    @Override
                    public Boolean call(WelfarePhotoBean photoBean) {
                        try {
                            photoBean.setPixel(ImageLoader.calePhotoSize(mView.getContext(), photoBean.getUrl()));
                            return true;
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            return false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<WelfarePhotoBean>>() {
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
                    public void onNext(List<WelfarePhotoBean> photoList) {
                        mView.loadData(photoList);
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getMoreWelfarePhoto()
                .observeOn(Schedulers.io())
                // 接口返回的数据是没有宽高参数的，所以这里设置图片的宽度和高度，速度会慢一点
                .filter(new Func1<WelfarePhotoBean, Boolean>() {
                    @Override
                    public Boolean call(WelfarePhotoBean photoBean) {
                        try {
                            photoBean.setPixel(ImageLoader.calePhotoSize(mView.getContext(), photoBean.getUrl()));
                            return true;
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            return false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<WelfarePhotoBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<WelfarePhotoBean> photoList) {
                        mView.loadMoreData(photoList);
                    }
                });
    }
}
