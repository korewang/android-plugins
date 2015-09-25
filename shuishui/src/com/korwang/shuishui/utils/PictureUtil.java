package com.korwang.shuishui.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Display;
import android.widget.ImageView;
/**
 * @author wxy 
 * @email korewang_2011@gmail.com
 * @date 2015年9月24日
 * &*& by class PictureUtil
 */
public class PictureUtil {

	//@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity ay, String path){
		Display display = ay.getWindowManager().getDefaultDisplay();
		// up version release getWidth and getHeight
		float destWidth,destHeight;
		// version comparev
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2){//3.0
			Point getSize = new Point();
			display.getSize(getSize);
			destWidth = getSize.x;
			destHeight = getSize.y;
		}else{
			destWidth = display.getWidth();
			destHeight = display.getHeight();
		}
		//Read in the dimensions of the image on disk
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		int inSampleSize = 1;
		if(srcHeight > destHeight || srcWidth > destWidth){
			if(srcWidth > srcHeight){
				inSampleSize = Math.round(srcHeight / destHeight);
			}else{
				inSampleSize = Math.round(srcWidth / destWidth); 
			}
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return new BitmapDrawable(ay.getResources(), bitmap);
	}
	//How to clean a picture of ImageView in Activity
	public static void cleanImageView(ImageView imageView){
		if(!(imageView.getDrawable() instanceof BitmapDrawable)){
			return ;
		}
		BitmapDrawable bp = (BitmapDrawable)imageView.getDrawable();
		/*recycle 可以释放bitmap占用的原始存储空间
		 * 如果不调用recycle()方法释放内存，占用的内存也会被清理，但是，它是在将来的某个时点finalizer
		 * 中清理，而不是bitmap自身的垃圾回收时清理，这意味着很可能在finalizer调用之前，应用已经耗尽了
		 * 内存资源。
		 * 一般建议在Activity 的onStop调用recycle();
		 * */
		bp.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
}
