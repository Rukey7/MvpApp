package com.dl7.mvp.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dl7.mvp.R;
import com.dl7.mvp.utils.AnimateHelper;


/**
 * Created by long on 2017/1/12.
 * 可滚动超出上拉的 ScrollView
 */
public class PullScrollView extends NestedScrollView {

    private View mFootView;
    private OnPullListener mPullListener;
    private boolean mIsPullStatus = false;
    private float mLastY;
    private int mPullCriticalDistance;

    public PullScrollView(Context context) {
        this(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPullCriticalDistance = getResources().getDimensionPixelSize(R.dimen.pull_critical_distance);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t >= (getChildAt(0).getMeasuredHeight() - getHeight()) && mPullListener != null) {
            mPullListener.isDoPull();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_MOVE:
                if (!mIsPullStatus) {
                    if (getScrollY() >= (getChildAt(0).getMeasuredHeight() - getHeight()) || getChildAt(0).getMeasuredHeight() < getHeight()) {
                        if (mPullListener != null && mPullListener.isDoPull()) {
                            mIsPullStatus = true;
                            mLastY = ev.getY();
                        }
                    }
                } else if (mLastY < ev.getY()) {
                    mIsPullStatus = false;
                    _pullFootView(0);
                } else {
                    float offsetY = mLastY - ev.getY();
                    _pullFootView(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsPullStatus) {
                    if (mFootView.getHeight() > mPullCriticalDistance && mPullListener != null) {
                        if (!mPullListener.handlePull()) {
                            AnimateHelper.doClipViewHeight(mFootView, mFootView.getHeight(), 0, 200);
                        }
                    } else {
                        AnimateHelper.doClipViewHeight(mFootView, mFootView.getHeight(), 0, 200);
                    }
                    mIsPullStatus = false;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void setFootView(View footView) {
        mFootView = footView;
    }

    public void setPullListener(OnPullListener pullListener) {
        mPullListener = pullListener;
    }

    private void _pullFootView(float offsetY) {
        if (mFootView != null) {
            ViewGroup.LayoutParams layoutParams = mFootView.getLayoutParams();
            layoutParams.height = (int) (offsetY * 1 / 2);
            mFootView.setLayoutParams(layoutParams);
        }
    }

    public interface OnPullListener {
        boolean isDoPull();
        boolean handlePull();
    }
}
