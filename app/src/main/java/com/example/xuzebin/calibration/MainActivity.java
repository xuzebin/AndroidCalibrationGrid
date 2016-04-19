package com.example.xuzebin.calibration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    CaliGLSurfaceView caliGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//this works on elcipse
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//this works on android studio
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        caliGLSurfaceView = new CaliGLSurfaceView(this);

        setContentView(caliGLSurfaceView);
    }

}
