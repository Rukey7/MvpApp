package com.dl7.player.danmaku;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.FBDanmaku;
import master.flame.danmaku.danmaku.model.FTDanmaku;
import master.flame.danmaku.danmaku.model.R2LDanmaku;

import static master.flame.danmaku.danmaku.model.BaseDanmaku.TYPE_SCROLL_RL;

/**
 * Created by long on 2016/12/22.
 * 弹幕转换器，用来进行自定义数据和弹幕的转换
 */
public abstract class BaseDanmakuConverter<T extends BaseDanmakuData> {

//    BaseDanmaku parseDanmaku(T info);

    public abstract T convertDanmaku(BaseDanmaku danmaku);

    /**
     * 对弹幕的基础数据进行初始化
     * @param data
     * @param danmaku
     */
    protected void initData(T data, BaseDanmaku danmaku) {
        int danmakuType = TYPE_SCROLL_RL;
        if (danmaku instanceof R2LDanmaku) {
            danmakuType = BaseDanmaku.TYPE_SCROLL_RL;
        } else if (danmaku instanceof FBDanmaku) {
            danmakuType = BaseDanmaku.TYPE_FIX_BOTTOM;
        } else if (danmaku instanceof FTDanmaku) {
            danmakuType = BaseDanmaku.TYPE_FIX_TOP;
        }
        data.setType(danmakuType);
        data.setContent(danmaku.text.toString());
        data.setTime(danmaku.getTime());
        data.setTextSize(danmaku.textSize);
        data.setTextColor(danmaku.textColor);
    }
}
