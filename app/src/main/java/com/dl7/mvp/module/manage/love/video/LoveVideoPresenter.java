package com.dl7.mvp.module.manage.love.video;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.local.table.VideoInfoDao;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.base.ILocalView;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.mvp.rxbus.event.VideoEvent;
import com.dl7.mvp.utils.ListUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by long on 2016/12/13.
 * Video收藏 Presenter
 */
public class LoveVideoPresenter implements ILocalPresenter<VideoInfo> {

    private final ILocalView mView;
    private final VideoInfoDao mDbDao;
    private final RxBus mRxBus;

    public LoveVideoPresenter(ILocalView view, VideoInfoDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder().where(VideoInfoDao.Properties.IsCollect.eq(true))
                .rx()
                .list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoInfo>>() {
                    @Override
                    public void call(List<VideoInfo> videoList) {
                        if (ListUtils.isEmpty(videoList)) {
                            mView.noData();
                        } else {
                            mView.loadData(videoList);
                        }
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public void insert(VideoInfo data) {
    }

    @Override
    public void delete(VideoInfo data) {
        data.setCollect(false);
        if (!data.isCollect() && data.getDownloadStatus() == DownloadStatus.NORMAL) {
            mDbDao.delete(data);
        } else {
            mDbDao.update(data);
        }
        if (mDbDao.queryBuilder().where(VideoInfoDao.Properties.IsCollect.eq(true)).count() == 0) {
            // 如果收藏为0则显示无收藏
            mView.noData();
        }
        mRxBus.post(new VideoEvent());
    }

    @Override
    public void update(List<VideoInfo> list) {
    }
}
