package com.dl7.myapp.module.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dl7.myapp.AndroidApplication;
import com.dl7.myapp.R;
import com.dl7.myapp.injector.components.ApplicationComponent;
import com.dl7.myapp.injector.modules.ActivityModule;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * Created by long on 2016/8/19.
 * 基类Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    protected abstract int attachLayoutRes();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    /**
     * 更新视图控件
     */
    protected abstract void updateViews();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        _initSystemBarTint(isSystemBarTranslucent());
        ButterKnife.bind(this);
        initViews();
        updateViews();
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId 视图ID
     * @param fragment        Fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }

    /**
     * 获取 ApplicationComponent
     *
     * @return ApplicationComponent
     */
    protected ApplicationComponent getAppComponent() {
        return ((AndroidApplication) getApplication()).getAppComponent();
    }

    /**
     * 获取 ActivityModule
     *
     * @return ActivityModule
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


    /**
     * 设置状态是否透明
     */
    protected boolean isSystemBarTranslucent() {
        return false;
    }

    /**
     * 设置状态栏颜色
     */
    protected void _initSystemBarTint(boolean isSystemBarTranslucent) {
        if (isSystemBarTranslucent) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getDarkColorPrimary());
        }
    }

    /**
     * 获取深主题色
     *
     * @return
     */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    /**************************************************************************/

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (intent != null && intent.getComponent() != null
                && !intent.getComponent().getClassName().contains("MainActivity")) {
            // 设置Activity进入动画为从右往左覆盖
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.hold_long);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (intent != null && intent.getComponent() != null) {
            // 设置Activity进入动画为从右往左覆盖
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.hold_long);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (!((Object) this).getClass().getName().contains("MainActivity")) {
            // 设置Activity退出动画为从左往右退出
            overridePendingTransition(R.anim.hold_long, R.anim.move_right_out_activity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
