package com.dl7.player.utils;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Method;

/**
 * Created by long on 2016/10/10.
 * 导航栏工具类
 */
public final class NavUtils {

    private NavUtils() {
        throw new RuntimeException("NavUtils cannot be initialized!");
    }

    /**
     * 获取底部导航栏高度
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Context activity) {
        if (!checkDeviceHasNavigationBar(activity)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 检测是否有虚拟键
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }
}
