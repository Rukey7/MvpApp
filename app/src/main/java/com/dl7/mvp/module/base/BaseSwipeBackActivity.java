package com.dl7.mvp.module.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.dl7.mvp.widget.SwipeBackLayout;

/**
 * Created by long on 2017/1/19.
 * 滑动退出Activity，参考：https://github.com/ikew0ng/SwipeBackLayout
 */
public abstract class BaseSwipeBackActivity<T extends IBasePresenter> extends BaseActivity<T> {

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSwipeBackLayout = new SwipeBackLayout(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.attachToActivity(this, SwipeBackLayout.EDGE_LEFT);
        // 触摸边缘变为屏幕宽度的1/2
        mSwipeBackLayout.setEdgeSize(getResources().getDisplayMetrics().widthPixels / 2);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
