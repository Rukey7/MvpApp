package com.dl7.tag.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Rukey7 on 2016/12/5.
 */

public final class BitmapUtils {

    private BitmapUtils() {
        throw new AssertionError();
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap 源Bitmap
     * @param w 宽
     * @param h 高
     * @return 目标Bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBitmap;
    }
}
