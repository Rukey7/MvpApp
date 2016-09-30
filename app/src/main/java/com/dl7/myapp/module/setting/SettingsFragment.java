package com.dl7.myapp.module.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.dl7.myapp.R;
import com.dl7.myapp.views.XFilePickerPreference;
import com.orhanobut.logger.Logger;

/**
 * Created by long on 2016/9/29.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String NO_IMAGE_KEY = "setting_no_image";
    public static final String SAVE_PATH_KEY = "setting_save_path";

    private XFilePickerPreference mFilePickerPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_settings);
        _initPreferences();
    }

    private void _initPreferences() {
        mFilePickerPreference = (XFilePickerPreference) findPreference(SAVE_PATH_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        mFilePickerPreference.setSummary(sharedPreferences.getString(SAVE_PATH_KEY, "/sdcard"));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(NO_IMAGE_KEY)) {
            Logger.w(sharedPreferences.getBoolean(NO_IMAGE_KEY, false)+"");
        } else if (key.equals(SAVE_PATH_KEY)) {
            String path = sharedPreferences.getString(SAVE_PATH_KEY, "/sdcard");
            mFilePickerPreference.setSummary(path);
        }
    }

}
