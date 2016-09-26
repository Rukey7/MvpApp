package com.dl7.myapp.module.photonews;

import android.support.v7.widget.RecyclerView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.helperlibrary.listener.OnRequestDataListener;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.PhotoBean;
import com.dl7.myapp.injector.components.DaggerPhotoNewsComponent;
import com.dl7.myapp.injector.modules.PhotoNewsModule;
import com.dl7.myapp.module.base.BaseFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.base.ILoadDataView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by long on 2016/9/5.
 * 图片新闻列表
 */
public class PhotoNewsFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<PhotoBean>> {

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;

    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_news;
    }

    @Override
    protected void initInjector() {
        DaggerPhotoNewsComponent.builder()
                .applicationComponent(getAppComponent())
                .photoNewsModule(new PhotoNewsModule(this))
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
