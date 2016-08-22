package com.dl7.myapp.api;

import android.support.annotation.IntDef;
import android.util.Log;
import android.util.SparseArray;

import com.dl7.myapp.api.bean.NewsBean;
import com.orhanobut.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
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

    static final String HOST = "http://c.m.163.com/";

    private static IApiService mService;
    // 保存新闻列表的当前页数
    private static SparseArray<Integer> sNewsPage;
    private static final int INCREASE_PAGE = 20;


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
        sNewsPage = new SparseArray<>();
    }


    /**
     * 获取新闻列表
     * @return
     */
    public static void getNewsList(@NewsType int newsType) {
        sNewsPage.put(newsType, 0);
        Observable<Map<String, List<NewsBean>>> newsList;
        if (newsType == NEWS_HEAD_LINE) {
            newsList = mService.getNewsList("headline", NewsContact.convertNewsType(newsType), 0);
        } else {
            newsList = mService.getNewsList("list", NewsContact.convertNewsType(newsType), 0);
        }
        newsList.subscribeOn(Schedulers.io())

                .subscribe(new Subscriber<Map<String, List<NewsBean>>>() {
                    @Override
                    public void onCompleted() {
                        Log.w("RetrofitService", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.w(e.toString());
                    }

                    @Override
                    public void onNext(Map<String, List<NewsBean>> stringListMap) {
                        for (Map.Entry<String, List<NewsBean>> entry : stringListMap.entrySet()) {
                            Logger.w(entry.getKey());
                            Log.w("LogTAG", entry.getValue().toString());
                        }
                    }
                });
    }

    /**
     * 获取下一页新闻列表
     * @return
     */
    public static void getNewsListNext(@NewsType int newsType) {
        Integer prePage = sNewsPage.get(newsType);
        int page;
        if (prePage == null) {
            page = 0;
        } else {
            page = prePage + INCREASE_PAGE;
        }
        Log.e("LogTAG", ""+page);
        sNewsPage.put(newsType, page);

        Observable<Map<String, List<NewsBean>>> newsList;
        if (newsType == NEWS_HEAD_LINE) {
            newsList = mService.getNewsList("headline", NewsContact.convertNewsType(newsType), page);
        } else {
            newsList = mService.getNewsList("list", NewsContact.convertNewsType(newsType), page);
        }
        newsList.subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Map<String, List<NewsBean>>>() {
                    @Override
                    public void onCompleted() {
                        Log.w("RetrofitService", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.w(e.toString());
                    }

                    @Override
                    public void onNext(Map<String, List<NewsBean>> stringListMap) {
                        for (Map.Entry<String, List<NewsBean>> entry : stringListMap.entrySet()) {
                            Logger.w(entry.getKey());
                            Log.w("LogTAG", entry.getValue().toString());
                        }
                    }
                });
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NEWS_HEAD_LINE, NEWS_BEST, NEWS_ENTERTAINMENT, NEWS_SPORT})
    public @interface NewsType {
    }
}
