package com.dl7.myapp.injector.components;

import com.dl7.myapp.injector.PerFragment;
import com.dl7.myapp.injector.modules.WelfarePhotoModule;
import com.dl7.myapp.module.welfarephoto.WelfarePhotoFragment;

import dagger.Component;

/**
 * Created by long on 2016/10/11.
 * 福利图片界面 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = WelfarePhotoModule.class)
public interface WelfarePhotoComponent {
    void inject(WelfarePhotoFragment fragment);
}
