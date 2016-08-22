package com.dl7.myapp.api;

import com.dl7.myapp.api.bean.NewsBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by long on 2016/8/22.
 * API 接口
 */
public interface IApiService {

    /**
     * 获取新闻列表
     * eg: http://c.m.163.com/nc/article/headline/T1348647909107/60-20.html
     *
     * @param name headline 或者 list
     * @param type 新闻类型
     * @param page 起始页码
     * @return
     */
    @GET("nc/article/{list}/{type}/{page}-20.html")
    Observable<Map<String, List<NewsBean>>> getNewsList(@Path("list") String name,
                                                        @Path("type") String type,
                                                        @Path("page") int page);
}
