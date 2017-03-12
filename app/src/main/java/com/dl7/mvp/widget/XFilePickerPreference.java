package com.dl7.mvp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.dl7.mvp.utils.SDCardUtils;
import com.dl7.mvp.utils.StringUtils;
import com.github.angads25.filepicker.view.FilePickerPreference;

import java.io.File;

/**
 * Created by long on 2016/9/30.
 * 对 FilePickerPreference 做一些修改
 */
public class XFilePickerPreference extends FilePickerPreference {

    public XFilePickerPreference(Context context) {
        super(context);
    }

    public XFilePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XFilePickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        String dir = a.getString(index);
        File rootDirectory = SDCardUtils.getRootDirectory();
        if (rootDirectory != null && dir != null) {
            File defaultDir = new File(rootDirectory, dir);
            if (!defaultDir.exists()) {
                defaultDir.mkdirs();
            } else if (!defaultDir.isDirectory()) {
                defaultDir.delete();
                defaultDir.mkdirs();
            }
            return defaultDir.getAbsolutePath();
        }
        return super.onGetDefaultValue(a, index);
    }

    @Override
    public void onSelectedFilePaths(String[] files) {
        if (files == null || files.length == 0) {
            return;
        }
        // 这里改成只取第一项
        String dFiles = StringUtils.replaceFilePath(files[0]);
        if (isPersistent()) {
            persistString(dFiles);
        }
        try {
            getOnPreferenceChangeListener().onPreferenceChange(this, dFiles);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (defaultValue != null && defaultValue instanceof String) {
            if (isPersistent()) {
                persistString(defaultValue.toString());
            }
        } else {
            super.onSetInitialValue(restorePersistedValue, defaultValue);
        }
    }


}
