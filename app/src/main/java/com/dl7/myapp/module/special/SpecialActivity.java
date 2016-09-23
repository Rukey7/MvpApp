package com.dl7.myapp.module.special;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.SpecialBean;
import com.dl7.myapp.entity.SpecialItem;
import com.dl7.myapp.injector.components.DaggerSpecialComponent;
import com.dl7.myapp.injector.modules.SpecialModule;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.utils.DefIconFactory;
import com.dl7.myapp.utils.ImageLoader;
import com.dl7.myapp.views.EmptyLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class SpecialActivity extends BaseActivity<IBasePresenter> implements ISpecialView {

    private static final String SPECIAL_KEY = "SpecialKey";

    @BindView(R.id.iv_banner)
    ImageView mIvBanner;
    @BindView(R.id.collapsing_tool_bar)
    CollapsingToolbarLayout mCollapsingToolBar;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.rv_news_list)
    RecyclerView mRvNewsList;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;

    @Inject
    BaseQuickAdapter mSpecialAdapter;

    private String mSpecialId;


    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, SpecialActivity.class);
        intent.putExtra(SPECIAL_KEY, newsId);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_special;
    }

    @Override
    protected void initInjector() {
        mSpecialId = getIntent().getStringExtra(SPECIAL_KEY);
        DaggerSpecialComponent.builder()
                .specialModule(new SpecialModule(this, mSpecialId))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "");
        mCollapsingToolBar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.expanded_title));
        mCollapsingToolBar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.collapsed_title));
        ScaleInAnimationAdapter animAdapter = new ScaleInAnimationAdapter(mSpecialAdapter);
        RecyclerViewHelper.initRecyclerViewV(this, mRvNewsList, true, new AlphaInAnimationAdapter(animAdapter));
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
    public void loadData(List<SpecialItem> specialItems) {
        mSpecialAdapter.updateItems(specialItems);
    }

    @Override
    public void loadBanner(SpecialBean specialBean) {
        // 设置标题
        mCollapsingToolBar.setTitle(specialBean.getSname());
        // 加载图片
        ImageLoader.loadFit(this, specialBean.getBanner(), mIvBanner, DefIconFactory.provideIcon());
        // 添加导语
        if (!TextUtils.isEmpty(specialBean.getDigest())) {
            View headView = LayoutInflater.from(this).inflate(R.layout.head_special, null);
            TextView tvDigest = (TextView) headView.findViewById(R.id.tv_digest);
            tvDigest.setText(specialBean.getDigest());
            mSpecialAdapter.addHeaderView(headView);
        }
    }

}
