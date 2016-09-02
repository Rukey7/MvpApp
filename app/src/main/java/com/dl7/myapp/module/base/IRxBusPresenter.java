package com.dl7.myapp.module.base;

import com.dl7.myapp.module.base.IBasePresenter;

/**
 * Created by long on 2016/9/2.
 * RxBus Presenter
 */
public interface IRxBusPresenter extends IBasePresenter {

    <T> void registerRxBus(Class<T> eventType);

    void unregisterRxBus();
}
