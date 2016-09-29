package com.dl7.myapp.module.setting;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.orhanobut.logger.Logger;

/**
 * Created by long on 2016/9/29.
 */

public class SavePathPreference extends Preference {


    public SavePathPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SavePathPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SavePathPreference(Context context) {
        super(context);
    }

    @Override
    protected void onClick() {
//        super.onClick();
        Logger.w("onClick");
    }
}
