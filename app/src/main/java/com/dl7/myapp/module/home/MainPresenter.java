package com.dl7.myapp.module.home;

import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.local.table.NewsTypeBeanDao;
import com.dl7.myapp.module.base.IBasePresenter;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by long on 2016/9/1.
 * 主页 Presenter
 */
public class MainPresenter implements IBasePresenter {

    private final IMainView mView;
    private final NewsTypeBeanDao mDbDao;

    public MainPresenter(IMainView view, NewsTypeBeanDao dbDao) {
        mView = view;
        mDbDao = dbDao;
    }


    @Override
    public void getData() {
        mDbDao.queryBuilder().rx().list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<NewsTypeBean>>() {
                    @Override
                    public void call(List<NewsTypeBean> newsTypeBeen) {
                        mView.loadData(newsTypeBeen);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }
}
