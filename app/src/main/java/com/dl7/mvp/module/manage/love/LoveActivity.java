package com.dl7.mvp.module.manage.love;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.injector.components.DaggerLoveComponent;
import com.dl7.mvp.injector.modules.LoveModule;
import com.dl7.mvp.local.table.BeautyPhotoBean;
import com.dl7.mvp.module.base.BaseActivity;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.photo.bigphoto.BigPhotoActivity;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRemoveDataListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;

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
        mRvPhotoList.setItemAnimator(new FlipInLeftYAnimator());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BigPhotoActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            final boolean[] delLove = data.getBooleanArrayExtra(BigPhotoActivity.RESULT_KEY);
            // 延迟 500MS 做删除操作，不然退回来看不到动画效果
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = delLove.length - 1; i >= 0; i--) {
                        if (delLove[i]) {
                            mAdapter.removeItem(i);
                        }
                    }
                }
            }, 500);
        }
    }
}
