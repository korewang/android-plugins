package com.korewang.shuishui.animation.activitys;

import java.util.Calendar;

import com.baidu.navi.location.al;
import com.korewang.shuishui.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AlarmActivity extends Activity{
	
	private MediaPlayer alarmMusic;
	protected AlarmManager aManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		alarmMusic = MediaPlayer.create(this, R.raw.beep);
		alarmMusic.setLooping(true);
		alarmMusic.start();
		
		
		new AlertDialog.Builder(AlarmActivity.this)
		.setTitle("闹铃")
		.setMessage("闹铃响了，1234")
		.setPositiveButton("确定",	 new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alarmMusic.stop();
				AlarmActivity.this.finish();
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				aManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
				Intent intent = new Intent(AlarmActivity.this,AlarmActivity.class);
				PendingIntent pi = PendingIntent.getActivity(AlarmActivity.this, 0, intent, 0);
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());
				

				
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int min = c.get(Calendar.MINUTE);
				c.set(Calendar.HOUR, hour);
				c.set(Calendar.MINUTE, min+2);
				aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
				Log.i("alarm", "c.getTimeInMillis():"+c.getTimeInMillis()+"hour:"+hour+"=:minute:"+(min+2));
				Toast.makeText(getApplicationContext(),"伙计!再休息两分钟吧", Toast.LENGTH_SHORT).show();
				alarmMusic.stop();
				AlarmActivity.this.finish();
				
			}
		}).show();
	}
}
