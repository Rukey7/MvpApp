package com.dl7.myapp.module.base;

/**
 * Created by long on 2016/9/2.
 * RxBus Presenter
 */
public interface IRxBusPresenter extends IBasePresenter {

    /**
     * 注册
     * @param eventType
     * @param <T>
     */
    <T> void registerRxBus(Class<T> eventType);

    /**
     * 注销
     */
    void unregisterRxBus();
}
