package com.korewang.shuishui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.korewang.shuishui.plugins.MyViews;
import com.korewang.shuishui.widget.HeaderView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AnimationFrameActivity extends Activity implements View.OnClickListener{

	private Button mStart,mStop,mbn1,mbn2,mbn3,mbn4,mbn5;
	private ImageView mframeImage,mShaderImage;
	private AnimationDrawable anim;
	private LinearLayout mly,msinner;
	//生命位图渲染对象  shader  三维着色器
	private Shader[] shaders = new Shader[5];
	//生命颜色数组
	private int[] colors;
	MyShaderView myView;
	private MyViews madView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_frame_ly);
		HeaderView headerview = (HeaderView)findViewById(R.id.headerViewAnimation);
		headerview.setHeaderTitle("Frame绘图");
		mly = (LinearLayout)findViewById(R.id.ly);
		msinner = (LinearLayout)findViewById(R.id.sinner);
		mStart = (Button)findViewById(R.id.start);
		mStop  =(Button)findViewById(R.id.stop);
		mbn1 = (Button)findViewById(R.id.bn1);
		mbn2 = (Button)findViewById(R.id.bn2);
		mbn3 = (Button)findViewById(R.id.bn3);
		mbn4 = (Button)findViewById(R.id.bn4);
		mbn5 = (Button)findViewById(R.id.bn5);
		mframeImage = (ImageView)findViewById(R.id.frameImage);
		mframeImage.setBackgroundResource(R.anim.animation_frame);
		anim = (AnimationDrawable)mframeImage.getBackground();
		mShaderImage = (ImageView)findViewById(R.id.shaderImage);
		mStart.setOnClickListener(this);
		mStop.setOnClickListener(this);
		mbn1.setOnClickListener(this);
		mbn2.setOnClickListener(this);
		mbn3.setOnClickListener(this);
		mbn4.setOnClickListener(this);
		mbn5.setOnClickListener(this);
		
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sock);
		colors = new int[]{Color.RED,Color.GREEN,Color.BLUE};
		//  实例化bitmapshader x坐标方向重复图形，y坐标方向重复图形
		shaders[0] = new BitmapShader(bm, TileMode.REPEAT, TileMode.MIRROR);
		// 实例化LinearFradient
		shaders[1] = new LinearGradient(0, 0, 100, 100, colors, null, TileMode.REPEAT);
		//实例化RadialGradient
		shaders[2] = new RadialGradient(100, 100, 80, colors, null, TileMode.REPEAT);
		//实例化SweepFradient
		shaders[3] = new SweepGradient(160, 160, colors, null);
		//实例化conposeShader		
		shaders[4] = new ComposeShader(shaders[1], shaders[2], PorterDuff.Mode.DARKEN);
		madView = new MyViews(this ,R.drawable.river);
		mly.addView(madView);   // add  layout里
		
		//msinner.addView(madView);//add 到scrollview里  不起作用
		
		
		//myView = new MyShaderView(this);// shader 创建 本地 shader 类
//		mly.addView(myView);///  添加到layout里
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.start: 
				anim.start();
				break;
			case R.id.stop:
				anim.stop();
				break;
				
			case R.id.bn1:
				break;
			case R.id.bn2:
				break;
			case R.id.bn3:
				break;
			case R.id.bn4:
				break;
			case R.id.bn5:
				String name = System.currentTimeMillis()+".jpg";
				saveImage(name);
				
				break;
		}
	}
	private class MyShaderView extends View{
		private Paint paint;
		public MyShaderView(Context context) {
			super(context);
			
		}
		@Override
		public void onDraw(Canvas canvas){
			super.onDraw(canvas);
			
			paint = new Paint();
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sock);
			//paint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
			canvas.drawRect(300, 300, 300, 300, paint);
		}
	}
	
	
	
	/**
	 * 
	 *@author wangxinyan
	 * 保存图片  imageview
	 */
	public static Bitmap convertViewToBitmap(View view)  
    {  
		view.setDrawingCacheEnabled(true);
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
        view.buildDrawingCache();  
        Bitmap bitmap = view.getDrawingCache();  
        view.setDrawingCacheEnabled(false);
        return bitmap;  
    }   
	
	 public static String getSDPath()  
	    {  
	        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);  
	        if(hasSDCard)  
	        {  
	            return Environment.getExternalStorageDirectory().toString() + "/shuishui/MyCameraApp";  
	        }  
	        else  {
	        	return "/data/data/com.korewang.shuishui/saving_picture";
	        }
	              
	    }
	 public Bitmap createViewBitmap(View v) {
	        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
	                Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(bitmap);
	        v.draw(canvas);
	        return bitmap;
	    }
	 public void saveImage(String strFileName)  
	    {  
	        Bitmap bitmap = convertViewToBitmap(mShaderImage);  
	        String strPath = getSDPath();  
	  
	        try  
	        {  
	            File destDir = new File(strPath);  
	            if (!destDir.exists())  
	            {  
	                Log.d("MagicMirror", "Dir not exist create it " + strPath);  
	                destDir.mkdirs();  
	                Log.d("MagicMirror", "Make dir success: " + strPath);  
	            }  
	              
	            File imageFile = new File(strPath + "/" + strFileName);  
	            imageFile.createNewFile();  
	            FileOutputStream fos = new FileOutputStream(imageFile);  
	            /*
	           *    两种方法均可保存到本地   一种是view  另一个是直接asset里的图片资源
	           * */
	            Bitmap b = createViewBitmap(madView);//  使用本地的view组件来保存到本地 mShaderImage
	            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.sock);// 使用资源文件保存到本地
	            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);  
        		 //mShaderImage.setDrawingCacheEnabled(false);
	            fos.flush();  
	            fos.close();  
	        }  
	        catch (FileNotFoundException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        catch (IOException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	    }  
	
}
