package com.dl7.player.utils;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by Rukey7 on 2016/11/21.
 */

public final class MotionEventUtils {

    public static final int FINGER_FLAG_1 = 601;
    public static final int FINGER_FLAG_2 = 602;
    public static final int FINGER_FLAG_3 = 603;

    private MotionEventUtils() {
        throw new AssertionError();
    }


    /**
     * Determine the space between the first two fingers
     */
    public static float calcSpacing(MotionEvent event, int index1, int index2) {
        float x = event.getX(index1) - event.getX(index2);
        float y = event.getY(index1) - event.getY(index2);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Determine the space between the two fingers
     * @param event
     * @param fingerFlag
     * @return
     */
    public static float calcSpacing(MotionEvent event, int fingerFlag) {
        float x, y;
        if (FINGER_FLAG_1 == fingerFlag) {
            x = (event.getX(0) + event.getX(1)) / 2 - event.getX(2);
            y = (event.getY(0) + event.getY(1)) / 2 - event.getY(2);
        } else if (FINGER_FLAG_2 == fingerFlag) {
            x = (event.getX(0) + event.getX(2)) / 2 - event.getX(1);
            y = (event.getY(0) + event.getY(2)) / 2 - event.getY(1);
        } else if (FINGER_FLAG_3 == fingerFlag) {
            x = (event.getX(2) + event.getX(1)) / 2 - event.getX(0);
            y = (event.getY(2) + event.getY(1)) / 2 - event.getY(0);
        } else {
            x = (event.getX(0) - event.getX(1));
            y = (event.getY(0) - event.getY(1));
        }
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of two pointers
     */
    public static void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1) + event.getX(2);
        float y = event.getY(0) + event.getY(1) + event.getY(2);
        point.set(x / 3, y / 3);
    }

    /**
     * 计算靠的最近的两指
     * @param event
     * @return
     */
    public static int calcFingerFlag(MotionEvent event) {
        float space1 = calcSpacing(event, 0, 1);
        float space2 = calcSpacing(event, 0, 2);
        float space3 = calcSpacing(event, 2, 1);
        float minSpace = Math.min(space1, Math.min(space2, space3));
        if (minSpace == space1) {
            return FINGER_FLAG_1;
        } else if (minSpace == space2) {
            return FINGER_FLAG_2;
        } else if (minSpace == space3) {
            return FINGER_FLAG_3;
        } else {
            return -1;
        }
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    public static float rotation(MotionEvent event, int fingerFlag) {
        double delta_x, delta_y;
        if (FINGER_FLAG_1 == fingerFlag) {
            delta_x = (event.getX(0) + event.getX(1)) / 2 - event.getX(2);
            delta_y = (event.getY(0) + event.getY(1)) / 2 - event.getY(2);
        } else if (FINGER_FLAG_2 == fingerFlag) {
            delta_x = (event.getX(0) + event.getX(2)) / 2 - event.getX(1);
            delta_y = (event.getY(0) + event.getY(2)) / 2 - event.getY(1);
        } else if (FINGER_FLAG_3 == fingerFlag) {
            delta_x = (event.getX(2) + event.getX(1)) / 2 - event.getX(0);
            delta_y = (event.getY(2) + event.getY(1)) / 2 - event.getY(0);
        } else {
            delta_x = (event.getX(0) - event.getX(1));
            delta_y = (event.getY(0) - event.getY(1));
        }
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}
