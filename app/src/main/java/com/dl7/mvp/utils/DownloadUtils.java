package com.dl7.mvp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.dl7.mvp.local.table.BeautyPhotoInfo;
import com.dl7.mvp.local.table.BeautyPhotoInfoDao;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by long on 2016/10/10.
 * 下载工具类
 */
public final class DownloadUtils {

    // 记录下载完的图片
    private static SparseBooleanArray sDlPhotos = new SparseBooleanArray();
    // 记录下载中的图片
    private static SparseBooleanArray sDoDlPhotos = new SparseBooleanArray();


    private DownloadUtils() {
        throw new RuntimeException("DownloadUtils cannot be initialized!");
    }

    /**
     * 先进行初始化，把之前下载的图片记录下来
     * @param dbDao
     */
    public static void init(BeautyPhotoInfoDao dbDao) {
        dbDao.queryBuilder().rx().list()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<BeautyPhotoInfo>, Observable<BeautyPhotoInfo>>() {
                    @Override
                    public Observable<BeautyPhotoInfo> call(List<BeautyPhotoInfo> photoList) {
                        return Observable.from(photoList);
                    }
                })
                .filter(new Func1<BeautyPhotoInfo, Boolean>() {
                    @Override
                    public Boolean call(BeautyPhotoInfo bean) {
                        return bean.isDownload();
                    }
                })
                .subscribe(new Action1<BeautyPhotoInfo>() {
                    @Override
                    public void call(BeautyPhotoInfo bean) {
                        sDlPhotos.put(bean.getImgsrc().hashCode(), true);
                    }
                });
    }

    /**
     * 图片下载
     * @param context
     * @param url
     */
    public static void downloadOrDeletePhoto(final Context context, final String url, final String id, final OnCompletedListener listener) {
        if (sDlPhotos.get(url.hashCode(), false)) {
            DialogHelper.deleteDialog(context, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FileUtils.deleteFile(PreferencesUtils.getSavePath(context) + File.separator + id + ".jpg");
                    listener.onDeleted(url);
                    delDownloadPhoto(url);
                }
            });
            return;
        }
        if (sDoDlPhotos.get(url.hashCode(), false)) {
            ToastUtils.showToast("正在下载...");
            return;
        }
        sDoDlPhotos.put(url.hashCode(), true);
        ToastUtils.showToast("正在下载...");
        Observable.just(url)
                // 通过URL下载图片，图片默认保存在 Glide 缓存里面
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        File file = null;
                        try {
                            file = Glide.with(context).load(url)
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        // 复制图片文件到指定路径，并改为 .jpg 后缀名
                        return FileUtils.copyFile(file.getPath(), PreferencesUtils.getSavePath(context) +
                                File.separator + "picture" + File.separator + id + ".jpg");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isCompleted) {
                        if (isCompleted) {
                            ToastUtils.showToast("下载完成");
                            sDlPhotos.put(url.hashCode(), true);
                            if (listener != null) {
                                listener.onCompleted(url);
                            }
                        } else {
                            ToastUtils.showToast("下载失败");
                        }
                        sDoDlPhotos.put(url.hashCode(), false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.e(throwable.toString());
                        ToastUtils.showToast("下载失败");
                        sDoDlPhotos.put(url.hashCode(), false);
                    }
                });
    }

    /**
     * 图片是否已下载
     * @param url
     * @return
     */
    public static boolean isPhotoDownloaded(String url) {
        return sDlPhotos.get(url.hashCode(), false);
    }

    /**
     * 清除下载记录
     * @param url
     */
    public static void delDownloadPhoto(String url) {
        sDlPhotos.put(url.hashCode(), false);
        sDoDlPhotos.put(url.hashCode(), false);
    }


    /**
     * 下载监听器
     */
    public interface OnCompletedListener {
        /**
         * 下载完成
         * @param url
         */
        void onCompleted(String url);

        /**
         * 删除
         * @param url
         */
        void onDeleted(String url);
    }
}
