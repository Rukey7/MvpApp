package com.dl7.myapp.module.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.helperlibrary.listener.OnRequestDataListener;
import com.dl7.myapp.R;
import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.entity.NewsMultiItem;
import com.dl7.myapp.injector.components.DaggerNewsListComponent;
import com.dl7.myapp.injector.modules.NewsListModule;
import com.dl7.myapp.module.base.BaseFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.utils.SliderHelper;
import com.dl7.myapp.view.EmptyLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

/**
 * Created by long on 2016/8/23.
 * 新闻列表
 */
public class NewsListFragment extends BaseFragment implements INewsListView {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";

    @BindView(R.id.rv_news_list)
    RecyclerView mRvNewsList;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;

    @Inject
    BaseQuickAdapter mAdapter;
    @Inject
    IBasePresenter mPresenter;
    private SliderLayout mAdSlider;

    private int mNewsType;


    public static NewsListFragment newInstance(@RetrofitService.NewsType int newsType) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(NEWS_TYPE_KEY, newsType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsType = getArguments().getInt(NEWS_TYPE_KEY, -1);
        }
    }

    @Override
    public void onResume() {
        if (mAdSlider != null) {
            mAdSlider.startAutoCycle();
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        if (mAdSlider != null) {
            mAdSlider.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initViews() {
        DaggerNewsListComponent.builder()
                .applicationComponent(getAppComponent())
                .newsListModule(new NewsListModule(this, mNewsType))
                .build()
                .inject(this);
//        RecyclerViewHelper.initRecyclerViewV(getContext(), mRvNewsList, true, mAdapter, new OnRequestDataListener() {
//            @Override
//            public void onLoadMore() {
//                mPresenter.loadMoreData();
//            }
//        });
        SlideInRightAnimationAdapter animAdapter = new SlideInRightAnimationAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvNewsList, true, new AlphaInAnimationAdapter(animAdapter));
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData();
            }
        });
    }

    @Override
    protected void updateViews() {
        mPresenter.loadData();
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
    public void loadData(List<NewsMultiItem> newsList) {
        mAdapter.updateItems(newsList);
    }

    @Override
    public void loadMoreData(List<NewsMultiItem> newsList) {
        mAdapter.loadComplete();
        mAdapter.addItems(newsList);
    }

    @Override
    public void loadNoData() {
        mAdapter.noMoreData();
    }

    @Override
    public void loadAdData(NewsBean newsBean) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.head_news_list, null);
        mAdSlider = (SliderLayout) view.findViewById(R.id.slider_ads);
        SliderHelper.initAdSlider(mContext, mAdSlider, newsBean);
        mAdapter.addHeaderView(view);
    }
}
