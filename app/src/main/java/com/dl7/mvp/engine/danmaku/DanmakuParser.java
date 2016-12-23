package com.dl7.mvp.engine.danmaku;

import android.graphics.Color;
import android.text.TextUtils;

import com.dl7.mvp.local.table.DanmakuInfo;
import com.dl7.mvp.utils.GsonHelper;

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
        Danmakus danmakus = new Danmakus();
        if (TextUtils.isEmpty(jsonStr)) {
            return danmakus;
        }
        try {
            List<DanmakuInfo> datas = GsonHelper.convertEntities(jsonStr, DanmakuInfo.class);
            int size = datas.size();
            for (int i = 0; i < size; i++) {
                BaseDanmaku item = mContext.mDanmakuFactory.createDanmaku(datas.get(i).type, mContext);
                if (item != null) {
                    item.setTime(datas.get(i).time);
                    item.textSize = datas.get(i).textSize;
                    item.textColor = datas.get(i).textColor;
                    item.textShadowColor = textColor <= Color.BLACK ? Color.WHITE : Color.BLACK;
                    DanmakuUtils.fillText(item, datas.get(i).content);
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
