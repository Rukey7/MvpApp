package com.dl7.myapp.module.base;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.dl7.myapp.R;
import com.dl7.myapp.module.home.MainActivity;
import com.dl7.myapp.module.photos.PhotosActivity;

/**
 * Created by long on 2016/9/5.
 * 带导航的基类 Activity
 */
public abstract class BaseNavActivity<T extends IBasePresenter> extends BaseActivity<T>
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mAttachDrawerLayout;
    private Class mClass;

    /**
     * 初始化 DrawerLayout
     * 子类要调用这个才能正确初始化抽屉菜单
     *
     * @param drawerLayout DrawerLayout
     * @param navView      NavigationView
     * @param toolbar      Toolbar
     */
    protected void initDrawerLayout(DrawerLayout drawerLayout, NavigationView navView, Toolbar toolbar) {
        mAttachDrawerLayout = drawerLayout;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //将侧边栏顶部延伸至status bar
            drawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            drawerLayout.setClipToPadding(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        mAttachDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (mClass != null) {
                    Intent intent = new Intent(BaseNavActivity.this, mClass);
                    // 此标志用于启动一个Activity的时候，若栈中存在此Activity实例，则把它调到栈顶。不创建多一个
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    BaseNavActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    mClass = null;
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mAttachDrawerLayout.closeDrawer(GravityCompat.START);
        if (item.isChecked()) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.nav_photos:
                mClass = PhotosActivity.class;
                break;
            case R.id.nav_news:
                mClass = MainActivity.class;
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mAttachDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mAttachDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        overridePendingTransition(0, 0);
    }
}
