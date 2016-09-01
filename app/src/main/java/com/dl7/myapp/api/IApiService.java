package com.dl7.myapp.api;

import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.api.bean.NewsDetailBean;
import com.dl7.myapp.api.bean.PhotoSetBean;
import com.dl7.myapp.api.bean.SpecialBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

import static com.dl7.myapp.api.RetrofitService.CACHE_CONTROL_NETWORK;

/**
 * Created by long on 2016/8/22.
 * API 接口
 */
public interface IApiService {

    /**
     * 获取新闻列表
     * eg: http://c.m.163.com/nc/article/list/T1348647909107/60-20.html
     *
     * @param type 新闻类型
     * @param startPage 起始页码
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("nc/article/list/{type}/{startPage}-20.html")
    Observable<Map<String, List<NewsBean>>> getNewsList(@Path("type") String type,
                                                        @Path("startPage") int startPage);

    /**
     * 获取专题
     * eg: http://c.3g.163.com/nc/special/S1451880983492.html
     *
     * @param specialIde 专题ID
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("nc/special/{specialId}.html")
    Observable<Map<String, SpecialBean>> getSpecial(@Path("specialId") String specialIde);

    /**
     * 获取新闻详情
     * eg: http://c.3g.163.com/nc/article/BV56RVG600011229/full.html
     *
     * @param newsId 专题ID
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("nc/article/{newsId}/full.html")
    Observable<Map<String, NewsDetailBean>> getNewsDetail(@Path("newsId") String newsId);

    /**
     * 获取新闻详情
     * eg: http://c.3g.163.com/photo/api/set/0006/2136404.json
     *
     * @param photoId 图集ID
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("photo/api/set/{photoId}.json")
    Observable<PhotoSetBean> getPhotoSet(@Path("photoId") String photoId);
}
