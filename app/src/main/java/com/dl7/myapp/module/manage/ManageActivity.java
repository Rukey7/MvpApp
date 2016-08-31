package com.dl7.myapp.module.manage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.myapp.R;
import com.dl7.myapp.injector.components.DaggerManageComponent;
import com.dl7.myapp.injector.modules.ManageModule;
import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.module.base.BaseActivity;
import com.dl7.myapp.utils.AssetsHelper;
import com.dl7.myapp.utils.GsonHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ManageActivity extends BaseActivity {

    @BindView(R.id.rv_checked_list)
    RecyclerView mRvCheckedList;
    @BindView(R.id.rv_unchecked_list)
    RecyclerView mRvUncheckedList;

    @Inject
    BaseQuickAdapter mCheckedAdapter;
//    @Inject
//    BaseQuickAdapter mUncheckedAdapter;


    public static void launch(Context context) {
        Intent intent = new Intent(context, ManageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_manage;
    }

    @Override
    protected void initViews() {
        DaggerManageComponent.builder()
                .manageModule(new ManageModule(this))
                .build()
                .inject(this);
        RecyclerViewHelper.initRecyclerViewG(this, mRvCheckedList, mCheckedAdapter, 4);
//        RecyclerViewHelper.initRecyclerViewG(this, mRvUncheckedList, mUncheckedAdapter, 4);
    }

    @Override
    protected void updateViews() {
        List<NewsTypeBean> channels = GsonHelper.convertEntities(AssetsHelper.readData(this, "NewsChannel"),
                NewsTypeBean.class);
        Logger.w(channels.toString());
        Logger.e(mCheckedAdapter.toString());
        mCheckedAdapter.updateItems(channels);
//        mUncheckedAdapter.updateItems(channels);
    }

}
