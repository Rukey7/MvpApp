package com.dl7.mvp.module.news.detail;

import com.dl7.mvp.api.RetrofitService;
import com.dl7.mvp.api.bean.NewsDetailInfo;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.utils.ListUtils;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by long on 2016/8/25.
 * 新闻详情 Presenter
 */
@Deprecated
public class NewsDetailPresenter implements IBasePresenter {

    private static final String HTML_IMG_TEMPLATE = "<img src=\"http\" />";

    private final String mNewsId;
    private final INewsDetailView mView;

    public NewsDetailPresenter(String newsId, INewsDetailView view) {
        this.mNewsId = newsId;
        this.mView = view;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getNewsDetail(mNewsId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .doOnNext(new Action1<NewsDetailInfo>() {
                    @Override
                    public void call(NewsDetailInfo newsDetailBean) {
                        _handleRichTextWithImg(newsDetailBean);
                    }
                })
                .compose(mView.<NewsDetailInfo>bindToLife())
                .subscribe(new Subscriber<NewsDetailInfo>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showNetError();
                    }

                    @Override
                    public void onNext(NewsDetailInfo newsDetailBean) {
                        mView.loadData(newsDetailBean);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    /**
     * 处理富文本包含图片的情况
     * @param newsDetailBean    原始数据
     */
    private void _handleRichTextWithImg(NewsDetailInfo newsDetailBean) {
        if (!ListUtils.isEmpty(newsDetailBean.getImg())) {
            String body = newsDetailBean.getBody();
            for (NewsDetailInfo.ImgEntity imgEntity : newsDetailBean.getImg()) {
                String ref = imgEntity.getRef();
                String src = imgEntity.getSrc();
                String img = HTML_IMG_TEMPLATE.replace("http", src);
                body = body.replaceAll(ref, img);
                Logger.w(img);
                Logger.i(body);
            }
            newsDetailBean.setBody(body);
        }
    }
}
