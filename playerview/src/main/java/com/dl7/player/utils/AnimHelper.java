package com.dl7.player.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by long on 2016/11/4.
 */

public final class AnimHelper {

    private AnimHelper() {
        throw new AssertionError();
    }


    /**
     * 执行从右滑入动画
     * @param view
     * @param startX
     * @param endX
     * @param duration
     */
    public static void doSlideRightIn(View view, int startX, int endX, int duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", startX, endX);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(translationX, alpha);
        set.start();
    }

    /**
     * 裁剪视图宽度
     * @param view
     * @param srcWidth
     * @param endWidth
     * @param duration
     */
    public static void doClipViewWidth(final View view, int srcWidth, int endWidth, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(srcWidth, endWidth).setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int width = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = width;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    /**
     * 裁剪视图宽度
     * @param view
     * @param srcHeight
     * @param endHeight
     * @param duration
     */
    public static void doClipViewHeight(final View view, int srcHeight, int endHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(srcHeight, endHeight).setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int width = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = width;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }
}
