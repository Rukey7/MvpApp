package com.dl7.mvp.module.news.special;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.item.SpecialItem;
import com.dl7.mvp.api.bean.SpecialInfo;
import com.dl7.mvp.injector.components.DaggerSpecialComponent;
import com.dl7.mvp.injector.modules.SpecialModule;
import com.dl7.mvp.module.base.BaseSwipeBackActivity;
import com.dl7.mvp.module.base.IBasePresenter;
import com.dl7.mvp.utils.DefIconFactory;
import com.dl7.mvp.utils.ImageLoader;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.dl7.tag.TagView.OnTagClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SpecialActivity extends BaseSwipeBackActivity<IBasePresenter> implements ISpecialView {

    private static final String SPECIAL_KEY = "SpecialKey";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_news_list)
    RecyclerView mRvNewsList;
    @BindView(R.id.fab_coping)
    FloatingActionButton mFabCoping;

    @Inject
    BaseQuickAdapter mSpecialAdapter;

    private TagLayout mTagLayout;
    private String mSpecialId;
    private final int[] mSkipId = new int[20];
    private LinearLayoutManager mLayoutManager;

    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, SpecialActivity.class);
        intent.putExtra(SPECIAL_KEY, newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
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
        initToolBar(mToolbar, true, "");
        ScaleInAnimationAdapter animAdapter = new ScaleInAnimationAdapter(mSpecialAdapter);
        RecyclerViewHelper.initRecyclerViewV(this, mRvNewsList, true, new AlphaInAnimationAdapter(animAdapter));
        mLayoutManager = (LinearLayoutManager) mRvNewsList.getLayoutManager();
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<SpecialItem> specialItems) {
        mSpecialAdapter.updateItems(specialItems);
        _handleTagLayout(specialItems);
    }

    @Override
    public void loadBanner(SpecialInfo specialBean) {
        View headView = LayoutInflater.from(this).inflate(R.layout.head_special, null);
        ImageView mIvBanner = (ImageView) headView.findViewById(R.id.iv_banner);
        // 加载图片
        ImageLoader.loadFitCenter(this, specialBean.getBanner(), mIvBanner, DefIconFactory.provideIcon());
        // 添加导语
        if (!TextUtils.isEmpty(specialBean.getDigest())) {
            ViewStub stub = (ViewStub) headView.findViewById(R.id.vs_digest);
            assert stub != null;
            stub.inflate();
            TextView tvDigest = (TextView) headView.findViewById(R.id.tv_digest);
            tvDigest.setText(specialBean.getDigest());
        }
        mTagLayout = (TagLayout) headView.findViewById(R.id.tag_layout);
        mSpecialAdapter.addHeaderView(headView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
    }

    /**
     * 处理导航标签
     *
     * @param specialItems
     */
    private void _handleTagLayout(List<SpecialItem> specialItems) {
        Observable.from(specialItems)
                .compose(this.<SpecialItem>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<SpecialItem, Boolean>() {
                    int i = 0;
                    int index = mSpecialAdapter.getHeaderViewsCount();  // 存在一个 HeadView 所以从1算起

                    @Override
                    public Boolean call(SpecialItem specialItem) {
                        if (specialItem.isHeader) {
                            // 记录头部的列表索引值，用来跳转
                            mSkipId[i++] = index;
                        }
                        index++;
                        return specialItem.isHeader;
                    }
                })
                .map(new Func1<SpecialItem, String>() {
                    @Override
                    public String call(SpecialItem specialItem) {
                        return _clipHeadStr(specialItem.header);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mTagLayout.addTag(s);
                    }
                });
        mTagLayout.setTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text, @TagView.TagMode int tagMode) {
                // 跳转到对应position,比scrollToPosition（）精确
                mLayoutManager.scrollToPositionWithOffset(mSkipId[position], 0);
            }
        });
    }

    private String _clipHeadStr(String headStr) {
        String head = null;
        int index = headStr.indexOf(" ");
        if (index != -1) {
            head = headStr.substring(index, headStr.length());
        }
        return head;
    }

    @OnClick(R.id.fab_coping)
    public void onClick() {
        mLayoutManager.scrollToPosition(0);
    }
}
