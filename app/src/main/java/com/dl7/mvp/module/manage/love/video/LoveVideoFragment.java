package com.dl7.mvp.module.manage.love.video;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.injector.components.DaggerLoveVideoComponent;
import com.dl7.mvp.injector.modules.LoveVideoModule;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.module.base.BaseFragment;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.base.ILocalView;
import com.dl7.mvp.module.video.player.VideoPlayerActivity;
import com.dl7.mvp.utils.CommonConstant;
import com.dl7.mvp.utils.DialogHelper;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.dl7.recycler.listener.OnRecyclerViewItemLongClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.app.Activity.RESULT_OK;

/**
 * Created by long on 2016/12/13.
 * Video收藏界面
 */
public class LoveVideoFragment extends BaseFragment<ILocalPresenter> implements ILocalView<VideoInfo> {

    @BindView(R.id.rv_love_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Inject
    BaseQuickAdapter mAdapter;

    private int mCurIndex;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_love_list;
    }

    @Override
    protected void initInjector() {
        DaggerLoveVideoComponent.builder()
                .applicationComponent(getAppComponent())
                .loveVideoModule(new LoveVideoModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        SlideInRightAnimationAdapter slideAdapter = new SlideInRightAnimationAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvVideoList, slideAdapter);
        mRvVideoList.setItemAnimator(new SlideInLeftAnimator());
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurIndex = position;
                VideoPlayerActivity.launchForResult(LoveVideoFragment.this, (VideoInfo) mAdapter.getItem(position));
            }
        });
        mAdapter.setOnItemLongClickListener(new OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position) {
                DialogHelper.deleteDialog(mContext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delete(mAdapter.getItem(position));
                        mAdapter.removeItem(position);
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<VideoInfo> photoList) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonConstant.VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            final boolean isCollect = data.getBooleanExtra(CommonConstant.RESULT_KEY, true);
            // 延迟 500MS 做删除操作，不然退回来看不到动画效果
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isCollect) {
                        mAdapter.removeItem(mCurIndex);
                    }
                }
            }, 500);
        }
    }
}
