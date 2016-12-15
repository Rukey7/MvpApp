package com.dl7.mvp.module.manage.download;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.module.base.BaseFragment;
import com.dl7.mvp.module.base.IBasePresenter;

import butterknife.BindView;

/**
 * Created by long on 2016/12/15.
 */

public class DownloadFragment extends BaseFragment<IBasePresenter> {

    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void updateViews() {

    }
}
