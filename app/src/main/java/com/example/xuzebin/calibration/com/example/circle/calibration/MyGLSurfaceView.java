package com.example.xuzebin.calibration.com.example.circle.calibration;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.xuzebin.calibration.GridRenderer;

/**
 * Created by xuzebin on 16/4/18.
 */
public class MyGLSurfaceView extends GLSurfaceView{
	CircleRenderer renderer;
	public MyGLSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

        setEGLContextClientVersion(2);

        renderer = new CircleRenderer(context);
        renderer.setRenderMode(CircleRenderer.RenderMode.DOUBLE);
        //-1 indicates grids' width and height will be set as equally as possible
        renderer.initGrids(2, 2);

        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

	}
}
