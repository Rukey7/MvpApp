package com.dl7.mvp.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long on 2016/9/22.
 * Activity 管理器
 */
public final class ActivityCollector {

    private ActivityCollector() {
        throw new RuntimeException("ActivityCollector cannot be initialized!");
    }

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
