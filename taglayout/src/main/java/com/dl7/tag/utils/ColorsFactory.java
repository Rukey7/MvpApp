package com.dl7.tag.utils;

import android.support.annotation.ColorInt;

import java.util.Random;

/**
 * Created by long on 2016/12/5.
 * 随机颜色生成器
 */
public final class ColorsFactory {

    private static final
    @ColorInt
    int[] COLORS = new int[]{
            0xe51c23, 0xe91e63, 0x9c27b0, 0x673ab7,
            0x3f51b5, 0x5677fc, 0x03a9f4, 0x00bcd4,
            0x009688, 0x259b24, 0x8bc34a, 0xcddc39,
            0xffc107, 0xff9800, 0xff5722, 0x795548, 0x9e9e9e, 0x607d8b,
    };
    private static Random sRandom = new Random();

    private ColorsFactory() {
        throw new AssertionError();
    }

    @ColorInt
    public static int[] provideColor() {
        int index = sRandom.nextInt(COLORS.length);
        int[] colors = new int[2];
        colors[0] = COLORS[index] + 0xff000000;
        colors[1] = COLORS[index] + 0x88000000;
        return colors;
    }

}
