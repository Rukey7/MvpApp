package com.dl7.mvp.module.manage.download.cache;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dl7.downloaderlib.entity.FileInfo;
import com.dl7.mvp.R;
import com.dl7.mvp.adapter.BaseVideoDownloadAdapter;
import com.dl7.mvp.injector.components.DaggerVideoCacheComponent;
import com.dl7.mvp.injector.modules.VideoCacheModule;
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
 * Created by long on 2016/12/15.
 * video缓存列表
 */
public class VideoCacheFragment extends BaseFragment<IRxBusPresenter> implements ILocalView<VideoInfo> {

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
        DaggerVideoCacheComponent.builder()
                .applicationComponent(getAppComponent())
                .videoCacheModule(new VideoCacheModule(this))
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
        mPresenter.registerRxBus(FileInfo.class, new Action1<FileInfo>() {
            @Override
            public void call(FileInfo fileInfo) {
                mAdapter.updateDownload(fileInfo);
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
