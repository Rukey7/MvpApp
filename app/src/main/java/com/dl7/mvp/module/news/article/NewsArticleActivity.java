package com.dl7.mvp.module.news.article;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.api.NewsUtils;
import com.dl7.mvp.api.bean.NewsDetailInfo;
import com.dl7.mvp.injector.components.DaggerNewsArticleComponent;
import com.dl7.mvp.injector.modules.NewsArticleModule;
import com.dl7.mvp.module.base.BaseSwipeBackActivity;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.utils.AnimateHelper;
import com.dl7.mvp.utils.DialogHelper;
import com.dl7.mvp.utils.ListUtils;
import com.dl7.mvp.utils.PreferencesUtils;
import com.dl7.mvp.widget.EmptyLayout;
import com.dl7.mvp.widget.PullScrollView;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideEnter.SlideLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.animation.SlideExit.SlideTopExit;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnURLClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsArticleActivity extends BaseSwipeBackActivity<IBasePresenter> implements INewsArticleView {

    private static final String SHOW_POPUP_DETAIL = "ShowPopupDetail";
    private static final String NEWS_ID_KEY = "NewsIdKey";

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_pre_toolbar)
    LinearLayout mLlPreToolbar;
    @BindView(R.id.tv_content)
    TextView mTvContent;
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

    private int mToolbarHeight;
    private int mTopBarHeight;
    private Animator mTopBarAnimator;
    private int mLastScrollY = 0;
    // 最小触摸滑动距离
    private int mMinScrollSlop;
    private String mNewsId;
    private String mNextNewsId;


    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, NewsArticleActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    private void launchInside(String newsId) {
        Intent intent = new Intent(this, NewsArticleActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
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
        mNewsId = getIntent().getStringExtra(NEWS_ID_KEY);
        DaggerNewsArticleComponent.builder()
                .newsArticleModule(new NewsArticleModule(this, mNewsId))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
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
                if (mEmptyLayout.getEmptyStatus() != EmptyLayout.STATUS_HIDE) {
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
                if (TextUtils.isEmpty(mNextNewsId)) {
                    return false;
                } else {
                    launchInside(mNextNewsId);
                    return true;
                }
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(NewsDetailInfo newsDetailBean) {
        mTvTitle.setText(newsDetailBean.getTitle());
        mTvTitle2.setText(newsDetailBean.getTitle());
        mTvTime.setText(newsDetailBean.getPtime());
        RichText.from(newsDetailBean.getBody())
                .into(mTvContent);
        _handleSpInfo(newsDetailBean.getSpinfo());
        _handleRelatedNews(newsDetailBean);
    }

    /**
     * 处理关联的内容
     *
     * @param spinfo
     */
    private void _handleSpInfo(List<NewsDetailInfo.SpinfoEntity> spinfo) {
        if (!ListUtils.isEmpty(spinfo)) {
            ViewStub stub = (ViewStub) findViewById(R.id.vs_related_content);
            assert stub != null;
            stub.inflate();
            TextView tvType = (TextView) findViewById(R.id.tv_type);
            TextView tvRelatedContent = (TextView) findViewById(R.id.tv_related_content);
            tvType.setText(spinfo.get(0).getSptype());
            RichText.from(spinfo.get(0).getSpcontent())
                    // 这里处理超链接的点击
                    .urlClick(new OnURLClickListener() {
                        @Override
                        public boolean urlClicked(String url) {
                            String newsId = NewsUtils.clipNewsIdFromUrl(url);
                            if (newsId != null) {
                                launch(NewsArticleActivity.this, newsId);
                            }
                            return true;
                        }
                    })
                    .into(tvRelatedContent);
        }
    }

    /**
     * 处理关联新闻
     *
     * @param newsDetailBean
     */
    private void _handleRelatedNews(NewsDetailInfo newsDetailBean) {
        if (ListUtils.isEmpty(newsDetailBean.getRelative_sys())) {
            mTvNextTitle.setText("没有相关文章了");
        } else {
            mNextNewsId = newsDetailBean.getRelative_sys().get(0).getId();
            mTvNextTitle.setText(newsDetailBean.getRelative_sys().get(0).getTitle());
        }
    }

    /**
     * 显示弹出提示
     */
    private void _showPopup() {
        if (PreferencesUtils.getBoolean(this, SHOW_POPUP_DETAIL, true)) {
            DialogHelper.createPopup(this, R.layout.layout_popup)
                    .anchorView(mTvTitle2)
                    .gravity(Gravity.BOTTOM)
                    .showAnim(new SlideBottomEnter())
                    .dismissAnim(new SlideTopExit())
                    .autoDismiss(true)
                    .autoDismissDelay(3500)
                    .show();
            DialogHelper.createPopup(this, R.layout.layout_popup_bottom)
                    .anchorView(mLlFootView)
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
            case R.id.iv_back_2:
                finish();
                break;
            case R.id.tv_title_2:
                mScrollView.stopNestedScroll();
                mScrollView.smoothScrollTo(0, 0);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
    }
}
