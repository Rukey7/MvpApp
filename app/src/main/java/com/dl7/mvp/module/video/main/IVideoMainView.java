package com.dl7.mvp.module.video.main;

/**
 * Created by long on 2016/12/20.
 * video 主界面接口
 */
public interface IVideoMainView {

    /**
     * 更新数据
     * @param lovedCount 收藏数
     */
    void updateLovedCount(int lovedCount);

    /**
     * 更新数据
     * @param downloadCount 下载中个数
     */
    void updateDownloadCount(int downloadCount);
}
