package com.korwang.shuishui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class CreateeBitmapForView {

	private Bitmap bitmap;
	public CreateeBitmapForView() {
		// TODO Auto-generated constructor stub
		
	}
	public static Bitmap CreateeBitmapForView(View view ,Context context){

		view.setDrawingCacheEnabled(true);    
	
		view.measure(    
	
		            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),    
	
		            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));    
	
		view.layout(0, 0, view.getMeasuredWidth(),    
				view.getMeasuredHeight());    
		view.buildDrawingCache();    
		    Bitmap bitmap= view.getDrawingCache();    
	
		    if(bitmap!=null){    
		    	//  success get bitmap    
		    }else{    
		          //buildDrawingCache  error
		    } 
		    return bitmap;
	}

}
