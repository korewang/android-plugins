package com.korewang.shuishui.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity{

	private static final String TAG="BaseActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(TAG, getClass().getSimpleName());
	}
}
