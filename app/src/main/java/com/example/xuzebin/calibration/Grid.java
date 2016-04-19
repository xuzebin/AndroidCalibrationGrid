package com.example.xuzebin.calibration;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;

/**
 * Create calibration grids' vertices, color, thickness
 * according to the given parameters.
 *
 * The created vertice buffer will be used in shader for rendering.
 *
 * Created by xuzebin on 16/4/18.
 */
public class Grid {
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


	public Grid() {
		this(18, 32, new Color(1f, 0f, 0f), 4, new Vec2(0f, 0f), new Color(0f, 1f, 0f), 12f, GridRenderer.RenderMode.DOUBLE);
	}
	public Grid(int rowNum, int colNum) {
		this(rowNum, colNum, new Color(1f, 0f, 0f), 4, new Vec2(0, 0), new Color(0f, 1f, 0f), 12f, GridRenderer.RenderMode.DOUBLE);
	}
	public Grid(int rowNum, int colNum, GridRenderer.RenderMode renderMode) {
		this(rowNum, colNum, new Color(1f, 0f, 0f), 4, new Vec2(0, 0), new Color(0f, 1f, 0f), 12f, renderMode);
	}

	public Grid(int rowNum, GridRenderer.RenderMode renderMode) {
		this(rowNum, new Color(1f, 0f, 0f), 4, new Vec2(0, 0), new Color(0f, 1f, 0f), 12f, renderMode);
	}
	/**
	 * Create square grids
	 * @param rowNum number of grids in row, grids in column will be calculated accordingly
	 * @param lineColor color of lines
	 * @param lineThickness thickness of lines
	 * @param gridCenter position of the grid center
	 * @param crossColor color of the grid center
	 * @param ctrThickness thickness of the grid center's line
	 * @param renderMode render single or double views
	 */
	public Grid(int rowNum, Color lineColor, float lineThickness, Vec2 gridCenter,
				 Color crossColor, float ctrThickness, GridRenderer.RenderMode renderMode) {

		mRowNum = rowNum;
		mLineColor = new Color();
		mLineColor = lineColor;
		mLineThickness = lineThickness;
		mCenterCross = new Vec2();
		mCenterCross = gridCenter;
		mCrossColor = new Color();
		mCrossColor = crossColor;
		mCtrThickness = ctrThickness;
		mScreenRatio = (float)16 / 9;


		if (renderMode == GridRenderer.RenderMode.SINGLE) {
			mColNum = rowNum * (int)mScreenRatio;
		} else if (renderMode == GridRenderer.RenderMode.DOUBLE) {
			mColNum = rowNum * (int)mScreenRatio;
		}
		mColNum = mColNum % 2 == 0 ? mColNum : mColNum + 1;
		ctrLengthX = ((float)2 / mColNum);
		ctrLengthY = ((float)2 / mRowNum);

		initBuffers();
	}
	public Grid(int rowNum, int colNum, Color lineColor, float lineThickness, Vec2 gridCenter,
				Color crossColor, float ctrThickness, GridRenderer.RenderMode renderMode) {

		mRowNum = rowNum;
		mColNum = colNum;
		mLineColor = new Color();
		mLineColor = lineColor;
		mLineThickness = lineThickness;
		mCenterCross = new Vec2();
		mCenterCross = gridCenter;
		mCrossColor = new Color();
		mCrossColor = crossColor;
		mCtrThickness = ctrThickness;
		mScreenRatio = (float)16 / 9;

		ctrLengthX = ((float)2 / colNum);
		ctrLengthY = ((float)2 / rowNum);

		initBuffers();
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
		final int ROW_LINE_POINT_NUM = (mRowNum - 1) * 2;
		final int COL_LINE_POINT_NUM = (mColNum - 1) * 2;
		lineVertices = new float[(ROW_LINE_POINT_NUM + COL_LINE_POINT_NUM) * 2];
		
		int index = 0;
		//create row lines
		for (int i = 0; i < mRowNum - 1; ++i) {
			lineVertices[index++] = -1;//x coordinate
			lineVertices[index++] = ((float) 2 / mRowNum) * (i + 1) - 1;//y coordinate
			
			lineVertices[index++] = 1;//x coordinate
			lineVertices[index++] = ((float) 2 / mRowNum) * (i + 1) - 1;//y coordinate
		}
		
		//create column lines
		for (int i = 0; i < mColNum - 1; ++i) {
			lineVertices[index++] = ((float)2 / mColNum) * (i + 1) - 1;//x coordinate
			lineVertices[index++] = -1;//y coordinate
			
			lineVertices[index++] = ((float)2 / mColNum) * (i + 1) - 1;//x coordinate
			lineVertices[index++] = 1;//y coordinate
		}
		
		lineBuffer = ByteBuffer.allocateDirect(lineVertices.length * FLOAT_SIZE_BYTES).
	    		order(ByteOrder.nativeOrder()).
	    		asFloatBuffer();
	    lineBuffer.put(lineVertices);
	    lineBuffer.position(0);
	    
	    
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
	  
	 
}
