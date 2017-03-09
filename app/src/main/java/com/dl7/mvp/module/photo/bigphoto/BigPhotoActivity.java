package com.dl7.mvp.module.photo.bigphoto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dl7.drag.DragSlopLayout;
import com.dl7.mvp.R;
import com.dl7.mvp.adapter.PhotoPagerAdapter;
import com.dl7.mvp.injector.components.DaggerBigPhotoComponent;
import com.dl7.mvp.injector.modules.BigPhotoModule;
import com.dl7.mvp.local.table.BeautyPhotoInfo;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.base.ILoadDataView;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.utils.AnimateHelper;
import com.dl7.mvp.utils.CommonConstant;
import com.dl7.mvp.utils.DownloadUtils;
import com.dl7.mvp.utils.NavUtils;
import com.dl7.mvp.utils.SnackbarUtils;
import com.dl7.mvp.utils.ToastUtils;
import com.dl7.mvp.widget.PhotoViewPager;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * 大图界面，这里和比较多地方关联，所以逻辑会多一点
 */
public class BigPhotoActivity extends BaseActivity<ILocalPresenter> implements ILoadDataView<List<BeautyPhotoInfo>> {

    private static final String BIG_PHOTO_KEY = "BigPhotoKey";
    private static final String PHOTO_INDEX_KEY = "PhotoIndexKey";
    private static final String FROM_LOVE_ACTIVITY = "FromLoveActivity";

    @BindView(R.id.vp_photo)
    PhotoViewPager mVpPhoto;
    @BindView(R.id.iv_favorite)
    ImageView mIvFavorite;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.iv_praise)
    ImageView mIvPraise;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;
    @BindView(R.id.ll_layout)
    LinearLayout mLlLayout;

    @Inject
    PhotoPagerAdapter mAdapter;
    private List<BeautyPhotoInfo> mPhotoList;
    private int mIndex; // 初始索引
    private boolean mIsFromLoveActivity;    // 是否从 LoveActivity 启动进来
    private boolean mIsHideToolbar = false; // 是否隐藏 Toolbar
    private boolean mIsInteract = false;    // 是否和 ViewPager 联动
    private int mCurPosition;   // Adapter 当前位置
    private boolean[] mIsDelLove;   // 保存被删除的收藏项
    private RxPermissions mRxPermissions;

    public static void launch(Context context, ArrayList<BeautyPhotoInfo> datas, int index) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.putParcelableArrayListExtra(BIG_PHOTO_KEY, datas);
        intent.putExtra(PHOTO_INDEX_KEY, index);
        intent.putExtra(FROM_LOVE_ACTIVITY, false);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }

    // 这个给 LoveActivity 使用，配合 setResult() 返回取消的收藏，这样做体验会好点，其实用 RxBus 会更容易做
    public static void launchForResult(Fragment fragment, ArrayList<BeautyPhotoInfo> datas, int index) {
        Intent intent = new Intent(fragment.getContext(), BigPhotoActivity.class);
        intent.putParcelableArrayListExtra(BIG_PHOTO_KEY, datas);
        intent.putExtra(PHOTO_INDEX_KEY, index);
        intent.putExtra(FROM_LOVE_ACTIVITY, true);
        fragment.startActivityForResult(intent, CommonConstant.REQUEST_CODE);
        fragment.getActivity().overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_big_photo;
    }

    @Override
    protected void initInjector() {
        mPhotoList = getIntent().getParcelableArrayListExtra(BIG_PHOTO_KEY);
        mIndex = getIntent().getIntExtra(PHOTO_INDEX_KEY, 0);
        mIsFromLoveActivity = getIntent().getBooleanExtra(FROM_LOVE_ACTIVITY, false);
        DaggerBigPhotoComponent.builder()
                .applicationComponent(getAppComponent())
                .bigPhotoModule(new BigPhotoModule(this, mPhotoList))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, true, "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 空出底部导航的高度，因为 NavigationBar 是透明的
            mLlLayout.setPadding(0, 0, 0, NavUtils.getNavigationBarHeight(this));
        }
//        mAdapter = new PhotoPagerAdapter(this);
        mVpPhoto.setAdapter(mAdapter);
        // 设置是否 ViewPager 联动和动画
        mDragLayout.interactWithViewPager(mIsInteract);
        mDragLayout.setAnimatorMode(DragSlopLayout.FLIP_Y);
        mAdapter.setTapListener(new PhotoPagerAdapter.OnTapListener() {
            @Override
            public void onPhotoClick() {
                mIsHideToolbar = !mIsHideToolbar;
                if (mIsHideToolbar) {
                    mDragLayout.startOutAnim();
                    mToolbar.animate().translationY(-mToolbar.getBottom()).setDuration(300);
                } else {
                    mDragLayout.startInAnim();
                    mToolbar.animate().translationY(0).setDuration(300);
                }
            }
        });
        if (!mIsFromLoveActivity) {
            mAdapter.setLoadMoreListener(new PhotoPagerAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMoreData();
                }
            });
        } else {
            // 收藏界面不需要加载更多
            mIsDelLove = new boolean[mPhotoList.size()];
        }
        mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurPosition = position;
                // 设置图标状态
                mIvFavorite.setSelected(mAdapter.isLoved(position));
                mIvDownload.setSelected(mAdapter.isDownload(position));
                mIvPraise.setSelected(mAdapter.isPraise(position));
            }
        });
        mRxPermissions = new RxPermissions(this);
        RxView.clicks(mIvDownload)
                .compose(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            DownloadUtils.downloadOrDeletePhoto(BigPhotoActivity.this, mAdapter.getData(mCurPosition).getImgsrc(),
                                    mAdapter.getData(mCurPosition).getDocid(), new DownloadUtils.OnCompletedListener() {
                                        @Override
                                        public void onCompleted(String url) {
                                            mAdapter.getData(url).setDownload(true);
                                            mIvDownload.setSelected(true);
                                            mPresenter.insert(mAdapter.getData(url));
                                        }

                                        @Override
                                        public void onDeleted(String url) {
                                            mAdapter.getData(url).setDownload(false);
                                            mIvDownload.setSelected(false);
                                            mPresenter.delete(mAdapter.getData(url));
                                        }
                                    });
                        } else {
                            SnackbarUtils.showSnackbar(BigPhotoActivity.this, "权限授权失败", false);
                        }
                    }
                });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<BeautyPhotoInfo> data) {
        mAdapter.updateData(data);
        mVpPhoto.setCurrentItem(mIndex);
        if (mIndex == 0) {
            // 为 0 不会回调 addOnPageChangeListener，所以这里要处理下
            mIvFavorite.setSelected(mAdapter.isLoved(0));
            mIvDownload.setSelected(mAdapter.isDownload(0));
            mIvPraise.setSelected(mAdapter.isPraise(0));
        }
    }

    @Override
    public void loadMoreData(List<BeautyPhotoInfo> data) {
        mAdapter.addData(data);
        mAdapter.startUpdate(mVpPhoto);
    }

    @Override
    public void loadNoData() {
    }

    @OnClick({R.id.iv_favorite, R.id.iv_praise, R.id.iv_share})
    public void onClick(final View view) {
        final boolean isSelected = !view.isSelected();
        switch (view.getId()) {
            case R.id.iv_favorite:
                mAdapter.getData(mCurPosition).setLove(isSelected);
                break;
            case R.id.iv_praise:
                mAdapter.getData(mCurPosition).setPraise(isSelected);
                break;
            case R.id.iv_share:
                ToastUtils.showToast("分享:功能没加(╯-╰)");
                break;
        }
        // 除分享外都做动画和数据库处理
        if (view.getId() != R.id.iv_share) {
            view.setSelected(isSelected);
            AnimateHelper.doHeartBeat(view, 500);
            if (isSelected) {
                mPresenter.insert(mAdapter.getData(mCurPosition));
            } else {
                mPresenter.delete(mAdapter.getData(mCurPosition));
            }
        }
        if (mIsFromLoveActivity && view.getId() == R.id.iv_favorite) {
            // 不选中即去除收藏
            mIsDelLove[mCurPosition] = !isSelected;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_animate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.slide_bottom:
                mDragLayout.setAnimatorMode(DragSlopLayout.SLIDE_BOTTOM);
                return true;
            case R.id.slide_left:
                mDragLayout.setAnimatorMode(DragSlopLayout.SLIDE_LEFT);
                return true;
            case R.id.slide_right:
                mDragLayout.setAnimatorMode(DragSlopLayout.SLIDE_RIGHT);
                return true;
            case R.id.slide_fade:
                mDragLayout.setAnimatorMode(DragSlopLayout.FADE);
                return true;
            case R.id.slide_flip_x:
                mDragLayout.setAnimatorMode(DragSlopLayout.FLIP_X);
                return true;
            case R.id.slide_flip_y:
                mDragLayout.setAnimatorMode(DragSlopLayout.FLIP_Y);
                return true;
            case R.id.slide_zoom:
                mDragLayout.setAnimatorMode(DragSlopLayout.ZOOM);
                return true;
            case R.id.slide_zoom_left:
                mDragLayout.setAnimatorMode(DragSlopLayout.ZOOM_LEFT);
                return true;
            case R.id.slide_zoom_right:
                mDragLayout.setAnimatorMode(DragSlopLayout.ZOOM_RIGHT);
                return true;

            case R.id.item_interact:
                mIsInteract = !mIsInteract;
                item.setChecked(mIsInteract);
                mDragLayout.interactWithViewPager(mIsInteract);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        if (mIsFromLoveActivity) {
            Intent intent = new Intent();
            intent.putExtra(CommonConstant.RESULT_KEY, mIsDelLove);
            // 把数据传给 LoveActivity
            setResult(RESULT_OK, intent);
        }
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_out_exit);
    }
}
