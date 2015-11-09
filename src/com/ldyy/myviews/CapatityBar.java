package com.ldyy.myviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CapatityBar extends View {

	private static String TAG = "CapatityBar";
	private final boolean D = true;
	private void log(String msg){
		if(D){
			Log.e(TAG, msg);
		}
	}
	
	private int width;
	private int height;
	private Paint mPaint;
	private Paint fillPaint;
	private RectF fillRectf;
	
	private long maxLength;
	private long currentLength;
	
	
	//注意默认的布局文件调用的是两个参数的构造方法
	public CapatityBar(Context context,  AttributeSet attrs) {
		super(context, attrs);
		this.maxLength = 100;
		this.currentLength = 0;
		mPaint = new Paint();
		mPaint.setColor(Color.GRAY);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(1);
		
		fillPaint = new Paint();
		fillPaint.setColor(Color.GREEN);
		fillPaint.setStyle(Paint.Style.FILL);
		fillRectf = new RectF();
		
	}
	public CapatityBar(Context context) {
		super(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//获取控件高度和宽度
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(width > height){
			fillRectf.set(0, 0, (float)currentLength/maxLength*width, height);
//			log((float)currentLength/maxLength*width+":"+width);
//			log("currentLength"+currentLength);
//			log("maxLength"+maxLength);
			
		}else{
			fillRectf.set(0, height - (float)currentLength/maxLength*height, width, height);
		}
		canvas.drawRect(0, 0, width, height, mPaint);
		canvas.drawRect(fillRectf, fillPaint);
	}
	
	public void setData(long max, long curr){
		if(max < 0 || curr < 0 || max < curr){
			throw new RuntimeException("data id invaild!");
		}
		this.maxLength = max;
		this.currentLength = curr;
		
	}

}
