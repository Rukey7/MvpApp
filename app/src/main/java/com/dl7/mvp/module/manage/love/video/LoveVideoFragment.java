package com.dl7.mvp.module.manage.love.video;

import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.module.base.BaseFragment;
import com.dl7.mvp.module.base.ILocalPresenter;
import com.dl7.mvp.module.manage.love.ILoveView;

import java.util.List;

/**
 * Created by long on 2016/12/13.
 */
public class LoveVideoFragment extends BaseFragment<ILocalPresenter> implements ILoveView<VideoInfo> {


    @Override
    protected int attachLayoutRes() {
        return 0;
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
