package com.korwang.shuishui.utils;

import com.korewang.shuishui.FragmentMainActivity;
import com.korewang.shuishui.activitys.BmapActivity;
import com.korewang.shuishui.activitys.CameraSettingActivity;
import com.korewang.shuishui.activitys.DownImageActivity;
import com.korewang.shuishui.activitys.GetGPSActivity;
import com.korewang.shuishui.activitys.ListAppActivity;
import com.korewang.shuishui.activitys.WebChromeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class UIControl {
	public static void startListAppActivity(Context context) {
        Intent intent = new Intent(context, ListAppActivity.class);
        //context.startActivityForResult(intent);
        context.startActivity(intent);
    }
	public static void startGPSActivity(Context context) {
        Intent intent = new Intent(context, GetGPSActivity.class);
        //context.startActivityForResult(intent);
        context.startActivity(intent);
    }
    public static void startBmapActivity(Context context) {
        Intent intent = new Intent(context, BmapActivity.class);
        //context.startActivityForResult(intent);
        context.startActivity(intent);
    }
    public static void startWebViewActivity(Context context) {
        Intent intent = new Intent(context, WebChromeActivity.class);
        //context.startActivityForResult(intent);
        context.startActivity(intent);
    }
    public static void startDownImageActivity(Context context){
    	Intent intent = new Intent(context,DownImageActivity.class);
    	context.startActivity(intent);
    }
    public static void startFragmentActivity(Context context){
    	Intent intent = new Intent(context,FragmentMainActivity.class);
    	context.startActivity(intent);
    }
    
    public static void startCameraSettingActivity(Context context){
    	Intent intent = new Intent(context,CameraSettingActivity.class);
    	context.startActivity(intent);
    }
}
