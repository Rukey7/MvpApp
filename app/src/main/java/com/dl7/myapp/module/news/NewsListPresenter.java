package com.dl7.myapp.module.news;

import com.dl7.myapp.api.NewsUtils;
import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.entity.NewsMultiItem;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.views.EmptyLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by long on 2016/8/23.
 * 新闻列表 Presenter
 */
public class NewsListPresenter implements IBasePresenter {

    private INewsListView mView;
    private String mNewsId;

    public NewsListPresenter(INewsListView view, String newsId) {
        this.mView = view;
        this.mNewsId = newsId;
    }

    @Override
    public void getData() {
        RetrofitService.getNewsList(mNewsId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .filter(new Func1<NewsBean, Boolean>() {
                    @Override
                    public Boolean call(NewsBean newsBean) {
                        if (NewsUtils.isAbNews(newsBean)) {
                            mView.loadAdData(newsBean);
                        }
                        return !NewsUtils.isAbNews(newsBean);
                    }
                })
                .map(new Func1<NewsBean, NewsMultiItem>() {
                    @Override
                    public NewsMultiItem call(NewsBean newsBean) {
                        if (NewsUtils.isNewsPhotoSet(newsBean.getSkipType())) {
                            return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_PHOTO_SET, newsBean);
                        }
                        return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_NORMAL, newsBean);
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<NewsMultiItem>>() {
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
                    public void onNext(List<NewsMultiItem> newsMultiItems) {
                        mView.loadData(newsMultiItems);
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getNewsListNext(mNewsId)
                .map(new Func1<NewsBean, NewsMultiItem>() {
                    @Override
                    public NewsMultiItem call(NewsBean newsBean) {
                        if (NewsUtils.isNewsPhotoSet(newsBean.getSkipType())) {
                            return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_PHOTO_SET, newsBean);
                        }
                        return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_NORMAL, newsBean);
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<NewsMultiItem>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<NewsMultiItem> newsList) {
                        mView.loadMoreData(newsList);
                    }
                });
    }
}
