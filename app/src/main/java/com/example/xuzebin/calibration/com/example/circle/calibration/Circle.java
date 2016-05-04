package com.example.xuzebin.calibration.com.example.circle.calibration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.xuzebin.calibration.Color;
import com.example.xuzebin.calibration.GridRenderer;
import com.example.xuzebin.calibration.Vec2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Create calibration grids' vertices, color, thickness
 * according to the given parameters.
 *
 * The created vertice buffer will be used in shader for rendering.
 *
 * Created by xuzebin on 16/4/18.
 */
public class Circle {
	private final static int FLOAT_SIZE_BYTES = 4;
	public final int DIMENSION = 2;
	float[] lineVertices;
	float[] gridCenterVertices;
	private FloatBuffer lineBuffer;
	private FloatBuffer gridCenterBuffer;
	float ctrLengthX;
	float ctrLengthY;
	float mCtrThickness;
	Color mCrossColor;
	Color mLineColor;
	int mRowNum;
	int mColNum;
	float mLineThickness;
	Vec2 mCenterCross;
	float mScreenRatio;

	private FloatBuffer verBuffer;
	private FloatBuffer texCoordBuffer;
	float[] vertice = new float[] {
//			-1.0f, -1.0f,
//			-1.0f, 1.0f,
//			1.0f, -1.0f,
//			1.0f, 1.0f
//			-1.0f, 1.0f,
//			-1.0f, -1.0f,
//			1.0f, -1.0f,
//			1.0f, 1.0f

//			-1.0f, 1.0f,
//			1.0f, 1.0f,
//			-1.0f, -1.0f,
//			1.0f, -1.0f

			-1.0f, 0.9f,
			1.0f, 0.9f,
			-1.0f, -0.9f,
			1.0f, -0.9f


	};

	float[] texCoord = new float[] {
//			0.0f, 0.0f,
//			0.0f, 1.0f,
//			1.0f, 1.0f,
//			1.0f, 0.0f
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f
	};



	public Circle() {

		verBuffer = ByteBuffer.allocateDirect(vertice.length * FLOAT_SIZE_BYTES).
				order(ByteOrder.nativeOrder()).
				asFloatBuffer();
		verBuffer.put(vertice);
		verBuffer.position(0);

		texCoordBuffer = ByteBuffer.allocateDirect(texCoord.length * FLOAT_SIZE_BYTES).
				order(ByteOrder.nativeOrder()).
				asFloatBuffer();
		texCoordBuffer.put(texCoord);
		texCoordBuffer.position(0);


		mCenterCross = new Vec2(0, 0);
		mCrossColor = new Color(1f, 0f, 0f);
		mCtrThickness = 12f;
		ctrLengthX = 0.09f;
		ctrLengthY = 0.08f;

		gridCenterVertices = new float[] {
				//row
				mCenterCross.x - ctrLengthX, //x coordinate
				mCenterCross.y,//y coordinate
				mCenterCross.x + ctrLengthX,
				mCenterCross.y,

				//column
				mCenterCross.x,
				mCenterCross.y - ctrLengthY,
				mCenterCross.x,
				mCenterCross.y + ctrLengthY
		};

		gridCenterBuffer = ByteBuffer.allocateDirect(gridCenterVertices.length * FLOAT_SIZE_BYTES).
				order(ByteOrder.nativeOrder()).
				asFloatBuffer();
		gridCenterBuffer.put(gridCenterVertices);
		gridCenterBuffer.position(0);


	}


	public FloatBuffer getVerticeBuffer() {
		return verBuffer;
	}
	public FloatBuffer getTexCoordBuffer() {
		return texCoordBuffer;
	}




	public void setGridNum(int rowNum, int colNum) {
		mRowNum = rowNum;
		mColNum = colNum;
	}
	public void setLineColor(Color color) {
		mLineColor = color;
	}
	public void setLineThickness(float lineThickness) {
		mLineThickness = lineThickness;
	}
	public void setCenterCross(Vec2 centerCross) {
		mCenterCross = centerCross;
	}
	public void setCtrThickness(float ctrThickness) {
		mCtrThickness = ctrThickness;
	}
	public void setCrossColor(Color color) {
		mCrossColor = color;
	}
	public Color getLineColor() {
		return mLineColor;
	}
	public Vec2 getCenterCross() {
		return mCenterCross;
	}
	public float getLineThickness() {
		return mLineThickness;
	}
	public float getCtrThickness() {
		return mCtrThickness;
	}
	public Color getCrossColor() {
		return mCrossColor;
	}
	
	public void initBuffers() {
	    //create grid center
	    gridCenterVertices = new float[] {
	    	//row
				mCenterCross.x - ctrLengthX, //x coordinate
				mCenterCross.y,//y coordinate
				mCenterCross.x + ctrLengthX,
				mCenterCross.y,
	    	
	    	//column
				mCenterCross.x,
				mCenterCross.y - ctrLengthY,
				mCenterCross.x,
				mCenterCross.y + ctrLengthY
	    };
	    
	    gridCenterBuffer = ByteBuffer.allocateDirect(gridCenterVertices.length * FLOAT_SIZE_BYTES).
		order(ByteOrder.nativeOrder()).
		asFloatBuffer();
	    gridCenterBuffer.put(gridCenterVertices);
	    gridCenterBuffer.position(0);







//		verBuffer = ByteBuffer.allocateDirect(vertice.length * FLOAT_SIZE_BYTES).
//				order(ByteOrder.nativeOrder()).
//				asFloatBuffer();
//		verBuffer.put(vertice);
//		verBuffer.position(0);
	}
	
	 public FloatBuffer getLineBuffer() {
		 return lineBuffer;
	 }
	 public int getLinePointCount() {
		 return lineVertices.length / DIMENSION;
	 }
	 public FloatBuffer getGridCtrBuffer() {
		 return gridCenterBuffer;
	 }
	 public int getGridCtrPointCount() {
		 return gridCenterVertices.length / DIMENSION;
	 }

	public void setScreenRatio(float ratio) {
		mScreenRatio = ratio;
	}
	  
	 public FloatBuffer getVerBuffer() { return verBuffer;}
	public int getVerPointCount() { return vertice.length / DIMENSION;}




}
