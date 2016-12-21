package com.dl7.mvp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

/**
 * Created by long on 2016/12/21.
 * 带按压效果的 RelativeLayout
 */
public class PressRelativeLayout extends RelativeLayout {

    private GestureDetector gestureDetector;

    public PressRelativeLayout(Context context) {
        super(context);
    }

    public PressRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs);
    }

    public PressRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    private void _init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onShowPress(MotionEvent e) {
                switch (MotionEventCompat.getActionMasked(e)) {
                    case MotionEvent.ACTION_DOWN:
                        Logger.e("ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Logger.i("ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Logger.w("ACTION_UP");
                        break;
                    default:
                        Logger.d("" + MotionEventCompat.getActionMasked(e));
                        break;
                }
            }
        });
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        this.onTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }
}
