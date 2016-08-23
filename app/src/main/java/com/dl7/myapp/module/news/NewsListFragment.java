package com.dl7.myapp.module.news;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.myapp.R;
import com.dl7.myapp.adapter.NewsListAdapter;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.injector.components.DaggerNewsListComponent;
import com.dl7.myapp.injector.modules.NewsListModule;
import com.dl7.myapp.module.base.BaseFragment;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.view.EmptyLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by long on 2016/8/23.
 * 新闻列表
 */
public class NewsListFragment extends BaseFragment implements INewsListView {

    public static final String FRAG_KEY = "FragKey";

    @BindView(R.id.rv_news_list)
    RecyclerView mRvNewsList;
//    @BindView(R.id.empty_layout)
//    EmptyLayout mEmptyLayout;

    NewsListAdapter mAdapter;
    @Inject
    IBasePresenter mPresenter;


    public static NewsListFragment newInstance(String title) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAG_KEY, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initViews() {
        DaggerNewsListComponent.builder()
                .applicationComponent(getAppComponent())
                .newsListModule(new NewsListModule(this))
                .build()
                .inject(this);
        mAdapter = new NewsListAdapter(getContext());
        RecyclerViewHelper.initRecyclerViewV(getContext(), mRvNewsList, true, mAdapter);
    }

    @Override
    protected void updateViews() {
        mPresenter.updateViews();
    }

    @Override
    public void showLoading() {
//        mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
    }

    @Override
    public void hideLoading() {
//        mEmptyLayout.hide();
    }

    @Override
    public void showNetError(final EmptyLayout.OnRetryListener onRetryListener) {
//        mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
//        mEmptyLayout.setRetryListener(onRetryListener);
    }

    @Override
    public void updateList(List<NewsBean> newsList) {
        mAdapter.updateItems(newsList);
    }
}
