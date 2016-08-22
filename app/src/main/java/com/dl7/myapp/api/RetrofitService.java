package com.dl7.myapp.api;

import android.util.Log;

import com.dl7.myapp.api.bean.NewList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by long on 2016/8/22.
 * 整个网络通信服务的启动控制，必须先调用初始化函数才能正常使用网络通信接口
 */
public class RetrofitService {

    static final String HOST = "http://c.m.163.com/";

    private static IApiService mService;


    private RetrofitService() {}

    /**
     * 初始化网络通信服务
     */
    public static void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(HOST)
                .build();
        mService = retrofit.create(IApiService.class);
    }


    public static void newsList() {
        mService.headLine(0)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewList>() {
                    @Override
                    public void onCompleted() {
                        Log.w("RetrofitService", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RetrofitService", e.toString());
                    }

                    @Override
                    public void onNext(NewList newBeen) {
                        Log.w("RetrofitService", newBeen.toString());
                    }
                });
    }
}
