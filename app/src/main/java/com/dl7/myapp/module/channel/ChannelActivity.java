package com.dl7.myapp.module.channel;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.helperlibrary.listener.OnRecyclerViewItemClickListener;
import com.dl7.helperlibrary.listener.OnRemoveDataListener;
import com.dl7.myapp.R;
import com.dl7.myapp.injector.components.DaggerManageComponent;
import com.dl7.myapp.injector.modules.ManageModule;
import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.module.base.ILocalPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class ChannelActivity extends BaseActivity implements IChannelView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_checked_list)
    RecyclerView mRvCheckedList;
    @BindView(R.id.rv_unchecked_list)
    RecyclerView mRvUncheckedList;

    @Inject
    BaseQuickAdapter mCheckedAdapter;
    @Inject
    BaseQuickAdapter mUncheckedAdapter;
    @Inject
    ILocalPresenter mPresenter;


    public static void launch(Context context) {
        Intent intent = new Intent(context, ChannelActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_channel;
    }

    @Override
    protected void initViews() {
        DaggerManageComponent.builder()
                .applicationComponent(getAppComponent())
                .manageModule(new ManageModule(this))
                .build()
                .inject(this);
        initToolBar(mToolbar, true, "栏目管理");
        RecyclerViewHelper.initRecyclerViewG(this, mRvCheckedList, mCheckedAdapter, 4);
        RecyclerViewHelper.initRecyclerViewG(this, mRvUncheckedList, mUncheckedAdapter, 4);
        RecyclerViewHelper.startDragAndSwipe(mRvCheckedList, mCheckedAdapter, 3);
        // 设置动画
        mRvCheckedList.setItemAnimator(new ScaleInAnimator());
        mRvUncheckedList.setItemAnimator(new FlipInBottomXAnimator());
        // 设置拖拽背景
        mCheckedAdapter.setDragDrawable(ContextCompat.getDrawable(this, R.drawable.shape_channel_drag));
        // 设置移除监听器
        mCheckedAdapter.setRemoveDataListener(new OnRemoveDataListener() {
            @Override
            public void onRemove(int position) {
                mPresenter.delete(mCheckedAdapter.getItem(position));
                mUncheckedAdapter.addLastItem(mCheckedAdapter.getItem(position));
            }
        });
        mUncheckedAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 添加要放在删除前，不然获取不到对应数据
                mCheckedAdapter.addLastItem(mUncheckedAdapter.getItem(position));
                mPresenter.insert(mUncheckedAdapter.getItem(position));
                mUncheckedAdapter.removeItem(position);
            }
        });
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void loadData(List<NewsTypeBean> checkList, List<NewsTypeBean> uncheckList) {
        mCheckedAdapter.updateItems(checkList);
        mUncheckedAdapter.updateItems(uncheckList);
    }
}
