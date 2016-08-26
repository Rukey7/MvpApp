package com.dl7.myapp.module.special;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.NewsItemBean;
import com.dl7.myapp.api.bean.SpecialBean;
import com.dl7.myapp.api.bean.SpecialBean.TopicsEntity;
import com.dl7.myapp.entity.SpecialItem;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.views.EmptyLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by long on 2016/8/26.
 * 专题 Presenter
 */
public class SpecialPresenter implements IBasePresenter {

    private final String mSpecialId;
    private final ISpecialView mView;

    public SpecialPresenter(ISpecialView view, String specialId) {
        this.mSpecialId = specialId;
        this.mView = view;
    }


    @Override
    public void getData() {
        RetrofitService.getSpecial(mSpecialId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .flatMap(new Func1<SpecialBean, Observable<SpecialItem>>() {
                    @Override
                    public Observable<SpecialItem> call(SpecialBean specialBean) {
                        mView.loadBanner(specialBean);
                        return _convertSpecialBeanToItem(specialBean);
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<SpecialItem>>() {
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
                    public void onNext(List<SpecialItem> specialItems) {
                        mView.loadData(specialItems);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }


    private Observable<SpecialItem> _convertSpecialBeanToItem(SpecialBean specialBean) {
        final SpecialItem[] specialItems = new SpecialItem[specialBean.getTopics().size()];
        return Observable.from(specialBean.getTopics())
                    // 获取头部
                    .doOnNext(new Action1<TopicsEntity>() {
                        @Override
                        public void call(TopicsEntity topicsEntity) {
                            Logger.i(topicsEntity.getIndex() + "");
                            int index = topicsEntity.getIndex() - 1;
                            specialItems[index] = new SpecialItem(true, topicsEntity.getTname());
                            Logger.d(topicsEntity.getTname());
                        }
                    })
                    // 排序
                    .toSortedList(new Func2<TopicsEntity, TopicsEntity, Integer>() {
                        @Override
                        public Integer call(TopicsEntity topicsEntity, TopicsEntity topicsEntity2) {
                            return topicsEntity.getIndex() - topicsEntity2.getIndex();
                        }
                    })
                    // 拆分
                    .flatMap(new Func1<List<TopicsEntity>, Observable<TopicsEntity>>() {
                        @Override
                        public Observable<TopicsEntity> call(List<TopicsEntity> topicsEntities) {
                            return Observable.from(topicsEntities);
                        }
                    })
                    .flatMap(new Func1<TopicsEntity, Observable<SpecialItem>>() {
                        @Override
                        public Observable<SpecialItem> call(TopicsEntity topicsEntity) {
                            // 转换并在每个列表项增加头部
                            return Observable.from(topicsEntity.getDocs())
                                    .map(new Func1<NewsItemBean, SpecialItem>() {
                                        @Override
                                        public SpecialItem call(NewsItemBean newsItemBean) {
                                            return new SpecialItem(newsItemBean);
                                        }
                                    })
                                    .startWith(specialItems[topicsEntity.getIndex() - 1]);
                        }
                    });

//
//                .map(new Func1<TopicsEntity, List<NewsItemBean>>() {
//                    @Override
//                    public List<NewsItemBean> call(TopicsEntity topicsEntity) {
//                        return topicsEntity.getDocs();
//                    }
//                })
//                .flatMap(new Func1<List<NewsItemBean>, Observable<SpecialItem>>() {
//                    @Override
//                    public Observable<SpecialItem> call(List<NewsItemBean> newsItemList) {
//                        Observable.from(newsItemList).map(new Func1<NewsItemBean, SpecialItem>() {
//                            @Override
//                            public SpecialItem call(NewsItemBean newsItemBean) {
//                                return new SpecialItem(newsItemBean);
//                            }
//                        }).startWith();
//                        return null;
//                    }
//                })
//
//
//                //
//                .flatMap(new Func1<List<TopicsEntity>, Observable<List<NewsItemBean>>>() {
//                    @Override
//                    public Observable<List<NewsItemBean>> call(List<TopicsEntity> topicsEntities) {
//                        List<List<NewsItemBean>> lists = new ArrayList<List<NewsItemBean>>();
//                        for (TopicsEntity topicsEntity : topicsEntities) {
//                            lists.add(topicsEntity.getDocs());
//                        }
//                        return Observable.from(lists);
//                    }
//                })
//                .subscribe(new Action1<List<NewsItemBean>>() {
//                    @Override
//                    public void call(List<NewsItemBean> newsItemList) {
//                        Observable.from(newsItemList)
//                                .map(new Func1<NewsItemBean, SpecialItem>() {
//                                    @Override
//                                    public SpecialItem call(NewsItemBean newsItemBean) {
//                                        return null;
//                                    }
//                                })
//                    }
//                });

    }
}
