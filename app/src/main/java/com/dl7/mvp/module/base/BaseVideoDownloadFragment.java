package com.dl7.mvp.module.base;

import android.view.View;

import com.dl7.mvp.adapter.BaseVideoDownloadAdapter;
import com.dl7.mvp.module.manage.download.DownloadActivity;
import com.dl7.recycler.listener.OnRecyclerViewItemLongClickListener;

import javax.inject.Inject;

/**
 * Created by long on 2016/12/20.
 * video下载的基类Fragment
 */
public abstract class BaseVideoDownloadFragment<T extends IBasePresenter> extends BaseFragment<T> {

    @Inject
    protected BaseVideoDownloadAdapter mAdapter;

    /**
     * 初始化长按点击，必须在 initViews() 里调用
     */
    public void initItemLongClick() {
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
    }

    /**
     * 处理后退键
     *
     * @return
     */
    public boolean exitEditMode() {
        if (mAdapter.isEditMode()) {
            mAdapter.setEditMode(false);
            return true;
        }
        return false;
    }

    /**
     * 是否存于编辑模式
     * @return
     */
    public boolean isEditMode() {
        return mAdapter.isEditMode();
    }

    /**
     * 全选或取消全选
     * @param isChecked
     */
    public void checkAllOrNone(boolean isChecked) {
        mAdapter.checkAllOrNone(isChecked);
    }

    /**
     * 删除选中
     */
    public void deleteChecked() {
        mAdapter.deleteItemChecked();
    }
}
