package com.dl7.mvp.module.video.videolist;

import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.local.table.VideoBean;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.base.ILoadDataView;
import com.dl7.mvp.views.EmptyLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by long on 2016/10/11.
 */

public class VideoListPresenter implements IBasePresenter {

    final private ILoadDataView mView;
    final private String mVideoId;


    public VideoListPresenter(ILoadDataView view, String videoId) {
        this.mView = view;
        this.mVideoId = videoId;
    }


    @Override
    public void getData() {
        RetrofitService.getVideoList(mVideoId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .subscribe(new Subscriber<List<VideoBean>>() {
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
                    public void onNext(List<VideoBean> videoList) {
                        mView.loadData(videoList);
                    }
                });

    }

    @Override
    public void getMoreData() {
        RetrofitService.getVideoListNext(mVideoId)
                .subscribe(new Subscriber<List<VideoBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<VideoBean> videoList) {
                        mView.loadMoreData(videoList);
                    }
                });
    }

}
