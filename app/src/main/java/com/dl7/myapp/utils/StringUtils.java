package com.dl7.myapp.utils;

import android.text.TextUtils;

/**
 * Created by long on 2016/8/29.
 * 字符串工具
 */
public class StringUtils {

    private StringUtils() {
        throw new RuntimeException("StringUtils cannot be initialized!");
    }


    /**
     * 裁剪新闻的 Source 数据
     * @param source
     * @return
     */
    public static String clipNewsSource(String source) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }
        int i = source.indexOf("-");
        if (i != -1) {
            return source.substring(0, i);
        }
        return source;
    }

    /**
     * 裁剪图集ID
     * @param photoId
     * @return
     */
    public static String clipPhotoSetId(String photoId) {
        if (TextUtils.isEmpty(photoId)) {
            return photoId;
        }
        int i = photoId.indexOf("|");
        if (i > 4) {
            String result = photoId.replace('|', '/');
            return result.substring(i - 4);
        }
        return null;
    }
}
