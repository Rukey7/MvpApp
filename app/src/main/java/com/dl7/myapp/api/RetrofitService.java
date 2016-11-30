package com.dl7.myapp.api;

import android.util.SparseArray;

import com.dl7.myapp.AndroidApplication;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.api.bean.NewsDetailBean;
import com.dl7.myapp.api.bean.PhotoBean;
import com.dl7.myapp.api.bean.PhotoSetBean;
import com.dl7.myapp.api.bean.SpecialBean;
import com.dl7.myapp.local.table.VideoBean;
import com.dl7.myapp.api.bean.WelfarePhotoBean;
import com.dl7.myapp.api.bean.WelfarePhotoList;
import com.dl7.myapp.local.table.BeautyPhotoBean;
import com.dl7.myapp.utils.NetUtil;
import com.dl7.myapp.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by long on 2016/8/22.
 * 整个网络通信服务的启动控制，必须先调用初始化函数才能正常使用网络通信接口
 */
public class RetrofitService {

    private static final String HEAD_LINE_NEWS = "T1348647909107";

    //设缓存有效期为1天
    static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    private static final String NEWS_HOST = "http://c.3g.163.com/";
    private static final String WELFARE_HOST = "http://gank.io/";

    private static INewsApi sNewsService;
    private static IWelfareApi sWelfareService;
    // 同步锁，处理页码 sNewsPage
    private static Object key = new Object();
    // 保存新闻列表的当前页码
    private static SparseArray<Integer> sNewsPage;
    private static int sBeautyPage = 0;
    private static int sWelfarePage = 0;
    // 递增页码
    private static final int INCREASE_PAGE = 20;


    private RetrofitService() {}

    /**
     * 初始化网络通信服务
     */
    public static void init() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(AndroidApplication.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(sLoggingInterceptor)
                .addInterceptor(sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(NEWS_HOST)
                .build();
        sNewsService = retrofit.create(INewsApi.class);

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(WELFARE_HOST)
                .build();
        sWelfareService = retrofit.create(IWelfareApi.class);
        sNewsPage = new SparseArray<>();
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable(AndroidApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                Logger.e("no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtil.isNetworkAvailable(AndroidApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    // 打印返回的json数据拦截器
    private static final Interceptor sLoggingInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Logger.w(request.url().toString());
            final Response response = chain.proceed(request);

            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    Logger.e("Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                Logger.json(buffer.clone().readString(charset));
            }

            return response;
        }
    };

    /************************************ API *******************************************/

    /**
     * 获取新闻列表
     * @return
     */
    public static Observable<NewsBean> getNewsList(String newsId) {
        synchronized (key) {
            sNewsPage.put(newsId.hashCode(), 0);
        }
        String type;
        if (newsId.equals(HEAD_LINE_NEWS)) {
            type = "headline";
        } else {
            type = "list";
        }
        return sNewsService.getNewsList(type, newsId, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapNews(newsId));
    }

    /**
     * 获取下一页新闻列表
     * @return
     */
    public static Observable<NewsBean> getNewsListNext(String newsId) {
        int page;
        synchronized (key) {
            Integer prePage = sNewsPage.get(newsId.hashCode());
            if (prePage == null) {
                page = 0;
            } else {
                page = prePage + INCREASE_PAGE;
            }
            sNewsPage.put(newsId.hashCode(), page);
        }
        String type;
        if (newsId.equals(HEAD_LINE_NEWS)) {
            type = "headline";
        } else {
            type = "list";
        }
        return sNewsService.getNewsList(type, newsId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapNews(newsId));
    }

    /**
     * 获取专题数据
     * @param specialId
     * @return
     */
    public static Observable<SpecialBean> getSpecial(String specialId) {
        return sNewsService.getSpecial(specialId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapSpecial(specialId));
    }

    /**
     * 获取新闻详情
     * @param newsId 新闻ID
     * @return
     */
    public static Observable<NewsDetailBean> getNewsDetail(final String newsId) {
        return sNewsService.getNewsDetail(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Map<String, NewsDetailBean>, Observable<NewsDetailBean>>() {
                    @Override
                    public Observable<NewsDetailBean> call(Map<String, NewsDetailBean> newsDetailMap) {
                        return Observable.just(newsDetailMap.get(newsId));
                    }
                });
    }

    /**
     * 获取图集
     * @param photoId 图集ID
     * @return
     */
    public static Observable<PhotoSetBean> getPhotoSet(String photoId) {
        return sNewsService.getPhotoSet(StringUtils.clipPhotoSetId(photoId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取图片列表
     * @return
     */
    public static Observable<List<PhotoBean>> getPhotoList() {
        return sNewsService.getPhotoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取更多图片列表
     * @return
     */
    public static Observable<List<PhotoBean>> getPhotoMoreList(String setId) {
        return sNewsService.getPhotoMoreList(setId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取美女图片
     * 注: 因为网易这个原接口参数一大堆，我只传了部分参数，返回的数据会出现图片重复的情况，请不要在意这个问题- -
     * @return
     */
    public static Observable<List<BeautyPhotoBean>> getBeautyPhoto() {
        sBeautyPage = 0;
        return sNewsService.getBeautyPhoto(sBeautyPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapPhotos());
    }

    /**
     * 获取更多美女图片
     * 注: 因为网易这个原接口参数一大堆，我只传了部分参数，返回的数据会出现图片重复的情况，请不要在意这个问题- -
     * @return
     */
    public static Observable<List<BeautyPhotoBean>> getMoreBeautyPhoto() {
        sBeautyPage += INCREASE_PAGE;
        return sNewsService.getBeautyPhoto(sBeautyPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapPhotos());
    }

    /**
     * 获取福利图片
     * @return
     */
    public static Observable<WelfarePhotoBean> getWelfarePhoto() {
        sWelfarePage = 1;
        return sWelfareService.getWelfarePhoto(sWelfarePage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapWelfarePhotos());
    }

    /**
     * 获取福利图片
     * @return
     */
    public static Observable<WelfarePhotoBean> getMoreWelfarePhoto() {
        sWelfarePage += 1;
        return sWelfareService.getWelfarePhoto(sWelfarePage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapWelfarePhotos());
    }

    /**
     * 获取视频列表
     * @return
     */
    public static Observable<List<VideoBean>> getVideoList(String videoId) {
        synchronized (key) {
            sNewsPage.put(videoId.hashCode(), 0);
        }
        return sNewsService.getVideoList(videoId, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapVideo(videoId));
    }

    /**
     * 获取下一页视频列表
     * @return
     */
    public static Observable<List<VideoBean>> getVideoListNext(String videoId) {
        int page;
        synchronized (key) {
            Integer prePage = sNewsPage.get(videoId.hashCode());
            if (prePage == null) {
                page = 0;
            } else {
                page = prePage + INCREASE_PAGE / 2;
            }
            sNewsPage.put(videoId.hashCode(), page);
        }
        return sNewsService.getVideoList(videoId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapVideo(videoId));
    }

    /******************************************* 转换器 **********************************************/

    /**
     * 类型转换
     * @param typeStr 新闻类型
     * @return
     */
    private static Func1<Map<String, List<NewsBean>>, Observable<NewsBean>> _flatMapNews(final String typeStr) {
        return new Func1<Map<String, List<NewsBean>>, Observable<NewsBean>>() {
            @Override
            public Observable<NewsBean> call(Map<String, List<NewsBean>> newsListMap) {
                return Observable.from(newsListMap.get(typeStr));
            }
        };
    }

    /**
     * 类型转换
     * @param typeStr 视频类型
     * @return
     */
    private static Func1<Map<String, List<VideoBean>>, Observable<List<VideoBean>>> _flatMapVideo(final String typeStr) {
        return new Func1<Map<String, List<VideoBean>>, Observable<List<VideoBean>>>() {
            @Override
            public Observable<List<VideoBean>> call(Map<String, List<VideoBean>> newsListMap) {
                return Observable.just(newsListMap.get(typeStr));
            }
        };
    }

    /**
     * 类型转换
     * @param specialId 专题id
     * @return
     */
    private static Func1<Map<String, SpecialBean>, Observable<SpecialBean>> _flatMapSpecial(final String specialId) {
        return new Func1<Map<String, SpecialBean>, Observable<SpecialBean>>() {
            @Override
            public Observable<SpecialBean> call(Map<String, SpecialBean> specialMap) {
                return Observable.just(specialMap.get(specialId));
            }
        };
    }

    /**
     * 类型转换
     * @return
     */
    private static Func1<Map<String, List<BeautyPhotoBean>>, Observable<List<BeautyPhotoBean>>> _flatMapPhotos() {
        return new Func1<Map<String, List<BeautyPhotoBean>>, Observable<List<BeautyPhotoBean>>>() {
            @Override
            public Observable<List<BeautyPhotoBean>> call(Map<String, List<BeautyPhotoBean>> newsListMap) {
                return Observable.just(newsListMap.get("美女"));
            }
        };
    }

    /**
     * 类型转换
     * @return
     */
    private static Func1<WelfarePhotoList, Observable<WelfarePhotoBean>> _flatMapWelfarePhotos() {
        return new Func1<WelfarePhotoList, Observable<WelfarePhotoBean>>() {
            @Override
            public Observable<WelfarePhotoBean> call(WelfarePhotoList welfarePhotoList) {
                Logger.w(""+welfarePhotoList.getResults().size());
                if (welfarePhotoList.getResults().size() == 0) {
                    return Observable.empty();
                }
                return Observable.from(welfarePhotoList.getResults());
            }
        };
    }
}
