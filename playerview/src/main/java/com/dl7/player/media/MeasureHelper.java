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

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * 测量帮助类
 */
public final class MeasureHelper {
    // 测量的视图
    private WeakReference<View> mWeakView;

    // 视频宽高
    private int mVideoWidth;
    private int mVideoHeight;
    // 视频宽高比采样率???
    private int mVideoSarNum;
    private int mVideoSarDen;
    // 视频旋转角度
    private int mVideoRotationDegree;
    // 测量宽高
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    // 视频显示模式
    private int mCurrentAspectRatio = IRenderView.AR_ASPECT_FIT_PARENT;

    public MeasureHelper(View view) {
        mWeakView = new WeakReference<View>(view);
    }

    public View getView() {
        if (mWeakView == null)
            return null;
        return mWeakView.get();
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        mVideoSarNum = videoSarNum;
        mVideoSarDen = videoSarDen;
    }

    public void setVideoRotation(int videoRotationDegree) {
        mVideoRotationDegree = videoRotationDegree;
    }

    /**
     * Must be called by View.onMeasure(int, int)
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    public void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mVideoRotationDegree == 90 || mVideoRotationDegree == 270) {
            // 如果旋转了90°或270°则宽高测量值进行互换
            int tempSpec = widthMeasureSpec;
            widthMeasureSpec  = heightMeasureSpec;
            heightMeasureSpec = tempSpec;
        }
        // 获取默认的测量宽高值
        int width = View.getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = View.getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mCurrentAspectRatio == IRenderView.AR_MATCH_PARENT) {
            // 在 AR_MATCH_PARENT 模式下直接用原始测量值
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        } else if (mVideoWidth > 0 && mVideoHeight > 0) {
            int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
            // modify，把&&操作符换为||
            if (widthSpecMode == View.MeasureSpec.AT_MOST || heightSpecMode == View.MeasureSpec.AT_MOST) {
                // 测量宽高比，对应的视图的宽高比
                float specAspectRatio = (float) widthSpecSize / (float) heightSpecSize;
                // 显示宽高比，要显示的视频宽高比
                float displayAspectRatio;
                // 这里计算显示宽高比
                switch (mCurrentAspectRatio) {
                    case IRenderView.AR_16_9_FIT_PARENT:
                        // 16：9
                        displayAspectRatio = 16.0f / 9.0f;
                        if (mVideoRotationDegree == 90 || mVideoRotationDegree == 270)
                            displayAspectRatio = 1.0f / displayAspectRatio;
                        break;
                    case IRenderView.AR_4_3_FIT_PARENT:
                        // 4：3
                        displayAspectRatio = 4.0f / 3.0f;
                        if (mVideoRotationDegree == 90 || mVideoRotationDegree == 270)
                            displayAspectRatio = 1.0f / displayAspectRatio;
                        break;
                    case IRenderView.AR_ASPECT_FIT_PARENT:
                    case IRenderView.AR_ASPECT_FILL_PARENT:
                    case IRenderView.AR_ASPECT_WRAP_CONTENT:
                    default:
                        // 按视频来源宽高比
                        displayAspectRatio = (float) mVideoWidth / (float) mVideoHeight;
                        if (mVideoSarNum > 0 && mVideoSarDen > 0)
                            displayAspectRatio = displayAspectRatio * mVideoSarNum / mVideoSarDen;
                        break;
                }
                // 是否要显示视频宽度比例较大
                boolean shouldBeWider = displayAspectRatio > specAspectRatio;
                // 这里确定最终宽高
                switch (mCurrentAspectRatio) {
                    case IRenderView.AR_ASPECT_FIT_PARENT:
                    case IRenderView.AR_16_9_FIT_PARENT:
                    case IRenderView.AR_4_3_FIT_PARENT:
                        if (shouldBeWider) {
                            // too wide, fix width；宽度比较大，固定宽度，使用测量宽度，按显示比例缩放高度
                            width = widthSpecSize;
                            height = (int) (width / displayAspectRatio);
                        } else {
                            // too high, fix height；高度比较大，固定高度，使用测量高度，按显示比例缩放宽度
                            height = heightSpecSize;
                            width = (int) (height * displayAspectRatio);
                        }
                        break;
                    case IRenderView.AR_ASPECT_FILL_PARENT: // 填充满控件模式
                        if (shouldBeWider) {
                            // not high enough, fix height；宽度比较大，固定高度，缩放宽度
                            height = heightSpecSize;
                            width = (int) (height * displayAspectRatio);
                        } else {
                            // not wide enough, fix width；高度比较大，固定宽度，缩放高度
                            width = widthSpecSize;
                            height = (int) (width / displayAspectRatio);
                        }
                        break;
                    case IRenderView.AR_ASPECT_WRAP_CONTENT:
                    default:
                        if (shouldBeWider) {
                            // too wide, fix width；和第一个类似，这里取 (mVideoWidth, widthSpecSize) 最小的值
                            width = Math.min(mVideoWidth, widthSpecSize);
                            height = (int) (width / displayAspectRatio);
                        } else {
                            // too high, fix height
                            height = Math.min(mVideoHeight, heightSpecSize);
                            width = (int) (height * displayAspectRatio);
                        }
                        break;
                }
            } else if (widthSpecMode == View.MeasureSpec.EXACTLY && heightSpecMode == View.MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                // 这里做的是缩小某一边的大小以达到和视频原始尺寸的比例
                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == View.MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints，不让高度超出测量高度
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == View.MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints，不让宽度超出测量宽度
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }

        mMeasuredWidth = width;
        mMeasuredHeight = height;
    }

    public int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    public void setAspectRatio(int aspectRatio) {
        mCurrentAspectRatio = aspectRatio;
    }

}
