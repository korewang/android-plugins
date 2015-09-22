package com.korewang.shuishui.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.korewang.shuishui.R;

public class FragmentPageFour extends Fragment implements View.OnClickListener,View.OnTouchListener{

	private View rootView;
	private SeekBar sb1, sb2, sb3, sb4, sb5;  
    private ImageView iv;  
    private Bitmap bitmap, updateBitmap;  
    private Canvas canvas;  
    private Paint paint;  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_four, null);
			initRGB();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	/*view rgb*/
	public void initRGB(){
		iv = (ImageView)rootView.findViewById(R.id.iv);  
        sb1 = (SeekBar) rootView.findViewById(R.id.sb1);  
        sb2 = (SeekBar) rootView.findViewById(R.id.sb2);  
        sb3 = (SeekBar) rootView.findViewById(R.id.sb3);  
        sb4 = (SeekBar) rootView.findViewById(R.id.sb4);  
        sb5 = (SeekBar) rootView.findViewById(R.id.sb5);  
  
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wats);  
  
        updateBitmap = Bitmap.createBitmap(bitmap.getWidth(),  
                bitmap.getHeight(), bitmap.getConfig());  
        canvas = new Canvas(updateBitmap);  
        paint = new Paint();  
        final ColorMatrix cm = new ColorMatrix();  
        paint.setColorFilter(new ColorMatrixColorFilter(cm));  
        paint.setColor(Color.BLACK);  
        paint.setAntiAlias(true);  
        final Matrix matrix = new Matrix();  
        canvas.drawBitmap(bitmap, matrix, paint);  
        iv.setImageBitmap(updateBitmap);
        
        /** 
         * RGB三原色 红色值的设置 
         */  
        sb1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {  
  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar) {  
                int progress = seekBar.getProgress();  
                cm.set(new float[] { progress / 128f, 0, 0, 0, 0,// 红色值  
                        0, 1, 0, 0, 0,// 绿色值  
                        0, 0, 1, 0, 0,// 蓝色值  
                        0, 0, 0, 1, 0 // 透明度  
                });  
                paint.setColorFilter(new ColorMatrixColorFilter(cm));  
                canvas.drawBitmap(bitmap, matrix, paint);  
                iv.setImageBitmap(updateBitmap);  
            }  
  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar) {  
  
            }  
  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress,  
                    boolean fromUser) {  
  
            }  
        });  
        /** 
         * RGB三原色 绿色值的设置 
         */  
        sb2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {  
  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar) {  
                int progress = seekBar.getProgress();  
                cm.set(new float[] { 1, 0, 0, 0, 0,// 红色值  
                        0, progress / 128f, 0, 0, 0,// 绿色值  
                        0, 0, 1, 0, 0,// 蓝色值  
                        0, 0, 0, 1, 0 // 透明度  
                });  
                paint.setColorFilter(new ColorMatrixColorFilter(cm));  
                canvas.drawBitmap(bitmap, matrix, paint);  
                iv.setImageBitmap(updateBitmap);  
            }  
  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar) {  
  
            }  
  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress,  
                    boolean fromUser) {  
  
            }  
        }); 
        /** 
         * RGB三原色 蓝色值的设置 
         */  
        sb3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {  
  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar) {  
                int progress = seekBar.getProgress();  
                cm.set(new float[] { 1, 0, 0, 0, 0,// 红色值  
                        0, 1, 0, 0, 0,// 绿色值  
                        0, 0, progress / 128f, 0, 0,// 蓝色值  
                        0, 0, 0, 1, 0 // 透明度  
                });  
                paint.setColorFilter(new ColorMatrixColorFilter(cm));  
                canvas.drawBitmap(bitmap, matrix, paint);  
                iv.setImageBitmap(updateBitmap);  
            }  
  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar) {  
  
            }  
  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress,  
                    boolean fromUser) {  
  
            }  
        });  
  
        /** 
         * RGB三原色 三个值都改变为设置饱和度（亮度） 
         */  
        sb4.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {  
  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar) {  
                int progress = seekBar.getProgress();  
                cm.set(new float[] { progress / 128f, 0, 0, 0, 0,// 红色值  
                        0, progress / 128f, 0, 0, 0,// 绿色值  
                        0, 0, progress / 128f, 0, 0,// 蓝色值  
                        0, 0, 0, 1, 0 // 透明度  
                });  
                paint.setColorFilter(new ColorMatrixColorFilter(cm));  
                canvas.drawBitmap(bitmap, matrix, paint);  
                iv.setImageBitmap(updateBitmap);  
            }  
  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar) {  
  
            }  
  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress,  
                    boolean fromUser) {  
  
            }  
        });  
        /** 
         * RGB三原色 设置透明度 
         */  
        sb5.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {  
  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar) {  
                int progress = seekBar.getProgress();  
                cm.set(new float[] { 1, 0, 0, 0, 0,// 红色值  
                        0, 1, 0, 0, 0,// 绿色值  
                        0, 0, 1, 0, 0,// 蓝色值  
                        0, 0, 0, progress / 128f, 0 // 透明度  
                });  
                paint.setColorFilter(new ColorMatrixColorFilter(cm));  
                canvas.drawBitmap(bitmap, matrix, paint);  
                iv.setImageBitmap(updateBitmap);  
            }  
  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar) {  
  
            }  
  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress,  
                    boolean fromUser) {  
  
            }  
        }); 
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
