package com.korewang.shuishui.activitys;

//import android.app.Fragment;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;

import com.korewang.shuishui.R;

public class FragmentPageTwo extends Fragment implements View.OnClickListener,View.OnTouchListener{
	private static final String TAG = "FragmentPageTwo";
	private Context context;
	private View rootView;
	private RelativeLayout mfragment_two;
	private String[] province;
	private MultiAutoCompleteTextView multiAutoCompleteTextView;
	private static final String D_KEY="d_key";
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		String tempDatas = "N times comming to GestrueZoom";
		outState.putString(D_KEY, tempDatas);
		Log.i(TAG, "see you");///home键状态挂起
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		Log.d(TAG, "super.onAttach(activity)");
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(TAG, "super.onCreate()");
		if(savedInstanceState != null){
			String d_key = savedInstanceState.getString(D_KEY);
			Log.i(TAG, "see you again "+d_key);
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "super.onActivityCreated()");
	}
	@Override
	public void onStart(){
		super.onStart();
		Log.d(TAG, "super.onStart()");
	}
	@Override
	public void onResume(){
		super.onResume();
		Log.d(TAG, "super.onResume();");
	}
	@Override
	public void onPause(){
		super.onPause();
		Log.d(TAG, "super.onPause();");
	}
	@Override
	public void onStop(){
		super.onStop();
		Log.d(TAG, "super.onStop();");
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(TAG, "super.onDestroy();");
	}
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		Log.d(TAG, "super.onDestroyView()");
	}
	@Override
	public void onDetach(){
		super.onDetach();
		Log.d(TAG, "super.onDetach()");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		Log.d(TAG, "onCreateView");
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_two, null);
			
			initView();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	public void initView(){
		/*Bundle bundle = getArguments();  
		long deptId = bundle.getLong("id");  
		String deptName = bundle.getString("name");
		Log.d(TAG, deptName+deptId);*/
		 mfragment_two = (RelativeLayout)rootView.findViewById(R.id.fragment_two);
		 mfragment_two.setOnTouchListener(this);
	     multiAutoCompleteTextView=(MultiAutoCompleteTextView)rootView.findViewById(R.id.multiAutoCompleteTextView);
		 //创建适配器
         province =getResources().getStringArray(R.array.province); 
	     ArrayAdapter<String> adapter=new ArrayAdapter<String>(
	             getActivity(), 
	             android.R.layout.simple_dropdown_item_1line, 
	             province);
	     //构造器实例
	     multiAutoCompleteTextView.setAdapter(adapter);
	     //	设置输入多少字符后提示，默认值为2
	     multiAutoCompleteTextView.setThreshold(1);
	     //用户必须提供一个MultiAutoCompleteTextView.Tokenizer用来区分不同的子串。
	     multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	     
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fragment_two:
			//to do other  things
			break;

		default:
			break;
		}
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fragment_two:
			InputMethodManager ih = (InputMethodManager)  
			context.getSystemService(Context.INPUT_METHOD_SERVICE);  
			ih.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			mfragment_two.setFocusable(true);
			mfragment_two.setFocusableInTouchMode(true);
			mfragment_two.requestFocus();
			break;
		default:
			break;
		}
		return false;
	}
}
