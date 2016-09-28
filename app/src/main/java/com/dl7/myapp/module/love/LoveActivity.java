package com.dl7.myapp.module.love;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.helperlibrary.listener.OnRemoveDataListener;
import com.dl7.myapp.R;
import com.dl7.myapp.injector.components.DaggerLoveComponent;
import com.dl7.myapp.injector.modules.LoveModule;
import com.dl7.myapp.local.table.BeautyPhotoBean;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.ILocalPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * 收藏界面
 */
public class LoveActivity extends BaseActivity<ILocalPresenter> implements ILoveView {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Inject
    BaseQuickAdapter mAdapter;


    public static void launch(Context context) {
        Intent intent = new Intent(context, LoveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_love;
    }

    @Override
    protected void initInjector() {
        DaggerLoveComponent.builder()
                .applicationComponent(getAppComponent())
                .loveModule(new LoveModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "收藏");
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewSV(this, mRvPhotoList, slideAdapter, 2);
        RecyclerViewHelper.startDragAndSwipe(mRvPhotoList, mAdapter);
        mAdapter.setRemoveDataListener(new OnRemoveDataListener() {
            @Override
            public void onRemove(int position) {
                mPresenter.delete(mAdapter.getItem(position));
            }
        });
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void loadData(List<BeautyPhotoBean> photoList) {
        if (mDefaultBg.getVisibility() == View.VISIBLE) {
            mDefaultBg.setVisibility(View.GONE);
        }
        mAdapter.updateItems(photoList);
    }

    @Override
    public void noData() {
        mDefaultBg.setVisibility(View.VISIBLE);
    }
}
