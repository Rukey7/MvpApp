package com.dl7.myapp.module.photolist;

import android.support.v7.widget.RecyclerView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.helperlibrary.listener.OnRequestDataListener;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.PhotoBean;
import com.dl7.myapp.injector.components.DaggerPhotoListComponent;
import com.dl7.myapp.injector.modules.PhotoListModule;
import com.dl7.myapp.module.base.BaseFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.views.EmptyLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by long on 2016/9/5.
 * 图片列表
 */
public class PhotoListFragment extends BaseFragment<IBasePresenter> implements IPhotoListView<PhotoBean> {

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;

    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initInjector() {
        DaggerPhotoListComponent.builder()
                .applicationComponent(getAppComponent())
                .photoListModule(new PhotoListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvPhotoList, new SlideInBottomAnimationAdapter(mAdapter));
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
    public void loadData(List<PhotoBean> photoList) {
        mAdapter.updateItems(photoList);
    }

    @Override
    public void loadMoreData(List<PhotoBean> photoList) {
        mAdapter.addItems(photoList);
    }

    @Override
    public void loadNoData() {
        mAdapter.noMoreData();
    }
}
