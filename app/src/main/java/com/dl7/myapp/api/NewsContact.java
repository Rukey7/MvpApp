package com.dl7.myapp.api;

/**
 * Created by Rukey7 on 2016/8/22.
 * 常亮
 */
public final class NewsContact {

    private NewsContact() {
        throw new RuntimeException("NewsContact cannot be initialized!");
    }

    // 头条
    private static final String NEWS_HEAD_LINE_STR = "T1348647909107";
    // 精选
    private static final String NEWS_BEST_STR = "T1467284926140";
    // 娱乐
    private static final String NEWS_ENTERTAINMENT_STR = "T1348648517839";
    // 体育
    private static final String NEWS_SPORT_STR = "T1348649079062";


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
}
