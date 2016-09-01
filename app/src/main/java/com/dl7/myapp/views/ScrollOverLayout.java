package com.dl7.myapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.dl7.myapp.R;

/**
 * Created by long on 2016/8/30.
 */
public class ScrollOverLayout extends FrameLayout {

    // 展开
    public static final int STATUS_EXPANDED = 101;
    // 收缩
    public static final int STATUS_COLLAPSED = 102;
    // 退出
    public static final int STATUS_EXIT = 103;
    // 滑动
    public static final int STATUS_SCROLL = 104;
    private int mScrollStatus = STATUS_COLLAPSED;

    private static final int FLING_VELOCITY = 3000;
    private static final byte NO_FLING = 1;
    private static final byte UP_FLING = 2;
    private static final byte DOWN_FLING = 3;
    private byte mFlingDirection = NO_FLING;

    private Scroller mScroller;
    private GestureDetector mGd;
    private boolean mIsIntercept;
    private int mOldY;
    private int mLayoutHeight;
    private int mFixHeight;
    private int mCriticalY;

    // 检测快速滑动
    private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityY) > FLING_VELOCITY) {
                if (velocityY > 0) {
                    mFlingDirection = DOWN_FLING;
                } else {
                    mFlingDirection = UP_FLING;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };


    public ScrollOverLayout(Context context) {
        this(context, null);
    }

    public ScrollOverLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScrollOverLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    private void _init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context, null, true);
        mGd = new GestureDetector(context, mGestureListener);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollOverLayout, 0, 0);
        mFixHeight = a.getDimensionPixelOffset(R.styleable.ScrollOverLayout_fix_height, mFixHeight);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLayoutHeight = getMeasuredHeight();
        // 计算最大子视图高度
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int maxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (maxHeight < child.getMeasuredHeight()) {
                maxHeight = child.getMeasuredHeight();
            }
        }
        // 调整布局高度
        if (mLayoutHeight > maxHeight) {
            mLayoutHeight = maxHeight;
        }
        if (mLayoutHeight < mFixHeight) {
            mLayoutHeight = mFixHeight;
        }
        setMeasuredDimension(getMeasuredWidth(), mLayoutHeight);
        // 滚动视图
        if (mLayoutHeight != mFixHeight) {
            mCriticalY = (mFixHeight - mLayoutHeight) / 2;
            scrollTo(0, mFixHeight - mLayoutHeight);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsIntercept = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mScrollStatus != STATUS_EXPANDED) {
                    mIsIntercept = true;
                } else {
                    mIsIntercept = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                mIsIntercept = false;
                break;
        }
        return mIsIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGd.onTouchEvent(event);
        int y = (int) event.getRawY();
        int scrollY = getScrollY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mOldY = y;
                Log.e("ScrollOverLayout", ""+(mFixHeight - mLayoutHeight));
                Log.w("ScrollOverLayout", ""+scrollY);
                return true;

            case MotionEvent.ACTION_MOVE:
                scrollY += mOldY - y;
                Log.w("ScrollOverLayout", ""+scrollY);
                if (scrollY < 0) {
                    scrollTo(0, scrollY);
                    mScrollStatus = STATUS_SCROLL;
                    mOldY = y;
                    return true;
                } else {
                    scrollTo(0, 0);
                    mScrollStatus = STATUS_EXPANDED;
                    Log.e("ScrollOverLayout", "STATUS_EXPANDED");
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mFlingDirection == NO_FLING) {
                    if (scrollY < mCriticalY) {
                        _smoothScrollTo(mFixHeight - mLayoutHeight);
                    } else {
                        _smoothScrollTo(0);
                    }
                } else if (mFlingDirection == UP_FLING) {
                    _fastScrollTo(0);
                } else if (mFlingDirection == DOWN_FLING) {
                    _fastScrollTo(mFixHeight - mLayoutHeight);
                }
                mFlingDirection = NO_FLING;
                break;
        }

        mOldY = y;
        return false;
    }

    /**
     * 滚动到指定位置
     * @param destY Y坐标
     */
    private void _smoothScrollTo(int destY) {
        int scrollY = getScrollY();
        int deltaY = destY - scrollY;
        mScroller.startScroll(0, scrollY, 0, deltaY, 400);
        invalidate();
    }

    /**
     * 快速滚动到指定位置
     * @param destY Y坐标
     */
    private void _fastScrollTo(int destY) {
        int scrollY = getScrollY();
        int deltaY = destY - scrollY;
        mScroller.startScroll(0, scrollY, 0, deltaY, 200);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else {
            if (getScrollY() == 0) {
                mScrollStatus = STATUS_EXPANDED;
            } else if (getScrollY() == (mFixHeight - mLayoutHeight)) {
                mScrollStatus = STATUS_COLLAPSED;
            } else if (getScrollY() == (-mLayoutHeight)) {
                mScrollStatus = STATUS_EXIT;
            }
        }
    }

    public int getScrollStatus() {
        return mScrollStatus;
    }

    public void setScrollStatus(int scrollStatus) {
        mScrollStatus = scrollStatus;
        switch (mScrollStatus) {
            case STATUS_EXPANDED:
                _fastScrollTo(0);
                break;
            case STATUS_COLLAPSED:
                _fastScrollTo(mFixHeight - mLayoutHeight);
                break;
            case STATUS_EXIT:
                _fastScrollTo(-mLayoutHeight);
                break;
        }
    }

    void setOldY(int oldY) {
        mOldY = oldY;
    }
}
