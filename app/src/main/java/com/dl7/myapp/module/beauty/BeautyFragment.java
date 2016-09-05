package com.dl7.myapp.module.beauty;

import android.support.v7.widget.RecyclerView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.helperlibrary.listener.OnRequestDataListener;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.BeautyPhotoBean;
import com.dl7.myapp.injector.components.DaggerBeautyComponent;
import com.dl7.myapp.injector.modules.BeautyModule;
import com.dl7.myapp.module.base.BaseFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.photolist.IPhotoListView;
import com.dl7.myapp.views.EmptyLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by long on 2016/9/5.
 * 美女图片
 */
public class BeautyFragment extends BaseFragment implements IPhotoListView<BeautyPhotoBean> {

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;

    @Inject
    IBasePresenter mPresenter;
    @Inject
    BaseQuickAdapter mAdapter;


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initViews() {
        DaggerBeautyComponent.builder()
                .applicationComponent(getAppComponent())
                .beautyModule(new BeautyModule(this))
                .build()
                .inject(this);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvPhotoList, slideAdapter);
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getMoreData();
            }
        });
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void showLoading() {
        mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
    }

    @Override
    public void hideLoading() {
        mEmptyLayout.hide();
    }

    @Override
    public void showNetError(final EmptyLayout.OnRetryListener onRetryListener) {
        mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
        mEmptyLayout.setRetryListener(onRetryListener);
    }

    @Override
    public void loadData(List<BeautyPhotoBean> photoList) {
        mAdapter.updateItems(photoList);
    }

    @Override
    public void loadMoreData(List<BeautyPhotoBean> photoList) {
        mAdapter.addItems(photoList);
    }

    @Override
    public void loadNoData() {
        mAdapter.noMoreData();
    }
}
