package com.dl7.mvp.injector.components;

import com.dl7.mvp.injector.PerActivity;
import com.dl7.mvp.injector.modules.DownloadModule;
import com.dl7.mvp.module.manage.download.DownloadActivity;

import dagger.Component;

/**
 * Created by long on 2016/12/19.
 * video下载 Component
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = DownloadModule.class)
public interface DownloadComponent {
    void inject(DownloadActivity activity);
}
