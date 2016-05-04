package com.example.xuzebin.calibration;

import android.content.Context;
import android.opengl.GLES20;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by xuzebin on 16/4/19.
 */
public class Utils {
    private static final String TAG = "Utils";
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

    public static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }
}



