package com.dl7.mvp.engine.danmaku;

import com.dl7.mvp.local.table.DanmakuInfo;
import com.dl7.player.danmaku.BaseDanmakuConverter;

import master.flame.danmaku.danmaku.model.BaseDanmaku;

/**
 * Created by long on 2016/12/22.
 * 弹幕数据转换器
 */
public class DanmakuConverter extends BaseDanmakuConverter<DanmakuInfo> {

    private DanmakuConverter(){}
    private static volatile DanmakuConverter instance;

    public static DanmakuConverter instance() {
        if(instance == null){
            synchronized (DanmakuConverter.class){
                if(instance == null)
                    instance = new DanmakuConverter();
            }
        }
        return instance;
    }

    @Override
    public DanmakuInfo convertDanmaku(BaseDanmaku danmaku) {
        DanmakuInfo data = new DanmakuInfo();
        // 弹幕基础数据初始化，重要！记得调用
        initData(data, danmaku);
        return data;
    }
}
