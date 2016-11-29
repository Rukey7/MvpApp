package com.dl7.myapp.injector.modules;

import com.dl7.myapp.adapter.RelatedNewsAdapter;
import com.dl7.myapp.injector.PerActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.detail.NewsDetailActivity;
import com.dl7.myapp.module.detail.NewsDetailPresenter;
import com.dl7.recycler.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/8/25.
 * 新闻详情 Module
 */
@Module
public class NewsDetailModule {

    private final String mNewsId;
    private final NewsDetailActivity mView;

    public NewsDetailModule(NewsDetailActivity view, String newsId) {
        mNewsId = newsId;
        mView = view;
    }

    @PerActivity
    @Provides
    public BaseQuickAdapter provideRelatedAdapter() {
        return new RelatedNewsAdapter(mView);
    }

    @PerActivity
    @Provides
    public IBasePresenter providePresenter() {
        return new NewsDetailPresenter(mNewsId, mView);
    }
}
