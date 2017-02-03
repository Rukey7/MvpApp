package com.dl7.mvp.module.news.article;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dl7.downloaderlib.model.PreferencesUtils;
import com.dl7.mvp.R;
import com.dl7.mvp.api.bean.NewsDetailInfo;
import com.dl7.mvp.api.bean.NewsInfo;
import com.dl7.mvp.injector.components.DaggerNewsArticleComponent;
import com.dl7.mvp.injector.modules.NewsArticleModule;
import com.dl7.mvp.module.base.BaseSwipeBackActivity;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.utils.AnimateHelper;
import com.dl7.mvp.utils.ListUtils;
import com.dl7.mvp.widget.CustomWebView;
import com.dl7.mvp.widget.EmptyLayout;
import com.dl7.mvp.widget.PullScrollView;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideEnter.SlideLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.animation.SlideExit.SlideTopExit;
import com.flyco.dialog.widget.popup.BubblePopup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsArticleActivity extends BaseSwipeBackActivity<IBasePresenter> implements INewsArticleView {

    private static final String SHOW_POPUP_DETAIL = "ShowPopupDetail";
    private static final String NEWS_LIST_KEY = "NewsListKey";
    private static final String NEWS_POSITION = "NewsPos";

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_pre_toolbar)
    LinearLayout mLlPreToolbar;
    @BindView(R.id.web_view)
    CustomWebView mWebView;
    @BindView(R.id.tv_next_title)
    TextView mTvNextTitle;
    @BindView(R.id.ll_foot_view)
    LinearLayout mLlFootView;
    @BindView(R.id.scroll_view)
    PullScrollView mScrollView;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;
    @BindView(R.id.iv_back_2)
    ImageView mIvBack2;
    @BindView(R.id.tv_title_2)
    TextView mTvTitle2;
    @BindView(R.id.ll_top_bar)
    LinearLayout mLlTopBar;

    @Inject
    BaseQuickAdapter mRelatedAdapter;

    private ArrayList<NewsInfo> mNewsList;
    private int mCurIndex;
    private int mToolbarHeight;
    private int mTopBarHeight;
    private Animator mTopBarAnimator;
    private int mLastScrollY = 0;
    private int mMinScrollSlop;


    public static void launch(Context context, ArrayList<NewsInfo> newsList, int position) {
        Intent intent = new Intent(context, NewsArticleActivity.class);
        intent.putParcelableArrayListExtra(NEWS_LIST_KEY, newsList);
        intent.putExtra(NEWS_POSITION, position);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    private void launchInside(ArrayList<NewsInfo> newsList, int position) {
//        DbHelper.addNews(newsInfo.get(position));
//        EventBus.getDefault().post(new NewsEvent(newsInfo.get(position).getId(), position));
        Intent intent = new Intent(this, NewsArticleActivity.class);
        intent.putParcelableArrayListExtra(NEWS_LIST_KEY, newsList);
        intent.putExtra(NEWS_POSITION, position);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_news_article;
    }

    @Override
    protected void initInjector() {
        mNewsList = getIntent().getParcelableArrayListExtra(NEWS_LIST_KEY);
        mCurIndex = getIntent().getIntExtra(NEWS_POSITION, -1);
        if (mNewsList == null || mNewsList.size() == 0 || mCurIndex == -1) {
            finish();
            return;
        }
        DaggerNewsArticleComponent.builder()
                .newsArticleModule(new NewsArticleModule(this, mNewsList.get(mCurIndex).getPostid()))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        mTvTime.setText(mNewsList.get(mCurIndex).getPtime());
        mTvTitle.setText(mNewsList.get(mCurIndex).getTitle());
        mTvTitle2.setText(mNewsList.get(mCurIndex).getTitle());
        if (mCurIndex == mNewsList.size() - 1) {
            mTvNextTitle.setText("已经是最后一篇了");
        } else {
            mTvNextTitle.setText(mNewsList.get(mCurIndex + 1).getTitle());
        }
        mToolbarHeight = getResources().getDimensionPixelSize(R.dimen.news_detail_toolbar_height);
        mTopBarHeight = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        mMinScrollSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        ViewCompat.setTranslationY(mLlTopBar, -mTopBarHeight);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > mToolbarHeight) {
                    if (AnimateHelper.isRunning(mTopBarAnimator)) {
                        return;
                    }
                    if (Math.abs(scrollY - mLastScrollY) > mMinScrollSlop) {
                        boolean isPullUp = scrollY > mLastScrollY;
                        mLastScrollY = scrollY;
                        if (isPullUp && mLlTopBar.getTranslationY() != -mTopBarHeight) {
                            mTopBarAnimator = AnimateHelper.doMoveVertical(mLlTopBar, (int) mLlTopBar.getTranslationY(),
                                    -mTopBarHeight, 300);
                        } else if (!isPullUp && mLlTopBar.getTranslationY() != 0) {
                            mTopBarAnimator = AnimateHelper.doMoveVertical(mLlTopBar, (int) mLlTopBar.getTranslationY(),
                                    0, 300);
                        }
                    }
                } else {
                    if (mLlTopBar.getTranslationY() != -mTopBarHeight) {
                        AnimateHelper.stopAnimator(mTopBarAnimator);
                        ViewCompat.setTranslationY(mLlTopBar, -mTopBarHeight);
                        mLastScrollY = 0;
                    }
                }
            }
        });
        mScrollView.setFootView(mLlFootView);
        mScrollView.setPullListener(new PullScrollView.OnPullListener() {
            boolean isShowPopup = PreferencesUtils.getBoolean(NewsArticleActivity.this, SHOW_POPUP_DETAIL, true);

            @Override
            public boolean isDoPull() {
                if (mWebView.getProgress() < 50 || mEmptyLayout.getEmptyStatus() != EmptyLayout.STATUS_HIDE) {
                    return false;
                }
                if (isShowPopup) {
                    _showPopup();
                    isShowPopup = false;
                }
                return true;
            }

            @Override
            public boolean handlePull() {
                if (mCurIndex == mNewsList.size() - 1) {
                    return false;
                } else {
                    launchInside(mNewsList, ++mCurIndex);
                    return true;
                }
            }
        });
//        mWebView.setWebListener(new CustomWebView.OnWebViewListener() {
//            @Override
//            public void onProgressChanged(int progress) {
//                Logger.w("" + progress);
//                if (progress > 50 && mEmptyLayout.getEmptyStatus() == EmptyLayout.STATUS_LOADING) {
//                    hideLoading();
//                }
//            }
//
//            @Override
//            public void onError() {
//                showNetError(new EmptyLayout.OnRetryListener() {
//                    @Override
//                    public void onRetry() {
//                        updateViews();
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void loadData(String data) {

    }

    @Override
    public void loadRelatedNews(NewsDetailInfo newsDetailBean) {

    }

    /**
     * 处理关联新闻
     *
     * @param newsDetailBean
     */
    private void _handleRelatedNews(NewsDetailInfo newsDetailBean) {
        if (!ListUtils.isEmpty(newsDetailBean.getRelative_sys())) {
            ViewStub stub = (ViewStub) findViewById(R.id.vs_related_news);
            assert stub != null;
            stub.inflate();
            TextView mTvTopicName = (TextView) findViewById(R.id.tv_topic_name);
            if (!ListUtils.isEmpty(newsDetailBean.getHuati())) {
                mTvTopicName.setText(newsDetailBean.getHuati().get(0).getTopicName());
            }
            RecyclerView mRvRelatedNews = (RecyclerView) findViewById(R.id.rv_related_news);
            RecyclerViewHelper.initRecyclerViewV(this, mRvRelatedNews, false, mRelatedAdapter);
            mRelatedAdapter.updateItems(newsDetailBean.getRelative_sys());
        }
    }

    private void _showPopup() {
        if (PreferencesUtils.getBoolean(this, SHOW_POPUP_DETAIL, true)) {
            View inflate = View.inflate(NewsArticleActivity.this, R.layout.layout_popup, null);
            BubblePopup bubblePopup = new BubblePopup(NewsArticleActivity.this, inflate);
            bubblePopup.anchorView(mTvTitle2)
                    .gravity(Gravity.BOTTOM)
                    .showAnim(new SlideBottomEnter())
                    .dismissAnim(new SlideTopExit())
                    .autoDismiss(true)
                    .autoDismissDelay(3500)
                    .show();

            View inflateBottom = View.inflate(NewsArticleActivity.this, R.layout.layout_popup_bottom, null);
            BubblePopup bubblePopupBottom = new BubblePopup(NewsArticleActivity.this, inflateBottom);
            bubblePopupBottom.anchorView(mLlFootView)
                    .gravity(Gravity.TOP)
                    .showAnim(new SlideLeftEnter())
                    .dismissAnim(new SlideRightExit())
                    .autoDismiss(true)
                    .autoDismissDelay(3500)
                    .show();
            PreferencesUtils.putBoolean(this, SHOW_POPUP_DETAIL, false);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_back_2, R.id.tv_title_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_back_2:
                break;
            case R.id.tv_title_2:
                break;
        }
    }
}
