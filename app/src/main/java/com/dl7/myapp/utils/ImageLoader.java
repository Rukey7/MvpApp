package com.dl7.myapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by long on 2016/8/23.
 * 图片加载帮助类
 */
public final class ImageLoader {

    private ImageLoader() {
        throw new RuntimeException("ImageLoader cannot be initialized!");
    }


    public static void loadFit(Context context, String url, ImageView view, int defaultResId) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).fit().placeholder(defaultResId).into(view);
        }
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId) {
        Picasso.with(context).load(url).centerCrop().placeholder(defaultResId).into(view);
    }
}
