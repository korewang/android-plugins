package com.korewang.shuishui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestService extends Service{

	@Override
	public IBinder onBind(Intent arg0){
		return null;
	}
	@Override
	public void onCreate(){
		super.onCreate();
		System.out.println("service is createed");
	}
	@Override
	public int onStartCommand(Intent intent ,int flage,int startId){
		System.out.println("service is started");
		return START_STICKY;
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		System.out.println("service is destroyed");
	}
}
