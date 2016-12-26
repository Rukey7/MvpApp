/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dl7.player.media;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IRenderView {
    /**
     * 视频显示模式：View 是视频画面能显示的控件
     * 0 -> 填满View一边，另一边按比例缩小，整个视频显示在View里面
     * 1 -> 整个填满View，可能有一边会被裁剪一部分画面
     * 2 -> 根据原始视频大小来测量，不会超出View范围
     * 3 -> 按View的原始测量值来设定
     * 4 -> 按16：9的比例来处理，不会超出View范围
     * 5 -> 按4：3的比例来处理，不会超出View范围
     */
    int AR_ASPECT_FIT_PARENT = 0; // without clip
    int AR_ASPECT_FILL_PARENT = 1; // may clip
    int AR_ASPECT_WRAP_CONTENT = 2;
    int AR_MATCH_PARENT = 3;
    int AR_16_9_FIT_PARENT = 4;
    int AR_4_3_FIT_PARENT = 5;

    View getView();

    boolean shouldWaitForResize();

    void setVideoSize(int videoWidth, int videoHeight);

    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    void setVideoRotation(int degree);

    void setAspectRatio(int aspectRatio);

    void addRenderCallback(@NonNull IRenderCallback callback);

    void removeRenderCallback(@NonNull IRenderCallback callback);

    // add, 设置 Matrix，来做缩放操作
    void setTransform(Matrix transform);
    // add, 获取 Matrix，来做缩放操作
    Matrix getTransform();
    // add, 获取截图
    Bitmap getVideoScreenshot();

    interface ISurfaceHolder {
        void bindToMediaPlayer(IMediaPlayer mp);

        @NonNull
        IRenderView getRenderView();

        @Nullable
        SurfaceHolder getSurfaceHolder();

        @Nullable
        Surface openSurface();

        @Nullable
        SurfaceTexture getSurfaceTexture();
    }

    interface IRenderCallback {
        /**
         * @param holder
         * @param width  could be 0
         * @param height could be 0
         */
        void onSurfaceCreated(@NonNull ISurfaceHolder holder, int width, int height);

        /**
         * @param holder
         * @param format could be 0
         * @param width
         * @param height
         */
        void onSurfaceChanged(@NonNull ISurfaceHolder holder, int format, int width, int height);

        void onSurfaceDestroyed(@NonNull ISurfaceHolder holder);
    }
}
