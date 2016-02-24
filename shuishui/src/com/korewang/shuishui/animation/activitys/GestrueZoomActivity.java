package com.korewang.shuishui.animation.activitys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Calendar;

import android.R.integer;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Contacts.People;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.korewang.shuishui.MyExpandableListActivity;
import com.korewang.shuishui.R;
import com.korewang.shuishui.activitys.BmapActivity;
import com.korewang.shuishui.activitys.CameraSettingActivity;
import com.korewang.shuishui.activitys.InitActivity;
import com.korewang.shuishui.base.BaseActivity;
import com.korewang.shuishui.plugins.GifMovieView;
import com.korewang.shuishui.service.Contact_connection;
import com.korwang.shuishui.utils.Data;

public class GestrueZoomActivity extends BaseActivity implements OnGestureListener{
	private static final String TAG = "GestrueZoomActivity";
	private static final String FIRST_SERVICE = "com.shuishui.FIRST_SERVICE";
	private GestureDetector detector;
	private ImageView mImage;
	private Bitmap bitmap;
	private int width,height;
	private float currentScale = 1;
	private Matrix matrix;
	private Button stop,start,alarmButton,addShortCutButton;
	private Vibrator vibrator;
	
	private AlarmManager aManager;
	private static final String D_KEY="d_key";
	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		String tempDatas = "N times comming to GestrueZoom";
		outState.putString(D_KEY, tempDatas);
		Log.i(TAG, "see you");///home键状态挂起
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Task id is:"+getTaskId());
		setContentView(R.layout.gestrue_zoom_ly);
		if(savedInstanceState != null){
			String d_key = savedInstanceState.getString(D_KEY);
			Log.i(TAG, "see you again "+d_key);
		}
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		aManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
		detector = new GestureDetector(this);
		mImage  = (ImageView)findViewById(R.id.imag);
		matrix = new Matrix();
		bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		mImage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
		
		final Intent it = new Intent();
		it.setAction(FIRST_SERVICE);
		start = (Button)findViewById(R.id.startService);
		stop = (Button)findViewById(R.id.stopService);
		alarmButton = (Button)findViewById(R.id.alarmButton);
		addShortCutButton = (Button)findViewById(R.id.addShortCutButton);
		/*
		 * gif
		 * */
		final GifMovieView fi = (GifMovieView)findViewById(R.id.gif_movie_view);
		final GifMovieView fgirl = (GifMovieView)findViewById(R.id.gif_movie_girl);
		fi.setMovieResource(R.drawable.smxx);
		fgirl.setMovieResource(R.drawable.wwwes);
		
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startService(it);//  开启服务
				
			//	sendNotification();//  顶端提示
				
				// 打开相机
//				Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//				startActivity(it);
				Contact_connection contactIn = new Contact_connection(getApplicationContext());
				contactIn.getContactInfo();
				contactIn.updatePhoneNumberById(3, "12377777");
				contactIn.stepAddContacts("w","010","qq@qq.com");
				try {
					contactIn.testAddContactsInTransaction("linlll","020222");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			/*	Intent m = new Intent(GestrueZoomActivity.this,BmapActivity.class);
				m.putExtra("FromOne", "FromOneFromOneFromOne");
				setResult(RESULT_OK, m);
				finish();*/
			}
		});
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(it);
				sendNotificationTwo();
				//socket一个服务端http://download.eclipse.org/mpc/indigo/   Marketplace Client
				/*try{
					Socket socket = new Socket("127.0.0.1",3000);
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String line = br.readLine();
					Log.i(TAG, "来自服务器："+line);
					br.close();
					socket.close();
				}catch(IOException e){
					e.printStackTrace();
				}*/
			}
		});
		alarmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取alarmManager对象
				Calendar currentTime = Calendar.getInstance();
				new TimePickerDialog(GestrueZoomActivity.this, 0, new TimePickerDialog.OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(GestrueZoomActivity.this,AlarmActivity.class);
						//创建pendingintent对象
						PendingIntent pi = PendingIntent.getActivity(GestrueZoomActivity.this, 0, intent, 0);
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(System.currentTimeMillis());
						//根据用户时间来设置calendar 对象
						c.set(Calendar.HOUR, hourOfDay);
						c.set(Calendar.MINUTE, minute);
						c.set(Calendar.SECOND, 0);  
                        c.set(Calendar.MILLISECOND, 0);
						aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
						Log.i("alarm", "c.HOUR_OF_DAY:"+hourOfDay+":c.MINUTE"+minute);
						Toast.makeText(getApplicationContext(), "闹铃设置成功", Toast.LENGTH_SHORT).show();
						/*
						 * 
						 * Intent intent = new Intent(Test.this,AlarmActivity.class);  
				            PendingIntent pi = PendingIntent.getBroadcast(Test.this, 0, intent, 0);  
				            AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
				            am.cancel(pi);  
						 * 
						 * */
					}
				}, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE),false).show();
			}
		});
		
		//addShortCutButton  添加快捷桌面方式
		addShortCutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//d 点击后的添加跨界方式   启动项  直接进入到此activity
				Intent  addIntent = new Intent();
				addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
				String title = getResources().getString(R.string.shortCutTitle);
				//icon
				Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);
				/*
				 * 创建点击跨界方式后的操作Intent，
				 * 该处点击创建快捷方式，再次启动该程序
				 *  可以指定再次启动 的activity
				 * */
				Intent myItent = new Intent(GestrueZoomActivity.this,InitActivity.class);
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
				//快捷方式对应的intent
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myItent);
				sendBroadcast(addIntent);
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		return detector.onTouchEvent(event);
	}
	@Override
	public boolean onFling(MotionEvent event1,MotionEvent event2,float velocityX,float velocityY){
		velocityX = velocityX >4000 ?4000:velocityX;
		velocityY = velocityY <-4000 ?-4000:velocityY;
		currentScale += currentScale*velocityX/4000.0f;
		currentScale = currentScale >0.01 ?currentScale	:0.01f;
		matrix.reset();
		matrix.setScale(currentScale, currentScale,200,320);
		BitmapDrawable tmp = (BitmapDrawable)mImage.getDrawable();
		if(!tmp.getBitmap().isRecycled()){
			tmp.getBitmap().recycle();
		}
		//根据原始位图和matrix 创建新图片
		Bitmap bitmap2  =  Bitmap.createBitmap(bitmap, 0, 0,width,height,matrix,true);
		mImage.setImageBitmap(bitmap2);
		return true;
	}
	@Override
	public boolean onDown(MotionEvent arg1){
		return false;
	}
	@Override
	public void onLongPress(MotionEvent event){
	}
	@Override
	public boolean onScroll(MotionEvent event1,MotionEvent event2,float distanceX,float distanceY){
		return false;
	}
	@Override
	public void onShowPress(MotionEvent vent){
	}
	public boolean onSingleTapUp(MotionEvent event){
		return false;
	}
	
	
	@SuppressWarnings("deprecation")
	public void sendNotification(){ // notification在返回 task设计
		NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		 
		Notification notification = new Notification(R.drawable.ss_icon, "new msg", System.currentTimeMillis());
		Intent it = new Intent(getApplicationContext(), GestrueZoomActivity.class);  
		TaskStackBuilder st = TaskStackBuilder.create(getApplicationContext());  
		st.addParentStack(GestrueZoomActivity.class);  
		st.addNextIntent(it);  
		int rc = (int) SystemClock.uptimeMillis();  
		PendingIntent pi = st.getPendingIntent(rc, PendingIntent.FLAG_UPDATE_CURRENT);  
		notification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), "msg context 123", pi);
		/*|=
		 FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉  
         FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉  
         FLAG_ONGOING_EVENT 通知放置在正在运行  
         FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
		 */
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		nManager.notify(11, notification);
	}
	@SuppressWarnings("deprecation")
	public void sendNotificationTwo(){
		NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		 
		Notification notification = new Notification(R.drawable.ss_icon, "new msg", System.currentTimeMillis());
		Intent it = new Intent(getApplicationContext(),CameraSettingActivity.class);  //点击notification 时进入的activity
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification.flags=Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(getApplicationContext(),
				getResources().getString(R.string.app_name), "new shuishui app msg", pi);
		nManager.notify(110, notification);
	}
}

