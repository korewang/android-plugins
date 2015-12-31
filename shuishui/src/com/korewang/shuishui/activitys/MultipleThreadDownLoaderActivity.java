package com.korewang.shuishui.activitys;

import java.io.File;

import javax.net.ssl.ManagerFactoryParameters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.korewang.shuishui.R;
import com.korewang.shuishui.service.DoHttpGet;
import com.korewang.shuishui.service.downloader.DownloadProgressListener;
import com.korewang.shuishui.service.downloader.FileDownLoader;
import com.korwang.shuishui.utils.MyDrawView;

public class MultipleThreadDownLoaderActivity extends Activity {
	private static final String TAG = "MultipleThreadDownLoaderActivity";
	private static final int PROCESSING = 1;
	private static final int FAILURE = -1;
	
	private LinearLayout mly;
	private EditText pathText,userName,userPsd;
	private TextView resultView;
	private Button downlaodButton,stopButton,loginButton,getContactButton;
	private ProgressBar progressBar;
	public OrientationEventListener mScreenOrientationEventListener; 
	//handler 对象的作用是用于往创建handler对象所在的线程所绑定的消息队列发送消息并处理消息
	private Handler handler =new UIHandler();
	///
	 private SensorManager mSensorManager;
	 
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
 
    private TextView azimuthAngle;
 
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
	
	private int mOrientation =0;
	public Animation anim;
	/**
	 * 
	 * @params 系统会自动调用的毁掉哦方法 ，用于处理消息事件
	 * Message 一般会包含消息的标志和消息的内容以及消息的处理器handler
	 * @date 2015年12月9日
	 * &*& by class UIHandler
	 */
	private final class UIHandler extends Handler{
		public void handlerMessage(Message msg){
			switch(msg.what){
				case PROCESSING:// 下载ing
					int size = msg.getData().getInt("size");//下载长度
					progressBar.setProgress(size);
					float num  =(float)progressBar.getProgress() /(float)progressBar.getMax();// 计算下载百分比，转浮点型
					int  result = (int)(num*100);
					resultView.setText(result + "%");
					if(progressBar.getProgress() == progressBar.getMax()){
						// 下载完成时
						Toast.makeText(getApplicationContext(), R.string.downsuccess, 
								Toast.LENGTH_SHORT).show();
					}
					break;
				case FAILURE:
					Toast.makeText(getApplicationContext(), R.string.downerror,	
							Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_thread_down_loader_ly);
		//设置主界面
		mly = (LinearLayout)findViewById(R.id.rootly);
		pathText = (EditText)findViewById(R.id.path);
		resultView =(TextView)findViewById(R.id.resultView);
		downlaodButton = (Button)findViewById(R.id.downloadbutton);
		stopButton = (Button)findViewById(R.id.stopbutton);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		userName = (EditText)findViewById(R.id.username);
		userPsd = (EditText)findViewById(R.id.userpsd);
		loginButton = (Button)findViewById(R.id.login);
		getContactButton = (Button)findViewById(R.id.getcontact);
		ButtonClicklistener listener = new ButtonClicklistener();
		downlaodButton.setOnClickListener(listener);
		stopButton.setOnClickListener(listener);
		loginButton.setOnClickListener(listener);
		getContactButton.setOnClickListener(listener);
		
		anim = AnimationUtils.loadAnimation(this, R.anim.scale_translate);
		//set animation end save type
		anim.setFillAfter(true);
		
		final MyDrawView draw = new MyDrawView(this);
		
		draw.setMinimumHeight(500);
		draw.setMinimumWidth(400);
		draw.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				draw.CurrentX = event.getX();
				draw.CurrentY = event.getY();
				draw.invalidate();//通知组件重绘
				return true;
			}
		});
		mly.addView(draw);
		
		int rotation = this.getWindowManager().getDefaultDisplay().getRotation();  
		Log.i("rotation", "create::"+rotation);
		//0为关闭 1为开启
		//Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 1);
		//得到是否开启
		int flag = Settings.System.getInt(getContentResolver(),
		            Settings.System.ACCELEROMETER_ROTATION, 0);
		Log.i("rotation", "flag::"+flag);
		 // 实例化传感器管理者
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
 
        azimuthAngle =  (EditText)findViewById(R.id.username);
        calculateOrientation();
        
        
       
        AlbumOrientationEventListener mAlbumOrientationEventListener = new AlbumOrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL);  
        if (mAlbumOrientationEventListener.canDetectOrientation()) {  
            mAlbumOrientationEventListener.enable();  
        } else {  
            Log.d("chengcj1", "Can't Detect Orientation");  
        }   
	}
	
	
	
	@Override
	public void onResume(){
		super.onResume();
		int rotation = this.getWindowManager().getDefaultDisplay().getRotation();  
		Log.i("rotation", "rotation-OnResume"+rotation);
		
		
		 // 注册监听
        mSensorManager.registerListener(new MySensorEventListener(),
                accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(new MySensorEventListener(), magnetic,
                Sensor.TYPE_MAGNETIC_FIELD);
        super.onResume();
	}
	 @Override
	    protected void onPause() {
	        // TODO Auto-generated method stub
	        // 解除注册
	        mSensorManager.unregisterListener(new MySensorEventListener());
	        super.onPause();
	    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	// 计算方向
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
 
        Log.i(TAG, values[0] + "");
        if (values[0] >= -5 && values[0] < 5) {
            azimuthAngle.setText("正北");
        } else if (values[0] >= 5 && values[0] < 85) {
            // Log.i(TAG, "东北");
            azimuthAngle.setText("东北");
        } else if (values[0] >= 85 && values[0] <= 95) {
            // Log.i(TAG, "正东");
            azimuthAngle.setText("正东");
        } else if (values[0] >= 95 && values[0] < 175) {
            // Log.i(TAG, "东南");
            azimuthAngle.setText("东南");
        } else if ((values[0] >= 175 && values[0] <= 180)
                || (values[0]) >= -180 && values[0] < -175) {
            // Log.i(TAG, "正南");
            azimuthAngle.setText("正南");
        } else if (values[0] >= -175 && values[0] < -95) {
            // Log.i(TAG, "西南");
            azimuthAngle.setText("西南");
        } else if (values[0] >= -95 && values[0] < -85) {
            // Log.i(TAG, "正西");
            azimuthAngle.setText("正西");
        } else if (values[0] >= -85 && values[0] < -5) {
            // Log.i(TAG, "西北");
            azimuthAngle.setText("西北");
        }
    }
 
    
	
	/** 
     * 屏幕旋转时调用此方法 
     */  
    @SuppressWarnings("deprecation")
	@Override  
    public void onConfigurationChanged(Configuration newConfig) {  
        super.onConfigurationChanged(newConfig);  
        //newConfig.orientation获得当前屏幕状态是横向或者竖向   
        //Configuration.ORIENTATION_PORTRAIT 表示竖向   
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏   
        
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();  
        
        Log.i("rotation", "onConfigurationChanged==="+rotation);
        switch (rotation) {  
        case Surface.ROTATION_0:  
        	 Log.i("rotation", "onConfigurationChanged0==="+rotation);
        	 break;
        case Surface.ROTATION_90:  
        	Log.i("rotation", "onConfigurationChanged90==="+rotation);
       		break; 
        case Surface.ROTATION_180:  
        	Log.i("rotation", "onConfigurationChanged180==="+rotation);
        	break;
        case Surface.ROTATION_270:  
        	Log.i("rotation", "onConfigurationChanged270==="+rotation);
       	 break;
        }  
        
        //1  正确横屏  0 是竖屏逆时针旋转90,3是错误横屏需要逆时针专选180  
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){  
            Toast.makeText(MultipleThreadDownLoaderActivity.this, "现在是竖屏", Toast.LENGTH_SHORT).show();  
        }  
        if(newConfig.orientation == Configuration.ORIENTATION_UNDEFINED){
        	  Toast.makeText(MultipleThreadDownLoaderActivity.this, "现在是ORIENTATION_UNDEFINED", Toast.LENGTH_SHORT).show();
        }
    	if(newConfig.orientation == Configuration.ORIENTATION_SQUARE){
    		  Toast.makeText(MultipleThreadDownLoaderActivity.this, "现在是ORIENTATION_SQUARE", Toast.LENGTH_SHORT).show();
        }
        
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){  
            Toast.makeText(MultipleThreadDownLoaderActivity.this, "现在是横屏", Toast.LENGTH_SHORT).show();  
        }  
    }  
	
	private final class ButtonClicklistener implements View.OnClickListener{
		public void onClick(View v){
			switch(v.getId()){
				case R.id.downloadbutton:
					String path = pathText.getText().toString();
					//if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
						//获取sd卡是否存在    如果存在获取目录
					//	Environment.getExternalStorageDirectory();
						File saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
						getExternalFilesDir(Environment.DIRECTORY_MOVIES);
						File savedDir =new File( Environment.getExternalStorageDirectory()+"/shuishui/MyCameraApp/");
						
						download(path,savedDir);
						//Toast.makeText(getApplicationContext(), R.string.sdcarderror, Toast.LENGTH_SHORT).show();
				//	}else{
						Toast.makeText(getApplicationContext(), R.string.sdcarderror, Toast.LENGTH_SHORT).show();
					//}
					break;
				case R.id.stopbutton:
					exit();
					downlaodButton.setEnabled(true);
					stopButton.setEnabled(false);
					break;
				case R.id.login:
					final String name = userName.getText().toString();
					final String psd = userPsd.getText().toString();
					
					Thread ythread = new Thread(){
						public void run(){
							try{
								boolean result = 	DoHttpGet.login(name,	psd);
								if(result){
									Log.i("ddddddddddd", "true");
								}else{
									Log.i("ddddddddddd", "false");
								}
							}catch(Exception e){
								 e.printStackTrace();
								 Log.i("ddddddddddd", "printStackTrace:"+e.toString());
							}
						}
					};
					ythread.start();
					
			        
					Intent it = new Intent(Intent.ACTION_SEND);
					
					String[] tos = {"abc@qq.com"};
					String[] ccs = {"abcd@qq.com"};
					it.putExtra(Intent.EXTRA_EMAIL, tos);
					it.putExtra(Intent.EXTRA_CC, ccs);
					it.putExtra(Intent.EXTRA_TEXT, "邮件内容");
					it.putExtra(Intent.EXTRA_SUBJECT, "主题");
					it.setType("text/html");
					startActivity(it.createChooser(it, "choose Email client"));
					break;
				case R.id.getcontact:
					//设置动画
					loginButton.startAnimation(anim);
					//读取联系人
					Intent ln = new Intent();
					ln.setAction(Intent.ACTION_GET_CONTENT);
					ln.setType("vnd.android.cursor.item/phone");
					startActivityForResult(ln, 0);
					
					break;
			}
		}
		private DownloadTask task;// 声明下载执行者
		/**
		 * 退出下载
		 */
		public  void exit(){
			if(task !=null)
			{
				task.exit();
			}
		}
		/**
		 *   下载资源 声明下载之心这并开辟线程开始下载
		 *   path  下载路径
		 *   saveDir  保存文件
		 */
		private void download(String path , File saveDir){
			//  在主线程里运行
			 task = new DownloadTask(path,saveDir);//实例化下载任务
			 new Thread(task).start();//下载开始
		}
		/**
		 * UI 控制画面的重回 由主线程负责处理的，如果在子线程中更新ui  控件，更新后的值不会重绘到屏幕上
		 * 一定要在主线程里更新ui控件值，
		 * 
		 * 
		 * add unimplements methods
		 */
		
		private final class DownloadTask implements Runnable{
			private String path;
			private File saveDir;
			private FileDownLoader loader;
			
			/**
			 * 构造方法
			 * @param path x下载路径
			 * @param saveDir下载要保存到的文件
			 */
			public DownloadTask(String path ,File saveDir){
				this.path = path;
				this.saveDir = saveDir;
				
			}
			/**
			 * 
			 *@author wangxinyan
			 * TODO:退出下载
			 */
			public void exit(){
				if(loader!=null)
				{
					loader.exit();
				}
			}
			DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
				//接口bean    开始下载并设置下载的监听器
				@Override
				public void onDownloadSize(int size) {
					// TODO Auto-generated method stub
					Message  msg  = new Message();//新建立一个Message对象
					msg.what = PROCESSING;
					msg.getData().putInt("size", size);
					handler.sendMessage(msg);
				}
				
			};

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					loader = new FileDownLoader(getApplicationContext(), path, saveDir, 3);//初始化下载
					
					progressBar.setMax(loader.getFileSize());//设置进度条的最大刻度
					loader.download(downloadProgressListener);
				}catch(Exception e){
					e.printStackTrace();
					Log.i("ddddddddddd", "printStackTrace:"+e.toString());
					handler.sendMessage(handler.obtainMessage(FAILURE));
					/*
					 * 下载失败给消息队列发送的消息
					 *  等价于  
					 *  Message msg = handler.obtainMessage();
					 *  msg.what = FAILURE
					 * */
				}
			}
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.multiple_thread_down_loader, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public  class MySensorEventListener implements SensorEventListener{
		 @Override
	     public void onSensorChanged(SensorEvent event) {
	         // TODO Auto-generated method stub
	         if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	             accelerometerValues = event.values;
	         }
	         if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
	             magneticFieldValues = event.values;
	         }
	         calculateOrientation();
	     }

	     @Override
	     public void onAccuracyChanged(Sensor sensor, int accuracy) {
	         // TODO Auto-generated method stub

	     }

	 }
	
	private class AlbumOrientationEventListener extends OrientationEventListener {  
	    public AlbumOrientationEventListener(Context context) {  
	        super(context);  
	    }  
	          
	    public AlbumOrientationEventListener(Context context, int rate) {  
	        super(context, rate);  
	    }  
	  
	    @Override  
	    public void onOrientationChanged(int orientation) {  
	        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {  
	            return;  
	        }  
	  
	        //保证只返回四个方向  
	        int newOrientation = ((orientation + 45) / 90 * 90) % 360  ;
	      //  int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
	        if (newOrientation != mOrientation) {  
	            mOrientation = newOrientation;  
	            Log.i("newOrientation", "ro"+newOrientation);
	            	//返回的mOrientation就是手机方向，为0°、90°、180°和270°中的一个  
	        }  
	    }  
	}
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if(resultCode ==Activity.RESULT_OK){
				//get return data
				Uri contactData = data.getData();
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				if(cursor.moveToFirst()){
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					String name= cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
					String phoneNumber = "此联系人暂未输入电话号码！";
					Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
					//取出第一行
					if(phones.moveToFirst()){
						phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					// 关闭游标
					phones.close();
					Log.i(TAG, "联系人名称："+name+"&联系人电话："+phoneNumber);
				}
				cursor.close();
			}
			break;

		default:
			break;
		}
	}
	
}


