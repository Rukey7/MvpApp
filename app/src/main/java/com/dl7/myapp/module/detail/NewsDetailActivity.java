package com.dl7.myapp.module.detail;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewStub;
import android.widget.TextView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.NewsDetailBean;
import com.dl7.myapp.injector.components.DaggerNewsDetailComponent;
import com.dl7.myapp.injector.modules.NewsDetailModule;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.utils.ListUtils;
import com.dl7.myapp.utils.ToastUtils;
import com.dl7.myapp.views.EmptyLayout;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnURLClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class NewsDetailActivity extends BaseActivity implements INewsDetailView {

    private static final String APP_KEY = "AppKey";

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.fab_coping)
    FloatingActionButton mFabCoping;
    @BindView(R.id.tv_source)
    TextView mTvSource;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_topic_name)
    TextView mTvTopicName;
    @BindView(R.id.rv_related_news)
    RecyclerView mRvRelatedNews;

    @Inject
    IBasePresenter mPresenter;
    @Inject
    BaseQuickAdapter mRelatedAdapter;

    private String mNewsId;


    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(APP_KEY, newsId);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initViews() {
        mNewsId = getIntent().getStringExtra(APP_KEY);
        DaggerNewsDetailComponent.builder()
                .newsDetailModule(new NewsDetailModule(this, mNewsId))
                .build()
                .inject(this);
        initToolBar(mToolBar, true, "新闻详情");
        RecyclerViewHelper.initRecyclerViewV(this, mRvRelatedNews, false, mRelatedAdapter);
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
    public void loadData(NewsDetailBean newsDetailBean) {
        mTvTitle.setText(newsDetailBean.getTitle());
        mTvSource.setText(newsDetailBean.getSource());
        mTvTime.setText(newsDetailBean.getPtime());
        RichText.from(newsDetailBean.getBody())
                .urlClick(new OnURLClickListener() {
                    @Override
                    public boolean urlClicked(String url) {
                        return false;
                    }
                })
                .into(mTvContent);
        _handleSpInfo(newsDetailBean.getSpinfo());
        _handleRelatedNews(newsDetailBean);
    }

    /**
     * 处理关联的内容
     *
     * @param spinfo
     */
    private void _handleSpInfo(List<NewsDetailBean.SpinfoEntity> spinfo) {
        if (!ListUtils.isEmpty(spinfo)) {
            ViewStub stub = (ViewStub) findViewById(R.id.vs_related_content);
            assert stub != null;
            stub.inflate();
            TextView tvType = (TextView) findViewById(R.id.tv_type);
            TextView tvRelatedContent = (TextView) findViewById(R.id.tv_related_content);
            tvType.setText(spinfo.get(0).getSptype());
            RichText.from(spinfo.get(0).getSpcontent())
                    .urlClick(new OnURLClickListener() {
                        @Override
                        public boolean urlClicked(String url) {
                            ToastUtils.showToast(url);
                            return true;
                        }
                    })
                    .into(tvRelatedContent);
        }
    }

    /**
     * 处理关联新闻
     * @param newsDetailBean
     */
    private void _handleRelatedNews(NewsDetailBean newsDetailBean) {
        if (!ListUtils.isEmpty(newsDetailBean.getHuati())) {
            mTvTopicName.setText(newsDetailBean.getHuati().get(0).getTopicName());
        }
        if (!ListUtils.isEmpty(newsDetailBean.getRelative_sys())) {
            mRelatedAdapter.updateItems(newsDetailBean.getRelative_sys());
        }
    }
}
