package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.ChannelModule;
import com.dl7.mvp.module.news.channel.ChannelActivity;

import dagger.Component;

/**
 * Created by long on 2016/8/31.
 * 管理 Component
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ChannelModule.class)
public interface ManageComponent {
    void inject(ChannelActivity activity);
}
