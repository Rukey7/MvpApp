package com.dl7.myapp.module.manage;

import com.dl7.myapp.local.table.NewsTypeBeanDao;
import com.dl7.myapp.module.base.IBasePresenter;
import com.orhanobut.logger.Logger;

/**
 * Created by long on 2016/9/1.
 * 栏目管理 Presenter
 */
public class ManagePresenter implements IBasePresenter {

    private final IManageView mView;
    private final NewsTypeBeanDao mDbDao;

    public ManagePresenter(IManageView view, NewsTypeBeanDao dbDao) {
        mView = view;
        mDbDao = dbDao;
    }


    @Override
    public void getData() {
        Logger.e(mDbDao.queryBuilder().list().toString());
    }

    @Override
    public void getMoreData() {
    }
}
