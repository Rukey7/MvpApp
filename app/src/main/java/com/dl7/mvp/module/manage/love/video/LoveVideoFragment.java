package com.dl7.mvp.module.manage.love.video;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dl7.mvp.R;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.module.base.BaseFragment;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.manage.love.ILoveView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by long on 2016/12/13.
 */
public class LoveVideoFragment extends BaseFragment<ILocalPresenter> implements ILoveView<VideoInfo> {


    @BindView(R.id.rv_love_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_love_list;
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

    @Override
    public void loadData(List<VideoInfo> photoList) {

    }

    @Override
    public void noData() {

    }
}
