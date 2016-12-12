package com.dl7.tag.drawable;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dl7.tag.utils.BitmapUtils;

/**
 * Created by Rukey7 on 2016/12/5.
 * 旋转动画 Drawable
 */
public class RotateDrawable extends Drawable implements Animatable {

    private Paint mPaint;
    // 绘制的矩形框
    private RectF mRect = new RectF();
    // 动画控制
    private ValueAnimator mValueAnimator;
    // 旋转角度
    private float mRotate;
    // icon
    private Bitmap mBitmap;
    // 偏移
    private int mTranslationX;

    public RotateDrawable(Bitmap bitmap, int translationX) {
        mBitmap = bitmap;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTranslationX = translationX;
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.translate(mTranslationX, 0);
        canvas.rotate(mRotate, mRect.width() / 2, mRect.height() / 2);
        canvas.drawPaint(mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(_clipSquare(bounds));
        // 缩放 Bitmap
        Bitmap zoom = BitmapUtils.zoom(mBitmap, bounds.width(), bounds.height());
        BitmapShader bitmapShader = new BitmapShader(zoom, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(bitmapShader);
        if (isRunning()) {
            stop();
        }
        // 设置动画
        mValueAnimator = ValueAnimator.ofFloat(0, 2880).setDuration(3000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            boolean isOver = false;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotate = (float) animation.getAnimatedValue();
                if (mRotate <= 2160) {
                    isOver = false;
                    invalidateSelf();
                } else if (!isOver) {
                    isOver = true;
                    mRotate = 2160;
                    invalidateSelf();
                }
            }
        });
        // 设置动画无限循环
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        start();
    }

    /**
     * 裁剪Rect为正方形
     * @param rect
     * @return
     */
    private Rect _clipSquare(Rect rect) {
        int w = rect.width();
        int h = rect.height();
        int min = Math.min(w, h);
        int cx = rect.centerX();
        int cy = rect.centerY();
        int r = min / 2;
        return new Rect(
                cx - r,
                cy - r,
                cx + r,
                cy + r
        );
    }

    /**
     * ==================================== 显示模式 ====================================
     */

    @Override
    public void start() {
        mValueAnimator.start();
    }

    @Override
    public void stop() {
        mValueAnimator.end();
    }

    @Override
    public boolean isRunning() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }
}
