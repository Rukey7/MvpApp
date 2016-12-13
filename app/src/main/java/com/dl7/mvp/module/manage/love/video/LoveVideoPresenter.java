package com.dl7.mvp.module.manage.love.video;

import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.local.table.VideoInfoDao;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.manage.love.ILoveView;
import com.dl7.mvp.rxbus.RxBus;

import java.util.List;

/**
 * Created by long on 2016/12/13.
 * Video收藏 Presenter
 */
public class LoveVideoPresenter implements ILocalPresenter<VideoInfo> {

    private final ILoveView mView;
    private final VideoInfoDao mDbDao;
    private final RxBus mRxBus;

    public LoveVideoPresenter(ILoveView view, VideoInfoDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void insert(VideoInfo data) {

    }

    @Override
    public void delete(VideoInfo data) {

    }

    @Override
    public void update(List<VideoInfo> list) {

    }

    @Override
    public void getData() {

    }

    @Override
    public void getMoreData() {

    }
}
