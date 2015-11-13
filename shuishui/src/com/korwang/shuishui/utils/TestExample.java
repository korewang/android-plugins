package com.korwang.shuishui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class TestExample extends View{
	private Paint mPaint;
	private float mFontSize;
	private float dx,dy;
	public TestExample(Context context){
		super(context);
		mFontSize = 16*getResources().getDisplayMetrics().density;
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setTextSize(mFontSize);
	}
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		String text = "Hello World";
		canvas.drawText(text, dx, dy, mPaint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		dx = event.getX();
		dy = event.getY();
		invalidate();
		return true;
	}
	public class GestureActivity extends Activity{
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			TestExample view = new TestExample(this);
			setContentView(view);
		}
	}
}
