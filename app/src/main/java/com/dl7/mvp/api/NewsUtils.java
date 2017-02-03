package com.dl7.mvp.api;

import android.support.annotation.NonNull;

import com.dl7.mvp.api.bean.NewsInfo;

/**
 * Created by Rukey7 on 2016/8/22.
 * 工具类
 */
public final class NewsUtils {

    private NewsUtils() {
        throw new RuntimeException("NewsUtils cannot be initialized!");
    }

    // 新闻列表头部
    private static final int HAS_HEAD = 1;
    // 新闻ID长度
    private static final int NEWS_ID_LENGTH = 16;
    // 新闻ID前缀，eg：BV9KHEMS0001124J
    private static final String NEWS_ID_PREFIX = "BV";
    // 新闻ID后缀，eg：http://news.163.com/17/0116/16/CATR1P580001875N.html
    private static final String NEWS_ID_SUFFIX = ".html";

    private static final String NEWS_ITEM_SPECIAL = "special";
    private static final String NEWS_ITEM_PHOTO_SET = "photoset";


    /**
     * 判断是否为广告
     *
     * @param newsBean
     * @return
     */
    public static boolean isAbNews(@NonNull NewsInfo newsBean) {
        return (newsBean.getHasHead() == HAS_HEAD &&
                newsBean.getAds() != null && newsBean.getAds().size() > 1);
    }

    /**
     * 从超链接中取出新闻ID
     *
     * @param url url
     * @return 新闻ID
     */
    public static String clipNewsIdFromUrl(String url) {
        String newsId = null;
        int index = url.indexOf(NEWS_ID_PREFIX);
        if (index != -1) {
            newsId = url.substring(index, index + NEWS_ID_LENGTH);
        } else if (url.endsWith(NEWS_ID_SUFFIX)) {
            index = url.length() - NEWS_ID_SUFFIX.length() - NEWS_ID_LENGTH;
            newsId = url.substring(index, index + NEWS_ID_LENGTH);
        }
        return newsId;
    }

    /**
     * 判断新闻类型
     *
     * @param skipType
     * @return
     */
    public static boolean isNewsSpecial(String skipType) {
        return NEWS_ITEM_SPECIAL.equals(skipType);
    }

    public static boolean isNewsPhotoSet(String skipType) {
        return NEWS_ITEM_PHOTO_SET.equals(skipType);
    }
}
