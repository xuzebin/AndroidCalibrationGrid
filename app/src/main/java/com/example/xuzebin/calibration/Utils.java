package com.example.xuzebin.calibration;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by xuzebin on 16/4/19.
 */
public class Utils {

    /**
     * get the ratio of screen
     * @param context
     * @return
     */
    public static float getScreenRatio(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float ratio = (float) screenWidth / screenHeight;
        return ratio;
    }

}
