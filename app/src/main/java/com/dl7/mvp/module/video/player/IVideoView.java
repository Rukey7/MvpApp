package com.dl7.mvp.module.video.player;

import com.dl7.mvp.local.table.DanmakuInfo;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.module.base.IBaseView;

import java.util.List;

/**
 * Created by long on 2016/12/23.
 * Video接口
 */
public interface IVideoView extends IBaseView {

    /**
     * 获取Video数据
     * @param data 数据
     */
    void loadData(VideoInfo data);

    /**
     * 获取弹幕数据
     * @param dataList 数据
     */
    void loadDanmakuData(List<DanmakuInfo> dataList);

}
