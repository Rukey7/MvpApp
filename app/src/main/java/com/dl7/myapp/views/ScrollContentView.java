package com.dl7.myapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ScrollView;

/**
 * Created by long on 2016/8/30.
 * 配合 ScrollOverLayout 使用的 ScrollView
 */
public class ScrollContentView extends ScrollView {

    private ViewParent mParentView;
    private int mOldY;
    private boolean mIsSetParentOldY = false;

    public ScrollContentView(Context context) {
        super(context);
    }

    public ScrollContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        boolean isIllegal = true;
        mParentView = getParent();
        while (mParentView != null) {
            if (mParentView instanceof ScrollOverLayout) {
                isIllegal = false;
                break;
            }
            mParentView = mParentView.getParent();
        }
        if (mParentView == null || isIllegal) {
            throw new IllegalAccessError("ScrollContentView must be contained in ScrollOverLayout!");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (((ScrollOverLayout) mParentView).getScrollStatus() != ScrollOverLayout.STATUS_EXPANDED
                && !mIsSetParentOldY) {
            return false;
        }
        int y = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() == 0 && mOldY == 0) {
                    mOldY = y;
                }
                Log.i("ScrollContentView", "" + getScrollY());
                if (getScrollY() == 0 && (y > mOldY)) {
                    if (!mIsSetParentOldY) {
                        Log.e("ScrollContentView", "mIsSetParentOldY");
                        mIsSetParentOldY = true;
                        ((ScrollOverLayout) mParentView).setOldY(mOldY);
                    }
                    ((ScrollOverLayout) mParentView).onTouchEvent(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
//                if (getScrollY() == 0) {
                ((ScrollOverLayout) mParentView).onTouchEvent(ev);
//                }
            default:
                mOldY = 0;
                mIsSetParentOldY = false;
                break;
        }
        return super.onTouchEvent(ev);
    }
}
