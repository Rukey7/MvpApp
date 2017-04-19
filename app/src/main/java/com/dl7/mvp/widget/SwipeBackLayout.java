package com.dl7.mvp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dl7.mvp.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by long on 2017/1/19.
 * 开源项目：https://github.com/ikew0ng/SwipeBackLayout
 */
public class SwipeBackLayout extends FrameLayout {

    // 最小快速滑动速度
    private static final int MIN_FLING_VELOCITY = 400; // dips per second
    // 默认遮罩颜色
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    // 透明度基数
    private static final int FULL_ALPHA = 255;
    // 默认滚动退出临界值，超过这个值就滑动退出
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;
    // 超出滚动距离，就是在 activity 从界面滑出它的宽或高的基础上在多滑动 OVER_SCROLL_DISTANCE，这个应该是让滑出效果体验更好
    private static final int OVER_SCROLL_DISTANCE = 10;
    /**
     * 边缘拖拽使能标志位，支持左右和下边缘
     * 暂时不支持 EDGE_ALL，滑动会有问题，详见 @see {@link ViewDragCallback#clampViewPositionHorizontal}
     */
    public static final int EDGE_INVALID = -1;
    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;
    // @see {@link #writeFile(String, InputStream, boolean)}
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT | EDGE_BOTTOM;

    // 拖拽标志
    private int mEdgeFlag;
    // 滚动临界值
    private float mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
    // 绑定的Activity
    private Activity mAttachActivity;
    // 使能
    private boolean mEnableScroll = true;
    // Activity的ContentView
    private View mContentView;
    // 拖拽帮助类
    private ViewDragHelper mDragHelper;
    // 滚动百分比{0~1}
    private float mScrollPercent;
    // ContentView的左偏移
    private int mContentLeft;
    // ContentView的上偏移
    private int mContentTop;
    // 左边缘阴影
    private Drawable mShadowLeft;
    // 右边缘阴影
    private Drawable mShadowRight;
    // 下边缘阴影
    private Drawable mShadowBottom;
    // 遮罩不透明度百分比
    private float mScrimOpacity;
    // 遮罩颜色
    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    // 是否正在处理onLayout()
    private boolean mInLayout;
    //
    private Rect mContentRect = new Rect();
    // 拖拽边缘
    private int mTrackingEdge = EDGE_INVALID;


    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init();
    }

    private void _init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mInLayout = true;
        if (mContentView != null) {
            mContentView.layout(mContentLeft, mContentTop,
                    mContentLeft + mContentView.getMeasuredWidth(),
                    mContentTop + mContentView.getMeasuredHeight());
        }
        mInLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == mContentView;
        // 绘制子控件
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (mScrimOpacity > 0 && drawContent
                && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            // 根据拖拽状态来绘制阴影和遮罩
            _drawShadow(canvas, child);
            _drawScrim(canvas, child);
        }
        return ret;
    }

    /**
     * 绘制遮罩
     *
     * @param canvas
     * @param child
     */
    private void _drawScrim(Canvas canvas, View child) {
        // 获取遮罩颜色的基础透明度，>>>为无符号右移，因为透明度保存在最高位字节里0xff000000，所以不能用 >>
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        // 在基础透明度的基础上根据 mScrimOpacity 来改变透明度
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        // 遮罩的最终显示颜色
        final int color = alpha << 24 | (mScrimColor & 0xffffff);
        // 绘制遮罩，clipRect裁剪出遮罩大小
        if ((mTrackingEdge & EDGE_LEFT) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    /**
     * 绘制阴影
     *
     * @param canvas
     * @param child
     */
    private void _drawShadow(Canvas canvas, View child) {
        child.getHitRect(mContentRect);

        if ((mEdgeFlag & EDGE_LEFT) != 0) {
            mShadowLeft.setBounds(mContentRect.left - mShadowLeft.getIntrinsicWidth(), mContentRect.top,
                    mContentRect.left, mContentRect.bottom);
            mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowLeft.draw(canvas);
        }

        if ((mEdgeFlag & EDGE_RIGHT) != 0) {
            mShadowRight.setBounds(mContentRect.right, mContentRect.top,
                    mContentRect.right + mShadowRight.getIntrinsicWidth(), mContentRect.bottom);
            mShadowRight.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowRight.draw(canvas);
        }

        if ((mEdgeFlag & EDGE_BOTTOM) != 0) {
            mShadowBottom.setBounds(mContentRect.left, mContentRect.bottom, mContentRect.right,
                    mContentRect.bottom + mShadowBottom.getIntrinsicHeight());
            mShadowBottom.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowBottom.draw(canvas);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnableScroll) {
            return false;
        }
        try {
            return mDragHelper.shouldInterceptTouchEvent(event);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableScroll) {
            return false;
        }
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            boolean edgeTouched = mDragHelper.isEdgeTouched(mEdgeFlag, pointerId);
            if (edgeTouched) {
                if (mDragHelper.isEdgeTouched(EDGE_LEFT, pointerId)) {
                    mTrackingEdge = EDGE_LEFT;
                } else if (mDragHelper.isEdgeTouched(EDGE_RIGHT, pointerId)) {
                    mTrackingEdge = EDGE_RIGHT;
                } else if (mDragHelper.isEdgeTouched(EDGE_BOTTOM, pointerId)) {
                    mTrackingEdge = EDGE_BOTTOM;
                }
            }
            boolean directionCheck = false;
            if (mEdgeFlag == EDGE_LEFT || mEdgeFlag == EDGE_RIGHT) {
                // 左右边缘则检测竖直方向的滑动
                directionCheck = !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, pointerId);
            } else if (mEdgeFlag == EDGE_BOTTOM) {
                // 下边缘则检测水平方向的滑动
                directionCheck = !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, pointerId);
            } else if (mEdgeFlag == EDGE_ALL) {
                directionCheck = true;
            }
            return edgeTouched && directionCheck;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            // 返回0则不能水平滑动
            return mEdgeFlag & (EDGE_LEFT | EDGE_RIGHT);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            // 返回0则不能垂直滑动
            return mEdgeFlag & EDGE_BOTTOM;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            /**
             * 注意：原开源项目中的 ViewDragHelper 是自己写的，感兴趣的可以对比两者中的 shouldInterceptTouchEvent() 方法的区别;
             * 这里和原开源项目处理不同，用 mEdgeFlag 判断而不是 mTrackingEdge，因为系统的 clampViewPositionHorizontal()
             * 会先于 tryCaptureView() 调用，且如果水平方向上这里没发生移动就不会调用 tryCaptureView()，mTrackingEdge 是在
             * tryCaptureView() 赋值的，所以这边要做些判断
             */
            // 这里控制 ContentView 的水平滑动范围
            int edgeFlag = mTrackingEdge == EDGE_INVALID ? mEdgeFlag : mTrackingEdge;
            if ((edgeFlag & EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
            } else if ((edgeFlag & EDGE_RIGHT) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }
            return ret;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int ret = 0;
            // 这里控制 ContentView 的垂直滑动范围
            int edgeFlag = mTrackingEdge == EDGE_INVALID ? mEdgeFlag : mTrackingEdge;
            if ((edgeFlag & EDGE_BOTTOM) != 0) {
                ret = Math.min(0, Math.max(top, -child.getHeight()));
            }
            return ret;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowLeft.getIntrinsicWidth()));
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowRight.getIntrinsicWidth()));
            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                mScrollPercent = Math.abs((float) top / (mContentView.getHeight() + mShadowBottom.getIntrinsicHeight()));
            }
            mContentLeft = left;
            mContentTop = top;
            // 调用这个来绘制背景遮罩和阴影
            invalidate();

            if (mScrollPercent >= 1) {
                // 滑动超过1则销毁 Activity
                if (!mAttachActivity.isFinishing()) {
                    mAttachActivity.finish();
                    // 不显示 Activity 切换动画
                    mAttachActivity.overridePendingTransition(0, 0);
                }
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            final int childHeight = releasedChild.getHeight();
            // 计算 ContentView 最终滑动目标 left 和 top
            // 这里说明下 xvel 和 yvel，他俩的取值范围为系统判断触摸滑动fling事件的最大和最小滑动速率，小于 mMinVelocity 统统返回0
            int left = 0, top = 0;
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && mScrollPercent > mScrollThreshold ? childWidth
                        + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE : 0;
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                left = xvel < 0 || xvel == 0 && mScrollPercent > mScrollThreshold ? -(childWidth
                        + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE) : 0;
            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                top = yvel < 0 || yvel == 0 && mScrollPercent > mScrollThreshold ? -(childHeight
                        + mShadowBottom.getIntrinsicHeight() + OVER_SCROLL_DISTANCE) : 0;
            }
            // 让拖拽视图滑动到指定位置
            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                mTrackingEdge = EDGE_INVALID;
            }
        }
    }

    /**
     ================================== 外部调用 ==================================
     */

    /**
     * 设置控制滑动的 ContentView，也就是关联 Activity 的界面视图
     *
     * @param view
     */
    private void _setContentView(View view) {
        mContentView = view;
    }

    /**
     * 设置关联的 Activity
     * 重要!必须调用
     *
     * @param activity
     */
    public void attachToActivity(Activity activity, @EdgeFlag int edgeFlag) {
        mAttachActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        _setContentView(decorChild);
        decor.addView(this);
        setEdgeFlag(edgeFlag);
    }

    public int getEdgeFlag() {
        return mEdgeFlag;
    }

    public void setEdgeFlag(@EdgeFlag int edgeFlag) {
        mEdgeFlag = edgeFlag;
        mDragHelper.setEdgeTrackingEnabled(edgeFlag);
        if ((mEdgeFlag & EDGE_LEFT) != 0 && mShadowLeft == null) {
            mShadowLeft = ContextCompat.getDrawable(mAttachActivity, R.drawable.ic_shadow_left);
        }
        if ((mEdgeFlag & EDGE_RIGHT) != 0 && mShadowRight == null) {
            mShadowRight = ContextCompat.getDrawable(mAttachActivity, R.drawable.ic_shadow_right);
        }
        if ((mEdgeFlag & EDGE_BOTTOM) != 0 && mShadowBottom == null) {
            mShadowBottom = ContextCompat.getDrawable(mAttachActivity, R.drawable.ic_shadow_bottom);
        }
        invalidate();
    }

    public float getScrollThreshold() {
        return mScrollThreshold;
    }

    public void setScrollThreshold(float scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }

    public boolean isEnableScroll() {
        return mEnableScroll;
    }

    public void setEnableScroll(boolean enableScroll) {
        mEnableScroll = enableScroll;
    }

    public void setShadowLeft(Drawable shadowLeft) {
        mShadowLeft = shadowLeft;
    }

    public void setShadowRight(Drawable shadowRight) {
        mShadowRight = shadowRight;
    }

    public void setShadowBottom(Drawable shadowBottom) {
        mShadowBottom = shadowBottom;
    }

    public int getScrimColor() {
        return mScrimColor;
    }

    public void setScrimColor(int scrimColor) {
        mScrimColor = scrimColor;
    }

    /**
     * 设置可拖拽的边缘大小
     *
     * @param edgeSize
     */
    public void setEdgeSize(int edgeSize) {
        try {
            Field edgeSizeField = mDragHelper.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            edgeSizeField.setInt(mDragHelper, edgeSize);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * ==================================  ==================================
     */

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @IntDef({EDGE_LEFT, EDGE_RIGHT, EDGE_BOTTOM, EDGE_ALL})
    public @interface EdgeFlag {
    }
}
