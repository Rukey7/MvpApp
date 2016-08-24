package com.dl7.myapp.utils;

import android.content.Context;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.dl7.myapp.api.bean.NewsBean;

/**
 * Created by long on 2016/8/24.
 * 滑动器帮助类
 */
public final class SliderHelper {

    private SliderHelper() {
        throw new RuntimeException("SliderHelper cannot be initialized!");
    }

    /**
     * 初始化广告滑动器
     * @param context
     * @param sliderLayout
     * @param newsBean
     */
    public static void initAdSlider(Context context, SliderLayout sliderLayout, NewsBean newsBean) {
        for (NewsBean.AdData adData : newsBean.getAds()) {
            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView.description(adData.getTitle())
                    .image(adData.getImgsrc())
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle().putString("extra", name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
    }
}
