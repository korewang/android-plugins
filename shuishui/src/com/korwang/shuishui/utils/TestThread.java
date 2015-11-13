package com.korwang.shuishui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

public class TestThread extends View implements Runnable{
		private LocalBroadcastManager mLocalBroadcastManager;
		
		public TestThread(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			mLocalBroadcastManager =  LocalBroadcastManager.getInstance(context);
			IntentFilter mintentfiler = new IntentFilter();
	        mintentfiler.addAction("add_action");
	        mintentfiler.addAction("add_action_name");
	        mLocalBroadcastManager.registerReceiver(mreceive, mintentfiler);
		}
		
		private BroadcastReceiver mreceive = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context , Intent intent){
				if(intent.getAction() == "add_action"){
					String str = intent.getStringExtra("action_set");
					Log.i("BroadcastReceiver", "add_actions"+str);
				}else if(intent.getAction() == "add_action_name"){
					Log.i("BroadcastReceiver", "add_action_names");
				}
					
			}
			
		};
		@Override
        public void run() {
			for(int i =0;i<3;i++){
				
			
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 System.out.println("休眠被打断"+i);
				 Intent intent = new Intent("add_action");
				 intent.putExtra("action_set", "Hello ,this is a char");
				 mLocalBroadcastManager.sendBroadcast(intent);
			}
			
		}
}
