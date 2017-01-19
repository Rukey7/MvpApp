package com.dl7.mvp.widget.swipeback;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;

import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.base.IBasePresenter;

/**
 * Created by long on 2017/1/19.
 * 滑动退出Activity，参考：https://github.com/ikew0ng/SwipeBackLayout
 */
public abstract class SwipeBackActivity<T extends IBasePresenter> extends BaseActivity<T> {

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ViewCompat.setBackground(getWindow().getDecorView(), null);
        mSwipeBackLayout = new SwipeBackLayout(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.attachToActivity(this, SwipeBackLayout.EDGE_LEFT);
//        mSwipeBackLayout.setEdgeSize(getResources().getDisplayMetrics().widthPixels / 2);
    }

    public void setEdgeFlag(@SwipeBackLayout.EdgeFlag int edgeFlag) {
        mSwipeBackLayout.setEdgeFlag(edgeFlag);
    }

    public void setScrollThreshold(float scrollThreshold) {
        mSwipeBackLayout.setScrollThreshold(scrollThreshold);
    }

    public void setEnableScroll(boolean enable) {
        mSwipeBackLayout.setEnableScroll(enable);
    }

    public void setShadowLeft(Drawable shadowLeft) {
        mSwipeBackLayout.setShadowLeft(shadowLeft);
    }

    public void setShadowRight(Drawable shadowRight) {
        mSwipeBackLayout.setShadowRight(shadowRight);
    }

    public void setShadowBottom(Drawable shadowBottom) {
        mSwipeBackLayout.setShadowBottom(shadowBottom);
    }

    public void setEdgeSize(int edgeSize) {
        mSwipeBackLayout.setEdgeSize(edgeSize);
    }

    public void setScrimColor(int scrimColor) {
        mSwipeBackLayout.setScrimColor(scrimColor);
    }
}
