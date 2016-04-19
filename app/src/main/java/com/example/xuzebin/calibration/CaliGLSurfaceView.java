package com.example.xuzebin.calibration;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;

/**
 * Created by xuzebin on 16/4/18.
 */
public class CaliGLSurfaceView extends GLSurfaceView{
	GridRenderer renderer;
	public CaliGLSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

        setEGLContextClientVersion(2);

        renderer = new GridRenderer(context);
        renderer.setRenderMode(GridRenderer.RenderMode.DOUBLE);
        //-1 indicates grids' width and height will be set as equally as possible
        renderer.initGrids(18, -1);

        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
}
