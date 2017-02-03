package com.dl7.mvp.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

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

    /**
     * 跳跃动画
     * @param view  视图
     * @param jumpHeight 跳跃高度
     * @param duration  时间
     * @return  Animator
     */
    public static Animator doHappyJump(View view, int jumpHeight, int duration) {
        Keyframe scaleXFrame1 = Keyframe.ofFloat(0f, 1.0f);
        Keyframe scaleXFrame2 = Keyframe.ofFloat(0.05f, 1.5f);
        Keyframe scaleXFrame3 = Keyframe.ofFloat(0.1f, 0.8f);
        Keyframe scaleXFrame4 = Keyframe.ofFloat(0.15f, 1.0f);
        Keyframe scaleXFrame5 = Keyframe.ofFloat(0.5f, 1.0f);
        Keyframe scaleXFrame6 = Keyframe.ofFloat(0.55f, 1.5f);
        Keyframe scaleXFrame7 = Keyframe.ofFloat(0.6f, 0.8f);
        Keyframe scaleXFrame8 = Keyframe.ofFloat(0.65f, 1.0f);
        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofKeyframe("scaleX",
                scaleXFrame1, scaleXFrame2, scaleXFrame3, scaleXFrame4,
                scaleXFrame5, scaleXFrame6, scaleXFrame7, scaleXFrame8);

        Keyframe scaleYFrame1 = Keyframe.ofFloat(0f, 1.0f);
        Keyframe scaleYFrame2 = Keyframe.ofFloat(0.05f, 0.5f);
        Keyframe scaleYFrame3 = Keyframe.ofFloat(0.1f, 1.15f);
        Keyframe scaleYFrame4 = Keyframe.ofFloat(0.15f, 1.0f);
        Keyframe scaleYFrame5 = Keyframe.ofFloat(0.5f, 1.0f);
        Keyframe scaleYFrame6 = Keyframe.ofFloat(0.55f, 0.5f);
        Keyframe scaleYFrame7 = Keyframe.ofFloat(0.6f, 1.15f);
        Keyframe scaleYFrame8 = Keyframe.ofFloat(0.65f, 1.0f);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofKeyframe("scaleY",
                scaleYFrame1, scaleYFrame2, scaleYFrame3, scaleYFrame4,
                scaleYFrame5, scaleYFrame6, scaleYFrame7, scaleYFrame8);

        Keyframe translationY1 = Keyframe.ofFloat(0f, 0f);
        Keyframe translationY2 = Keyframe.ofFloat(0.085f, 0f);
        Keyframe translationY3 = Keyframe.ofFloat(0.2f, -jumpHeight);
        Keyframe translationY4 = Keyframe.ofFloat(0.25f, -jumpHeight);
        Keyframe translationY5 = Keyframe.ofFloat(0.375f, 0f);
        Keyframe translationY6 = Keyframe.ofFloat(0.5f, 0f);
        Keyframe translationY7 = Keyframe.ofFloat(0.585f, 0f);
        Keyframe translationY8 = Keyframe.ofFloat(0.7f, -jumpHeight);
        Keyframe translationY9 = Keyframe.ofFloat(0.75f, -jumpHeight);
        Keyframe translationY10 = Keyframe.ofFloat(0.875f, 0f);
        PropertyValuesHolder translationYHolder = PropertyValuesHolder.ofKeyframe("translationY",
                translationY1, translationY2, translationY3, translationY4, translationY5,
                translationY6, translationY7, translationY8, translationY9, translationY10);

        Keyframe rotationY1 = Keyframe.ofFloat(0f, 0f);
        Keyframe rotationY2 = Keyframe.ofFloat(0.125f, 0f);
        Keyframe rotationY3 = Keyframe.ofFloat(0.3f, -360f * 3);
        PropertyValuesHolder rotationYHolder = PropertyValuesHolder.ofKeyframe("rotationY",
                rotationY1, rotationY2, rotationY3);

        Keyframe rotationX1 = Keyframe.ofFloat(0f, 0f);
        Keyframe rotationX2 = Keyframe.ofFloat(0.625f, 0f);
        Keyframe rotationX3 = Keyframe.ofFloat(0.8f, -360f * 3);
        PropertyValuesHolder rotationXHolder = PropertyValuesHolder.ofKeyframe("rotationX",
                rotationX1, rotationX2, rotationX3);

        ValueAnimator valueAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                scaleXHolder, scaleYHolder, translationYHolder, rotationYHolder, rotationXHolder);
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        return valueAnimator;
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

    /**
     * 垂直偏移动画
     * @param view
     * @param startY
     * @param endY
     * @param duration
     * @return
     */
    public static Animator doMoveVertical(View view, int startY, int endY, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", startY, endY).setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * 动画是否在运行
     * @param animator
     */
    public static boolean isRunning(Animator animator) {
        return animator != null && animator.isRunning();
    }

    /**
     * 启动动画
     * @param animator
     */
    public static void startAnimator(Animator animator) {
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
    }

    /**
     * 停止动画
     * @param animator
     */
    public static void stopAnimator(Animator animator) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    /**
     * 删除动画
     * @param animator
     */
    public static void deleteAnimator(Animator animator) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        animator = null;
    }
}
