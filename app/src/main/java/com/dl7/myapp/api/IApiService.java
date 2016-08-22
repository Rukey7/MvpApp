package com.dl7.myapp.api;

import com.dl7.myapp.api.bean.NewList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by long on 2016/8/22.
 * API 接口
 */
public interface IApiService {

    // http://c.m.163.com/nc/article/headline/T1348647909107/60-20.html

    @GET("nc/article/headline/T1348647909107/{page}-20.html")
    Observable<NewList> headLine(@Path("page") int page);
}
