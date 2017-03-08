package com.dl7.mvp.module.manage.love;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.ViewPagerAdapter;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.manage.love.photo.LovePhotoFragment;
import com.dl7.mvp.module.manage.love.video.LoveVideoFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;

import static com.dl7.mvp.utils.CommonConstant.INDEX_KEY;

/**
 * 收藏界面
 */
public class LoveActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    ViewPagerAdapter mPagerAdapter;
    private int mIndex;

    public static void launch(Context context, int index) {
        Intent intent = new Intent(context, LoveActivity.class);
        intent.putExtra(INDEX_KEY, index);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.zoom_out_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_love;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initViews() {
        mIndex = getIntent().getIntExtra(INDEX_KEY, 0);
        initToolBar(mToolBar, true, "收藏");
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LovePhotoFragment());
        fragments.add(new LoveVideoFragment());
        mTabLayout.setViewPager(mViewPager, new String[] {"图片", "视频"}, this, fragments);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setCurrentItem(mIndex);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_out_exit);
    }
}
