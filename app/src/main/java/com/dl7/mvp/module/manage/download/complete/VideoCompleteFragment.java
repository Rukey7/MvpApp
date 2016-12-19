package com.dl7.mvp.module.manage.download.complete;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.BaseVideoDownloadAdapter;
import com.dl7.mvp.injector.components.DaggerVideoCompleteComponent;
import com.dl7.mvp.injector.modules.VideoCompleteModule;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.module.base.BaseFragment;
import com.dl7.mvp.module.base.ILocalView;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.module.manage.download.DownloadActivity;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemLongClickListener;
import com.dl7.recycler.listener.OnRemoveDataListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import rx.functions.Action1;

/**
 * Created by long on 2016/12/16.
 * video 缓存完成列表
 */
public class VideoCompleteFragment extends BaseFragment<IRxBusPresenter> implements ILocalView<VideoInfo> {

    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Inject
    BaseVideoDownloadAdapter mAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initInjector() {
        DaggerVideoCompleteComponent.builder()
                .applicationComponent(getAppComponent())
                .videoCompleteModule(new VideoCompleteModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvVideoList, mAdapter);
        mRvVideoList.setItemAnimator(new SlideInLeftAnimator());
        mAdapter.setRemoveDataListener(new OnRemoveDataListener() {
            @Override
            public void onRemove(int position) {
                if (mAdapter.getItemCount() <= 1 && mDefaultBg.getVisibility() == View.GONE) {
                    mDefaultBg.setVisibility(View.VISIBLE);
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                if (!mAdapter.isEditMode()) {
                    mAdapter.toggleItemChecked(position);
                    mAdapter.setEditMode(true);
                    ((DownloadActivity) getActivity()).enableEditMode(true);
                } else {
                    mAdapter.toggleItemChecked(position);
                }
                return true;
            }
        });
        mPresenter.registerRxBus(VideoInfo.class, new Action1<VideoInfo>() {
            @Override
            public void call(VideoInfo info) {
                mAdapter.addItem(info);
                if (mDefaultBg.getVisibility() == View.VISIBLE) {
                    mDefaultBg.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void loadData(List<VideoInfo> dataList) {
        if (mDefaultBg.getVisibility() == View.VISIBLE) {
            mDefaultBg.setVisibility(View.GONE);
        }
        mAdapter.updateItems(dataList);
    }

    @Override
    public void noData() {
        mDefaultBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    /**
     * 处理后退键
     *
     * @return
     */
    public boolean onBackPressed() {
        if (mAdapter.isEditMode()) {
            mAdapter.setEditMode(false);
            return true;
        }
        return false;
    }

    public boolean isEditMode() {
        return mAdapter.isEditMode();
    }

    public void checkAllOrNone(boolean isChecked) {
        mAdapter.checkAllOrNone(isChecked);
    }

    public void deleteChecked() {
        mAdapter.deleteItemChecked();
    }
}
