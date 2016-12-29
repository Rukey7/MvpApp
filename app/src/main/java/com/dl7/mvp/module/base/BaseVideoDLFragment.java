package com.dl7.mvp.module.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dl7.mvp.R;
import com.dl7.mvp.adapter.BaseVideoDLAdapter;
import com.dl7.mvp.module.manage.download.DownloadActivity;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.dl7.recycler.listener.OnRecyclerViewItemLongClickListener;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by long on 2016/12/20.
 * video下载的基类Fragment
 */
public abstract class BaseVideoDLFragment<T extends IBasePresenter> extends BaseFragment<T> {

    @BindView(R.id.rv_video_list)
    protected RecyclerView mRvVideoList;

    @Inject
    protected BaseVideoDLAdapter mAdapter;

    private int mEditLayoutHeight;

    /**
     * 初始化长按点击，必须在 initViews() 里调用
     */
    public void initItemLongClick() {
        mEditLayoutHeight = getResources().getDimensionPixelSize(R.dimen.edit_layout_height);
        mAdapter.setOnItemLongClickListener(new OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                if (!mAdapter.isEditMode()) {
                    mAdapter.setEditMode(true);
                    ((DownloadActivity) getActivity()).enableEditMode(true);
                    // 增加 padding 是防止底下被删除按钮遮住
                    mRvVideoList.setPadding(0, 0, 0, mEditLayoutHeight);
                }
                // 这里获取对应position对应的ViewHolder,需要借助RecyclerView，还有个更简便的做法是自定义的点击事件把ViewHolder一起传过来
                BaseViewHolder viewHolder = (BaseViewHolder) mRvVideoList.getChildViewHolder(view);
                if (viewHolder != null) {
                    mAdapter.toggleItemChecked(position, viewHolder);
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
            mRvVideoList.setPadding(0, 0, 0, 0);
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
