package com.dl7.myapp.module.detail;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.NewsDetailBean;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.views.EmptyLayout;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by long on 2016/8/25.
 * 新闻详情 Presenter
 */
public class NewsDetailPresenter implements IBasePresenter {

    private final String mNewsId;
    private final INewsDetailView mView;

    public NewsDetailPresenter(String newsId, INewsDetailView view) {
        this.mNewsId = newsId;
        this.mView = view;
    }

    @Override
    public void loadData() {
        _getData();
    }

    @Override
    public void loadMoreData() {
    }

    /**
     * 获取数据
     */
    private void _getData() {
        RetrofitService.getNewsDetail(mNewsId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .subscribe(new Subscriber<NewsDetailBean>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError(new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                loadData();
                            }
                        });
                    }

                    @Override
                    public void onNext(NewsDetailBean newsDetailBean) {
                        mView.loadData(newsDetailBean);
                    }
                });
    }
}
