package com.dl7.mvp.module.photo.beauty;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.SlideInBottomAdapter;
import com.dl7.mvp.injector.components.DaggerBeautyListComponent;
import com.dl7.mvp.injector.modules.BeautyListModule;
import com.dl7.mvp.local.table.BeautyPhotoInfo;
import com.dl7.mvp.module.base.BaseFragment;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.module.base.ILoadDataView;
import com.dl7.mvp.utils.CommonConstant;
import com.dl7.mvp.utils.MeasureUtils;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.dl7.recycler.listener.OnRequestDataListener;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2016/9/5.
 * 美女图片
 */
public class BeautyListFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<BeautyPhotoInfo>> {

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.iv_transition_photo)
    ImageView mIvTransitionPhoto;

    @Inject
    BaseQuickAdapter mAdapter;
    private int mCenterX = CommonConstant.INVALID_INTEGER;
    private int mCenterY = CommonConstant.INVALID_INTEGER;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initInjector() {
        DaggerBeautyListComponent.builder()
                .applicationComponent(getAppComponent())
                .beautyListModule(new BeautyListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        SlideInBottomAdapter slideAdapter = new SlideInBottomAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewSV(mContext, mRvPhotoList, slideAdapter, 2);
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getMoreData();
            }
        });
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView itemPhoto = (ImageView) view.findViewById(R.id.iv_photo);
                int[] location = MeasureUtils.getViewLocation(itemPhoto);
                int[] location2 = MeasureUtils.getViewLocation(mRvPhotoList);
                Logger.e(location2[0] + " - " + location2[1]);
                DisplayMetrics displayMetrics = MeasureUtils.getDisplayMetrics(mContext);
                if (mCenterX == CommonConstant.INVALID_INTEGER && mCenterY == CommonConstant.INVALID_INTEGER ) {
                    Rect rect = new Rect();
                    mIvTransitionPhoto.getGlobalVisibleRect(rect);
                    mCenterX = rect.centerX();
                    mCenterY = rect.centerY();
                }
                final ViewGroup.LayoutParams params = mIvTransitionPhoto.getLayoutParams();
                params.width = itemPhoto.getWidth();
                params.height = itemPhoto.getHeight();
                mIvTransitionPhoto.setImageDrawable(itemPhoto.getDrawable());
                int finalY = mCenterY - params.height / 2;
                int finalX = mCenterX - params.width / 2;
                ViewCompat.setTranslationY(mIvTransitionPhoto, location[1] - finalY);
                mIvTransitionPhoto.setVisibility(View.VISIBLE);
//                ViewCompat.animate(mIvTransitionPhoto).scaleY(2.0f).start();
//                BigPhotoActivity.launch(mContext, (ArrayList<BeautyPhotoInfo>) mAdapter.getData(), position);
            }
        });
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void loadData(List<BeautyPhotoInfo> photoList) {
        mAdapter.updateItems(photoList);
    }

    @Override
    public void loadMoreData(List<BeautyPhotoInfo> photoList) {
        mAdapter.loadComplete();
        mAdapter.addItems(photoList);
    }

    @Override
    public void loadNoData() {
        mAdapter.loadAbnormal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
