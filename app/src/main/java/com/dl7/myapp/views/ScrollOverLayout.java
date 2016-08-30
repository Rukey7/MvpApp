package com.dl7.myapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.orhanobut.logger.Logger;

/**
 * Created by long on 2016/8/30.
 */
public class ScrollOverLayout extends FrameLayout {

    private Scroller mScroller;
    private GestureDetector mGd;
    private boolean mIsIntercept;
    private int mOldX;
    private int mOldY;

    private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Logger.i(e1.getY()+"");
            Logger.w(e2.getY()+"");
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
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        Log.i("LogTAG", "x = "+x);
        Log.i("LogTAG", "y = "+y);
        Log.i("LogTAG", "mOldX = "+mOldX);
        Log.i("LogTAG", "mOldY = "+mOldY);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsIntercept = true;
                mOldX = (int) ev.getX();
                mOldY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                mIsIntercept = true;
                break;

            case MotionEvent.ACTION_UP:
                mIsIntercept = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGd.onTouchEvent(event);
    }
}
