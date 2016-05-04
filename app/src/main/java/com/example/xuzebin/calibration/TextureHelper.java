package com.example.xuzebin.calibration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xuzebin on 16/5/3.
 */
public class TextureHelper {
    private static final String TAG = "TextureHelper";
    public static int loadTextureBitmap(final Context context, final int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;    // No pre-scaling

        // Read in the resource
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);


        Log.i(TAG, "BITMAP widthxheight= " + bitmap.getWidth() + "x" + bitmap.getHeight());
        int[] textureId = new int[1];
        GLES20.glGenTextures(1, textureId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();

        Log.i(TAG, "TEXTUREHELPER: textureId= " + textureId[0]);

        if (textureId[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        return textureId[0];
    }
}
