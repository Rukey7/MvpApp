package com.dl7.myapp.module.news;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.view.EmptyLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;

/**
 * Created by long on 2016/8/23.
 * 新闻列表 Presenter
 */
public class NewsListPresenter implements IBasePresenter {

    private INewsListView mView;

    public NewsListPresenter(INewsListView view) {
        this.mView = view;
    }

    @Override
    public void updateViews() {
        _getData();
    }

    private void _getData() {
        Logger.w("_getData");
        mView.showLoading();
        RetrofitService.getNewsList(RetrofitService.NEWS_HEAD_LINE)
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
                                updateViews();
                            }
                        });
                    }

                    @Override
                    public void onNext(List<NewsBean> newsList) {
                        Logger.w(newsList.size()+"");
                        Logger.i(newsList.toString());
                        mView.updateList(newsList);
                    }
                });
    }


}
