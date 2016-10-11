package com.dl7.myapp.module.welfarephoto;

import android.support.v7.widget.RecyclerView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.helperlibrary.listener.OnRequestDataListener;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.WelfarePhotoBean;
import com.dl7.myapp.injector.components.DaggerWelfarePhotoComponent;
import com.dl7.myapp.injector.modules.WelfarePhotoModule;
import com.dl7.myapp.module.base.BaseFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.base.ILoadDataView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by long on 2016/10/11.
 * 福利图片界面
 */
public class WelfarePhotoFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<WelfarePhotoBean>> {

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
        DaggerWelfarePhotoComponent.builder()
                .applicationComponent(getAppComponent())
                .welfarePhotoModule(new WelfarePhotoModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewSV(mContext, mRvPhotoList, slideAdapter, 2);
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
    public void loadData(List<WelfarePhotoBean> photoList) {
        mAdapter.updateItems(photoList);
    }

    @Override
    public void loadMoreData(List<WelfarePhotoBean> photoList) {
        mAdapter.loadComplete();
        mAdapter.addItems(photoList);
    }

    @Override
    public void loadNoData() {
        mAdapter.loadAbnormal();
    }
}