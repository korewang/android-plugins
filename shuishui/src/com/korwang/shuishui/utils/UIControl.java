package com.korwang.shuishui.utils;

import com.korewang.shuishui.AnimationFrameActivity;
import com.korewang.shuishui.MyExpandableListActivity;
import com.korewang.shuishui.MyPreferenceActivity;
import com.korewang.shuishui.WrapActivity;
import com.korewang.shuishui.activitys.BmapActivity;
import com.korewang.shuishui.activitys.CameraSettingActivity;
import com.korewang.shuishui.activitys.DownImageActivity;
import com.korewang.shuishui.activitys.FragmentMainActivity;
import com.korewang.shuishui.activitys.GetGPSActivity;
import com.korewang.shuishui.activitys.ListAppActivity;
import com.korewang.shuishui.activitys.MultipleThreadDownLoaderActivity;
import com.korewang.shuishui.activitys.WebChromeActivity;

import android.app.Activity;
import android.app.Notification.Action;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.MultiAutoCompleteTextView;


public class UIControl {
	final static String PREFERENCE = "android.intent.action.PREFERENCE";
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
    public static void startMultipleActivity(Context context){
    	Intent intent = new Intent(context,MultipleThreadDownLoaderActivity.class);
    	context.startActivity(intent);
    }
    public static void startExpandableListActivity(Context context){
    	Intent intent = new Intent(context,MyExpandableListActivity.class);
    	context.startActivity(intent);
    }
    public static void startPreferenceSActivity(Context context){
//    	
//    	Intent intent = new Intent(context,MyPreferenceActivity.class);
//    	context.startActivity(intent);
    	Intent intent = new Intent();
    	intent.setAction(UIControl.PREFERENCE);
    	context.startActivity(intent);
    	/*ComponentName com = new ComponentName(context,MyPreferenceActivity.class);
    	Intent ln = new Intent();
    	ln.setComponent(com);
    	context.startActivity(ln);*/
    }
    public static void startAnimationFrameActivity(Context context){
    	Intent intent = new Intent(context,AnimationFrameActivity.class);
    	context.startActivity(intent);
    }
    public static void startWrapActivity(Context context){
    	Intent intent = new Intent(context,WrapActivity.class);
    	context.startActivity(intent);
    }
    
}
