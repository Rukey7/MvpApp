package com.dl7.myapp.api;

import android.support.annotation.NonNull;

import com.dl7.myapp.api.bean.NewsBean;

/**
 * Created by Rukey7 on 2016/8/22.
 * 工具类
 */
public final class NewsUtils {

    private NewsUtils() {
        throw new RuntimeException("NewsUtils cannot be initialized!");
    }

    // 头条
    private static final String NEWS_HEAD_LINE_STR = "T1348647909107";
    // 精选
    private static final String NEWS_BEST_STR = "T1467284926140";
    // 娱乐
    private static final String NEWS_ENTERTAINMENT_STR = "T1348648517839";
    // 体育
    private static final String NEWS_SPORT_STR = "T1348649079062";

    // 新闻列表头部
    private static final int HAS_HEAD = 1;
    // 新闻ID长度
    private static final int NEWS_ID_LENGHT = 16;
    // 新闻ID前缀，eg：BV9KHEMS0001124J
    private static final String NEWS_ID_PREFIX = "BV";

    /**
     * 类型转换为字符串
     * @param type  newsID
     * @return
     */
    static String convertNewsType(@RetrofitService.NewsType int type) {
        switch (type) {
            case RetrofitService.NEWS_HEAD_LINE:
                return NEWS_HEAD_LINE_STR;
            case RetrofitService.NEWS_BEST:
                return NEWS_BEST_STR;
            case RetrofitService.NEWS_ENTERTAINMENT:
                return NEWS_ENTERTAINMENT_STR;
            case RetrofitService.NEWS_SPORT:
                return NEWS_SPORT_STR;
            default:
                break;
        }

        return null;
    }

    /**
     * 判断是否为广告
     * @param newsBean
     * @return
     */
    public static boolean isAbNews(@NonNull NewsBean newsBean) {
        return (newsBean.getHasHead() == HAS_HEAD &&
                newsBean.getAds() != null && newsBean.getAds().size() > 1);
    }

    /**
     * 从超链接中取出新闻ID
     * @param url url
     * @return  新闻ID
     */
    public static String clipNewsIdFromUrl(String url) {
        String newsId = null;
        int index = url.indexOf(NEWS_ID_PREFIX);
        if (index != -1) {
            newsId = url.substring(index, index + NEWS_ID_LENGHT);
        }
        return newsId;
    }
}
