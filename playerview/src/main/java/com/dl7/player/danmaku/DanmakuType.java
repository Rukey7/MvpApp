package com.dl7.player.danmaku;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static master.flame.danmaku.danmaku.model.BaseDanmaku.TYPE_FIX_BOTTOM;
import static master.flame.danmaku.danmaku.model.BaseDanmaku.TYPE_FIX_TOP;
import static master.flame.danmaku.danmaku.model.BaseDanmaku.TYPE_SCROLL_RL;

/**
 * Created by long on 2016/12/22.
 * 限定3种弹幕类型:
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@IntDef({TYPE_SCROLL_RL, TYPE_FIX_TOP, TYPE_FIX_BOTTOM})
public @interface DanmakuType {
}
