package com.korwang.shuishui.utils;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class MyDrawView extends View{
	
	public float CurrentX = 30;
	public float CurrentY = 30;
	
	public MyDrawView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint pt = new Paint();
		pt.setStyle(Paint.Style.STROKE);//空心    ，实心Paint.style.fil
		pt.setStrokeWidth(2);//  空心 宽度2
		pt.setColor(Color.RED);
		pt.setAntiAlias(true);//消除锯齿状
		canvas.drawCircle(CurrentX, CurrentY, 15, pt);//圆，正方形长方形canvas.drawRect  椭圆RectF re = new RectF   canvas.drawOval(re, paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		this.CurrentX = event.getX();
		this.CurrentY = event.getY();
		this.invalidate();
		return true;
	}
}
