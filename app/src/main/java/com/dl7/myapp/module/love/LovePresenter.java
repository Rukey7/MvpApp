package com.dl7.myapp.module.love;

import com.dl7.myapp.local.table.BeautyPhotoBean;
import com.dl7.myapp.local.table.BeautyPhotoBeanDao;
import com.dl7.myapp.module.base.ILocalPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by long on 2016/9/28.
 * 收藏 Presenter
 */
public class LovePresenter implements ILocalPresenter<BeautyPhotoBean> {

    private final ILoveView mView;
    private final BeautyPhotoBeanDao mDbDao;

    public LovePresenter(ILoveView view, BeautyPhotoBeanDao dbDao) {
        mView = view;
        mDbDao = dbDao;
    }

    @Override
    public void getData() {
        Observable.from(mDbDao.queryBuilder().list())
                .filter(new Func1<BeautyPhotoBean, Boolean>() {
                    @Override
                    public Boolean call(BeautyPhotoBean bean) {
                        return bean.isLove();
                    }
                })
                .toList()
                .subscribe(new Action1<List<BeautyPhotoBean>>() {
                    @Override
                    public void call(List<BeautyPhotoBean> list) {
                        if (list.size() == 0) {
                            mView.noData();
                        } else {
                            mView.loadData(list);
                        }
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public void insert(BeautyPhotoBean data) {
    }

    @Override
    public void delete(BeautyPhotoBean data) {
        data.setLove(false);
        if (!data.isLove() && !data.isDownload() && !data.isPraise()) {
            mDbDao.delete(data);
        } else {
            mDbDao.update(data);
        }
        if (mDbDao.queryBuilder().where(BeautyPhotoBeanDao.Properties.IsLove.eq(true)).count() == 0) {
            mView.noData();
        }
    }

    @Override
    public void update(List<BeautyPhotoBean> list) {
    }
}
