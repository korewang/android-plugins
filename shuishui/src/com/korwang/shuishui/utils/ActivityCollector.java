package com.korwang.shuishui.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollector {
// ac manager
	public static List<Activity> activitys = new ArrayList<Activity>();
	
	public static void addActivity(Activity activity){
		activitys.add(activity);
	}
	public static void removeActivity(Activity activity){
		activitys.remove(activity);
	}
	public static void finishAll(){
		for(Activity ac :activitys){
			if(!ac.isFinishing()){
				ac.finish();
			}
		}
	}
}
