package com.dl7.mvp.module.home;

import android.Manifest;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dl7.downloaderlib.FileDownloader;
import com.dl7.mvp.R;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.manage.setting.SettingsActivity;
import com.dl7.mvp.module.news.main.NewsMainFragment;
import com.dl7.mvp.module.photo.main.PhotoMainFragment;
import com.dl7.mvp.module.video.main.VideoMainFragment;
import com.dl7.mvp.utils.SnackbarUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * 程序入口
 */
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private RxPermissions mRxPermissions;

    // 本来想用这个来存储Fragment做切换，不过貌似fragment会被回收产生异常，估计内存占用太大
//    private SparseArray<Fragment> mSparseFragments = new SparseArray<>();
    private SparseArray<String> mSparseTags = new SparseArray<>();
    private long mExitTime = 0;
    private int mItemId = -1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.nav_news:
                    replaceFragment(R.id.fl_container, new NewsMainFragment(), mSparseTags.get(R.id.nav_news));
                    break;
                case R.id.nav_photos:
                    replaceFragment(R.id.fl_container, new PhotoMainFragment(), mSparseTags.get(R.id.nav_photos));
                    break;
                case R.id.nav_videos:
                    replaceFragment(R.id.fl_container, new VideoMainFragment(), mSparseTags.get(R.id.nav_videos));
                    break;
                case R.id.nav_setting:
                    SettingsActivity.launch(HomeActivity.this);
                    break;
            }
            mItemId = -1;
            return true;
        }
    });

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initViews() {
        _initDrawerLayout(mDrawerLayout, mNavView);
        mSparseTags.put(R.id.nav_news, "News");
        mSparseTags.put(R.id.nav_photos, "Photos");
        mSparseTags.put(R.id.nav_videos, "Videos");
        _getPemission();
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mNavView.setCheckedItem(R.id.nav_news);
        addFragment(R.id.fl_container, new NewsMainFragment(), "News");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (item.isChecked()) {
            return true;
        }
        mItemId = item.getItemId();
        return true;
    }

    @Override
    public void onBackPressed() {
        // 获取堆栈里有几个
        final int stackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (stackEntryCount == 1) {
            // 如果剩一个说明在主页，提示按两次退出app
            _exit();
        } else {
            // 获取上一个堆栈中保存的是哪个页面，根据name来设置导航项的选中状态
            final String tagName = getSupportFragmentManager().getBackStackEntryAt(stackEntryCount - 2).getName();
            mNavView.setCheckedItem(mSparseTags.keyAt(mSparseTags.indexOfValue(tagName)));
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化 DrawerLayout
     *
     * @param drawerLayout DrawerLayout
     * @param navView      NavigationView
     */
    private void _initDrawerLayout(DrawerLayout drawerLayout, NavigationView navView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //将侧边栏顶部延伸至status bar
            drawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            drawerLayout.setClipToPadding(false);
        }
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mHandler.sendEmptyMessage(mItemId);
            }
        });
        navView.setNavigationItemSelectedListener(this);
    }

    private void _getPemission() {
        final File dir = new File(FileDownloader.getDownloadDir());
        if (!dir.exists() || !dir.isDirectory()) {
            dir.delete();
            new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            if (granted) {
                                dir.mkdirs();
                            } else {
                                SnackbarUtils.showSnackbar(HomeActivity.this, "下载目录创建失败", true);
                            }
                        }
                    });
        }
    }


    /**
     * 退出
     */
    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
