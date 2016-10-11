package com.dl7.myapp.module.base;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.dl7.myapp.R;
import com.dl7.myapp.module.home.MainActivity;
import com.dl7.myapp.module.photos.PhotosActivity;
import com.dl7.myapp.module.setting.SettingsActivity;
import com.dl7.myapp.module.videos.VideosActivity;

/**
 * Created by long on 2016/9/5.
 * 带导航的基类 Activity
 */
public abstract class BaseNavActivity<T extends IBasePresenter> extends BaseActivity<T>
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mAttachDrawerLayout;

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
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        mAttachDrawerLayout.closeDrawer(GravityCompat.START);
        if (item.isChecked()) {
            return true;
        }
        final Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_photos:
                intent = new Intent(this, PhotosActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                break;
            case R.id.nav_news:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                break;
            case R.id.nav_videos:
                intent = new Intent(this, VideosActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                break;
            case R.id.nav_setting:
                intent = new Intent(this, SettingsActivity.class);
                break;
            default:
                return false;
        }
        // 延迟是为了让抽屉菜单先收回去
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                if (item.getItemId() != R.id.nav_setting) {
                    // 这个要放 startActivity 后面
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.scale_out_activity);
                }
            }
        }, 250);
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
