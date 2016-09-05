package com.dl7.myapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
//            Picasso.with(context).load(url).fit().placeholder(defaultResId).into(view);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(url).fitCenter().placeholder(defaultResId).crossFade().into(view);
        }
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId) {
//        Picasso.with(context).load(url).centerInside().placeholder(defaultResId).into(view);
        Glide.with(context).load(url).centerCrop().crossFade().placeholder(defaultResId).into(view);
    }

    public static void loadFitOverride(Context context, String url, ImageView view, int defaultResId,
                                       int width, int height) {
        if (!TextUtils.isEmpty(url)) {
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(url).fitCenter().override(width, height)
                    .placeholder(defaultResId).crossFade().into(view);
        }
    }
}
