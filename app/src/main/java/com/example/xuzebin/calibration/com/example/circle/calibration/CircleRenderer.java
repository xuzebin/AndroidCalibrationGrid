package com.example.xuzebin.calibration.com.example.circle.calibration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.example.xuzebin.calibration.Color;
import com.example.xuzebin.calibration.Grid;
import com.example.xuzebin.calibration.R;
import com.example.xuzebin.calibration.ShaderHelper;
import com.example.xuzebin.calibration.TextureHelper;
import com.example.xuzebin.calibration.Utils;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
public class CircleRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "CircleRenderer";
    enum RenderMode {
        SINGLE,
        DOUBLE
    }
    Context mContext;
    int mProgramHandle;
    int aPositionHandle;
    int uColorHandle;

    int uMvpMatrixHandle;
    int uTextureHandle;
    int aTexCoordsHandle;

    int mWidth;
    int mHeight;

    Circle leftGrid;
    Circle rightGrid;

    Color mBkgColor;

    RenderMode mRenderMode;

    float[] mvpMatrix;

    int mTextureId;

    int centerProgramHandle;

	public CircleRenderer(Context context) {
		mContext = context;
        mRenderMode = RenderMode.DOUBLE;
        mBkgColor = new Color(0.0f, 0.0f, 0.0f);


//        getTextureLocations(mProgramHandle);






//        mTextureId = loadTexture("marker.jpg");

	}

    public CircleRenderer(Context context, RenderMode renderMode) {
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
//        if (colNum == -1) {
//            leftGrid = new Circle(rowNum, mRenderMode);
//        } else {
//            leftGrid = new Circle(rowNum, colNum, mRenderMode);
//        }
//
//        if (mRenderMode == RenderMode.DOUBLE) {
//            if (colNum == -1) {
//                rightGrid = new Circle(rowNum, mRenderMode);
//            } else {
//                rightGrid = new Circle(rowNum, colNum, mRenderMode);
//            }
//
//            rightGrid.setScreenRatio(screenRatio);
//        }
        leftGrid = new Circle();
        rightGrid = new Circle();

        leftGrid.setScreenRatio(screenRatio);

    }


	@Override
	public void onDrawFrame(GL10 arg0) {
		// TODO Auto-generated method stub
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (mRenderMode == RenderMode.DOUBLE) {
            GLES20.glViewport(0, 0, mWidth / 2, mHeight);

//            renderLine(leftGrid);
            renderTexture(leftGrid);

            renderCenterCross(leftGrid);


            GLES20.glViewport(mWidth / 2, 0, mWidth / 2, mHeight);

//            renderLine(rightGrid);
            renderTexture(rightGrid);
            renderCenterCross(rightGrid);

        } else if (mRenderMode == RenderMode.SINGLE) {
            GLES20.glViewport(0, 0, mWidth, mHeight);
//            renderCenterCross(leftGrid);
//            renderLine(leftGrid);
            renderTexture(leftGrid);
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
        mTextureId = TextureHelper.loadTextureBitmap(mContext, R.drawable.marker);
		initShaders(mContext);
	}

    public void initShaders(Context context) {
        mProgramHandle = ShaderHelper.initShadersAndProgram(context,
                R.raw.vertex_shader, R.raw.fragment_shader_bitmap);

        centerProgramHandle = ShaderHelper.initShadersAndProgram(context, R.raw.vertex_shader_center,
                R.raw.fragment_shader_simple);

//        GLES20.glUseProgram(mProgramHandle);
        getTextureLocations(mProgramHandle);
    }


    protected void getTextureLocations(int programHandle) {
//        uMvpMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        uTextureHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");

        aPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        aTexCoordsHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");


    }


    public void renderTexture(Circle circle) {
        GLES20.glUseProgram(mProgramHandle);

//        GLES20.glUniformMatrix4fv(uMvpMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(uTextureHandle, 0);



        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 2, GLES20.GL_FLOAT, false, 0, circle.getVerticeBuffer());


        GLES20.glEnableVertexAttribArray(aTexCoordsHandle);
        GLES20.glVertexAttribPointer(aTexCoordsHandle, 2, GLES20.GL_FLOAT, false, 0, circle.getTexCoordBuffer());


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(aPositionHandle);
        GLES20.glDisableVertexAttribArray(aTexCoordsHandle);
    }



    /**
     * Render the cross at the center
     * @param grid
     */
    public void renderCenterCross(Circle grid) {
        getTextureLocations(centerProgramHandle);
        GLES20.glUseProgram(centerProgramHandle);
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
