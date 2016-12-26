package com.dl7.mvp.engine.danmaku;

import android.graphics.Color;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dl7.mvp.local.table.DanmakuInfo;
import com.orhanobut.logger.Logger;

import java.util.List;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.util.DanmakuUtils;

import static android.R.attr.textColor;

/**
 * Created by long on 2016/12/22.
 * 自定义弹幕解析器，参考A站
 */
public class DanmakuParser extends BaseDanmakuParser {

    @Override
    protected IDanmakus parse() {
        if (mDataSource != null && mDataSource instanceof JsonStrSource) {
            JsonStrSource jsonSource = (JsonStrSource) mDataSource;
            return _doParse(jsonSource.data());
        }
        return new Danmakus();
    }

    /**
     * @param jsonStr 弹幕数据
     * @return 转换后的Danmakus
     */
    private Danmakus _doParse(String jsonStr) {
        Logger.w(jsonStr);
        Danmakus danmakus = new Danmakus();
        if (TextUtils.isEmpty(jsonStr)) {
            return danmakus;
        }
        try {
            // 由于 DanmakuInfo 和父类用了相同的字段，用 Gson 直接解析会报错，这里用 FastJson 来处理
//            List<DanmakuInfo> datas = GsonHelper.convertEntities(jsonStr, DanmakuInfo.class);
            List<DanmakuInfo> datas = JSON.parseArray(jsonStr, DanmakuInfo.class);
            Logger.i(datas.toString());
            int size = datas.size();
            for (int i = 0; i < size; i++) {
                BaseDanmaku item = mContext.mDanmakuFactory.createDanmaku(datas.get(i).getType(), mContext);
                if (item != null) {
                    item.setTime(datas.get(i).getTime());
                    item.textSize = datas.get(i).getTextSize();
                    item.textColor = datas.get(i).getTextColor();
                    item.textShadowColor = textColor <= Color.BLACK ? Color.WHITE : Color.BLACK;
                    DanmakuUtils.fillText(item, datas.get(i).getContent());
                    item.index = i;
                    item.setTimer(mTimer);
                    danmakus.addItem(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return danmakus;
    }
}
