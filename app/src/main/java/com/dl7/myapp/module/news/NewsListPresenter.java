package com.dl7.myapp.module.news;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.view.EmptyLayout;

import java.util.List;

import rx.Subscriber;

/**
 * Created by long on 2016/8/23.
 * 新闻列表 Presenter
 */
public class NewsListPresenter implements IBasePresenter {

    private INewsListView mView;
    private int mNewsType;

    public NewsListPresenter(INewsListView view, @RetrofitService.NewsType int newsType) {
        this.mView = view;
        this.mNewsType = newsType;
    }

    @Override
    public void loadData() {
        _getData();
    }

    @Override
    public void loadMoreData() {
        _getMoreData();
    }

    /**
     * 获取数据
     */
    private void _getData() {
        mView.showLoading();
        RetrofitService.getNewsList(mNewsType)
                .subscribe(new Subscriber<List<NewsBean>>() {
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
                    public void onNext(List<NewsBean> newsList) {
                        mView.loadData(newsList);
                    }
                });
    }

    /**
     * 获取更多数据
     */
    private void _getMoreData() {
        RetrofitService.getNewsListNext(mNewsType)
                .subscribe(new Subscriber<List<NewsBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<NewsBean> newsList) {
                        mView.loadMoreData(newsList);
                    }
                });
    }
}
