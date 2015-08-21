package com.korewang.shuishui.application;


import com.korwang.shuishui.utils.DebugUtil;

import android.app.Application;

public class Applications extends Application{
	
	@Override
	public void onCreate(){
		 super.onCreate();
	        DebugUtil.setUncaughtExceptionHandler();
	}
	

}
