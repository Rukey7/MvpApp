package com.dl7.myapp.api;

import android.support.annotation.IntDef;
import android.util.SparseArray;

import com.dl7.myapp.AndroidApplication;
import com.dl7.myapp.api.bean.NewsBean;
import com.dl7.myapp.api.bean.NewsDetailBean;
import com.dl7.myapp.api.bean.PhotoSetBean;
import com.dl7.myapp.api.bean.SpecialBean;
import com.dl7.myapp.utils.NetUtil;
import com.dl7.myapp.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

    // 头条
    public static final int NEWS_HEAD_LINE = 0;
    // 精选
    public static final int NEWS_BEST = 1;
    // 娱乐
    public static final int NEWS_ENTERTAINMENT = 2;
    // 体育
    public static final int NEWS_SPORT = 3;

    //设缓存有效期为两天
    static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";

    static final String HOST = "http://c.3g.163.com/";

    private static IApiService mService;
    // 同步锁，处理页码 sNewsPage
    private static Object key = new Object();
    // 保存新闻列表的当前页码
    private static SparseArray<Integer> sNewsPage;
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
                .baseUrl(HOST)
                .build();
        mService = retrofit.create(IApiService.class);
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
    public static Observable<NewsBean> getNewsList(@NewsType int newsType) {
        synchronized (key) {
            sNewsPage.put(newsType, 0);
        }
        Observable<Map<String, List<NewsBean>>> newsList;
        if (newsType == NEWS_HEAD_LINE) {
            newsList = mService.getNewsList("headline", NewsUtils.convertNewsType(newsType), 0);
        } else {
            newsList = mService.getNewsList("list", NewsUtils.convertNewsType(newsType), 0);
        }
        return newsList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapNews(NewsUtils.convertNewsType(newsType)));
    }

    /**
     * 获取下一页新闻列表
     * @return
     */
    public static Observable<NewsBean> getNewsListNext(@NewsType int newsType) {
        int page;
        synchronized (key) {
            Integer prePage = sNewsPage.get(newsType);
            if (prePage == null) {
                page = 0;
            } else {
                page = prePage + INCREASE_PAGE;
            }
            sNewsPage.put(newsType, page);
        }

        Observable<Map<String, List<NewsBean>>> newsList;
        if (newsType == NEWS_HEAD_LINE) {
            newsList = mService.getNewsList("headline", NewsUtils.convertNewsType(newsType), page);
        } else {
            newsList = mService.getNewsList("list", NewsUtils.convertNewsType(newsType), page);
        }
        return newsList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapNews(NewsUtils.convertNewsType(newsType)));
    }

    /**
     * 获取专题数据
     * @param specialId
     * @return
     */
    public static Observable<SpecialBean> getSpecial(String specialId) {
        return mService.getSpecial(specialId)
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
        return mService.getNewsDetail(newsId)
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
        return mService.getPhotoSet(StringUtils.clipPhotoSetId(photoId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

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

    /***************************************************************************************/

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NEWS_HEAD_LINE, NEWS_BEST, NEWS_ENTERTAINMENT, NEWS_SPORT})
    public @interface NewsType {
    }
}
