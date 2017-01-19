package com.dl7.mvp.module.manage.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.dl7.mvp.R;
import com.dl7.mvp.widget.swipeback.SwipeBackActivity;

import butterknife.BindView;

public class SettingsActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, true, "设置");
    }

    @Override
    protected void updateViews() {
        getFragmentManager().beginTransaction().replace(R.id.settings_view, new SettingsFragment()).commit();
    }
}
