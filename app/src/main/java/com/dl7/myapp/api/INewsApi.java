package com.dl7.myapp.api;

import com.dl7.myapp.local.table.BeautyPhotoBean;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.api.bean.NewsDetailBean;
import com.dl7.myapp.api.bean.PhotoBean;
import com.dl7.myapp.api.bean.PhotoSetBean;
import com.dl7.myapp.api.bean.SpecialBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.dl7.myapp.api.RetrofitService.CACHE_CONTROL_NETWORK;

/**
 * Created by long on 2016/8/22.
 * API 接口
 */
public interface INewsApi {

    /**
     * 获取新闻列表
     * eg: http://c.m.163.com/nc/article/headline/T1348647909107/60-20.html
     *      http://c.m.163.com/nc/article/list/T1348647909107/60-20.html
     *
     * @param type 新闻类型
     * @param id 新闻ID
     * @param startPage 起始页码
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsBean>>> getNewsList(@Path("type") String type, @Path("id") String id,
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

    /**
     * 获取图片列表
     * eg: http://c.3g.163.com/photo/api/list/0096/4GJ60096.json
     *
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("photo/api/list/0096/4GJ60096.json")
    Observable<List<PhotoBean>> getPhotoList();

    /**
     * 获取更多图片列表
     * eg: http://c.3g.163.com/photo/api/morelist/0096/4GJ60096/106571.json
     *
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("photo/api/morelist/0096/4GJ60096/{setId}.json")
    Observable<List<PhotoBean>> getPhotoMoreList(@Path("setId") String setId);

    /**
     * 获取美女图片，这个API不完整，省略了好多参数
     * eg: http://c.3g.163.com/recommend/getChanListNews?channel=T1456112189138&size=20&offset=0
     *
     * @param offset 起始页码
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("recommend/getChanListNews?channel=T1456112189138&size=20")
    Observable<Map<String, List<BeautyPhotoBean>>> getBeautyPhoto(@Query("offset") int offset);
}
