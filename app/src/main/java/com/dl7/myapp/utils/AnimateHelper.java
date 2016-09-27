package com.dl7.myapp.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by long on 2016/9/27.
 * 动画帮助类
 */
public final class AnimateHelper {

    private AnimateHelper() {
        throw new RuntimeException("AnimateHelper cannot be initialized!");
    }

    /**
     * 心跳动画
     * @param view  视图
     * @param duration  时间
     */
    public static void doHeartBeat(View view, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.4f, 0.9f, 1.0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.4f, 0.9f, 1.0f)
        );
        set.setDuration(duration);
        set.start();
    }
}
