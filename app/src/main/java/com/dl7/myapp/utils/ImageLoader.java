package com.dl7.myapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dl7.myapp.module.setting.SettingsFragment;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by long on 2016/8/23.
 * 图片加载帮助类
 */
public final class ImageLoader {

    private ImageLoader() {
        throw new RuntimeException("ImageLoader cannot be initialized!");
    }


    public static void loadFit(Context context, String url, ImageView view, int defaultResId) {
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(url).fitCenter().placeholder(defaultResId).into(view);
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId) {
        Glide.with(context).load(url).centerCrop().placeholder(defaultResId).into(view);
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int defaultResId) {
        Glide.with(context).load(url).fitCenter().placeholder(defaultResId).into(view);
    }

    /**
     * 带监听处理
     * @param context
     * @param url
     * @param view
     * @param listener
     */
    public static void loadFitCenter(Context context, String url, ImageView view, RequestListener listener) {
        Glide.with(context).load(url).fitCenter().listener(listener).into(view);
    }

    /**
     * 设置图片大小处理
     * @param context
     * @param url
     * @param view
     * @param defaultResId
     * @param width
     * @param height
     */
    public static void loadFitOverride(Context context, String url, ImageView view, int defaultResId,
                                       int width, int height) {
        Glide.with(context).load(url).fitCenter().override(width, height)
                .placeholder(defaultResId).into(view);
    }

    public static void downloadPhoto(final Context context, final String url) {
        String savePath = PreferencesUtils.getString(context, SettingsFragment.SAVE_PATH_KEY);
//        File file = new File(savePath, "123.jpg");
//        if (file.exists()) {
//            return;
//        }
        Observable.just(url)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String s) {
                        File file = null;
                        try {
                            file = Glide.with(context).load(url)
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return file;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        Logger.w("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(File file) {
                        Logger.w(file.getAbsolutePath());
                        Logger.e(PreferencesUtils.getString(context, SettingsFragment.SAVE_PATH_KEY));
                        FileUtils.copyFile(file.getPath(),
                                PreferencesUtils.getString(context, SettingsFragment.SAVE_PATH_KEY) + new Date().getTime() + ".jpg");
                    }
                });
    }
}
