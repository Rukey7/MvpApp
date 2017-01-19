package com.dl7.mvp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by long on 2016/12/19.
 * 扩展动态控制 ViewPager 滑动使能功能
 */
public class FlexibleViewPager extends ViewPager {

    private boolean mIsCanScroll = true;


    public FlexibleViewPager(Context context) {
        super(context);
    }

    public FlexibleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (mIsCanScroll) {
            super.scrollTo(x, y);
        }
    }

    public boolean isCanScroll() {
        return mIsCanScroll;
    }

    public void setCanScroll(boolean canScroll) {
        mIsCanScroll = canScroll;
    }
}
