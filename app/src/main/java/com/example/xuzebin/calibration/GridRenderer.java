package com.example.xuzebin.calibration;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * Render grids for calibration using OpenGL ES 2.0.
 *
 * Steps to use:
 * 1. Create a GridRenderer object.
 * 2. Set rendering mode by calling setRenderMode.
 * 3. Create one/two Grid objects(s) based on
 *     rendering mode by calling initGrids.
 * 4. In the (derived) class of GLSurfaceView, call setRenderer to use this renderer.
 * 5. Once the surface is created, initShaders will be called to initialize shader program.
 * 6.
 * @author xuzebin zebinxu7@gmail.com
 */
public class GridRenderer implements GLSurfaceView.Renderer {
    enum RenderMode {
        SINGLE,
        DOUBLE
    }
    Context mContext;
    int mProgramHandle;
    int aPositionHandle;
    int uColorHandle;
	   
    int mWidth;
    int mHeight;
	   
    Grid leftGrid;
    Grid rightGrid;

    Color mBkgColor;

    RenderMode mRenderMode;

	public GridRenderer(Context context) {
		mContext = context;
        mRenderMode = RenderMode.DOUBLE;
        mBkgColor = new Color(0.0f, 0.0f, 0.0f);
	}

    public GridRenderer(Context context, RenderMode renderMode) {
        mContext = context;
        mRenderMode = renderMode;
        mBkgColor = new Color(0.0f, 0.0f, 0.0f);
    }

    /**
     * Initialize grids before rendering
     * @param rowNum
     * @param colNum
     */
    public void initGrids(int rowNum, int colNum) {
        float screenRatio = Utils.getScreenRatio(mContext);
        if (colNum == -1) {
            leftGrid = new Grid(rowNum, mRenderMode);
        } else {
            leftGrid = new Grid(rowNum, colNum, mRenderMode);
        }

        if (mRenderMode == RenderMode.DOUBLE) {
            if (colNum == -1) {
                rightGrid = new Grid(rowNum, mRenderMode);
            } else {
                rightGrid = new Grid(rowNum, colNum, mRenderMode);
            }

            rightGrid.setScreenRatio(screenRatio);
        }

        leftGrid.setScreenRatio(screenRatio);

    }

	   
	@Override
	public void onDrawFrame(GL10 arg0) {
		// TODO Auto-generated method stub
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (mRenderMode == RenderMode.DOUBLE) {
            GLES20.glViewport(0, 0, mWidth / 2, mHeight);
            renderCenterCross(leftGrid);
            renderLine(leftGrid);


            GLES20.glViewport(mWidth / 2, 0, mWidth / 2, mHeight);
            renderCenterCross(rightGrid);
            renderLine(rightGrid);

        } else if (mRenderMode == RenderMode.SINGLE) {
            GLES20.glViewport(0, 0, mWidth, mHeight);
            renderCenterCross(leftGrid);
            renderLine(leftGrid);
        }
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {	
		mWidth = width;
		mHeight = height;
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		GLES20.glClearColor(mBkgColor.r, mBkgColor.g, mBkgColor.b, 1.0f);
		initShaders(mContext);
	}

    public void initShaders(Context context) {
        mProgramHandle = ShaderHelper.initShadersAndProgram(context,
                R.raw.vertex_shader, R.raw.fragment_shader);

        GLES20.glUseProgram(mProgramHandle);
        getLocations(mProgramHandle);
    }
    protected void getLocations(int programHandle) {
        //get uniform locations
    	uColorHandle = GLES20.glGetUniformLocation(programHandle, "u_Color");

        //get attribute locations
        aPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");

        GLES20.glEnableVertexAttribArray(aPositionHandle);
    }

    /**
     * Render the lines of grids
     * @param grid the grids object containing grid's information
     */
    public void renderLine(Grid grid) {
        GLES20.glUseProgram(mProgramHandle);
        GLES20.glLineWidth(grid.getLineThickness());

        Color linecolor = grid.getLineColor();
        GLES20.glUniform3f(uColorHandle, linecolor.r, linecolor.g, linecolor.b);
        
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, grid.DIMENSION, GLES20.GL_FLOAT, false, 0, grid.getLineBuffer());

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, grid.getLinePointCount());

        GLES20.glDisableVertexAttribArray(aPositionHandle);
        GLES20.glLineWidth(1);
    }

    /**
     * Render the cross at the center
     * @param grid
     */
    public void renderCenterCross(Grid grid) {
        GLES20.glUseProgram(mProgramHandle);
        GLES20.glLineWidth(grid.getCtrThickness());

        Color crossColor = grid.getCrossColor();
        GLES20.glUniform3f(uColorHandle, crossColor.r, crossColor.g, crossColor.b);

        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, grid.DIMENSION, GLES20.GL_FLOAT, false, 0, grid.getGridCtrBuffer());

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, grid.getGridCtrPointCount());

        GLES20.glDisableVertexAttribArray(aPositionHandle);
        GLES20.glLineWidth(1);
    }

    public void setBackGroundColor(Color bkgColor) {
        mBkgColor = bkgColor;
    }

    /**
     * Must be called before initGrids method.
     * Default rendering mode is DOUBLE
     * @param mode the mode to render
     */
    public void setRenderMode(RenderMode mode) {
        mRenderMode = mode;
    }

}
