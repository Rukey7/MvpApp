package com.dl7.myapp.module.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.dl7.myapp.AndroidApplication;
import com.dl7.myapp.R;
import com.dl7.myapp.injector.components.ApplicationComponent;
import com.dl7.myapp.injector.modules.ActivityModule;
import com.dl7.myapp.utils.ActivityCollector;
import com.dl7.myapp.views.EmptyLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2016/8/19.
 * 基类Activity
 */
public abstract class BaseActivity<T extends IBasePresenter> extends AppCompatActivity implements IBaseView {

    /**
     * 把 EmptyLayout 放在基类统一处理，@Nullable 表明 View 可以为 null，详细可看 ButterKnife
     */
    @Nullable
    @BindView(R.id.empty_layout)
    protected EmptyLayout mEmptyLayout;
    /**
     * 把 Presenter 提取到基类需要配合基类的 initInjector() 进行注入，如果继承这个基类则必定要提供一个 Presenter 注入方法，
     * 该APP所有 Presenter 都是在 Module 提供注入实现，也可以选择提供另外不带 Presenter 的基类
     */
    @Inject
    protected T mPresenter;

    private long mExitTime = 0;

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

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
        initInjector();
        initViews();
        updateViews();
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void showLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
        }
    }

    @Override
    public void hideLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }

    @Override
    public void showNetError(final EmptyLayout.OnRetryListener onRetryListener) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
            mEmptyLayout.setRetryListener(onRetryListener);
        }
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
     * 设置状态是否透明，默认不透明
     */
    protected boolean isSystemBarTranslucent() {
        return false;
    }

    /**
     * 设置状态栏颜色
     */
    protected void _initSystemBarTint(boolean isSystemBarTranslucent) {
        if (isSystemBarTranslucent) {
            // 设置状态栏和导航栏全透明
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION );
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            }
            return;
        }
        // 状态栏设置颜色
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
        if (((Object) this).getClass().getName().contains("PhotosActivity")) {
            overridePendingTransition(0, 0);
        } else if (!((Object) this).getClass().getName().contains("MainActivity")) {
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

    @Override
    public void onBackPressed() {
        if (getClass().getName().contains("MainActivity")) {
            _exit();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 退出
     */
    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
        }
    }
}
