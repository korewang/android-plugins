package com.korewang.shuishui.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korewang.shuishui.R;
import com.korewang.shuishui.plugins.GetPhoneInfo;

/*
 * import android.support.v4.app.Fragment;
 * 而不是import android.Fragment;
 * 
 * */
public class FragmentPageThree extends Fragment{
	private View rootView;
	private TextView mPhoneInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_three, null);
			initView();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	public void initView(){
		mPhoneInfo = (TextView)rootView.findViewById(R.id.myPhoneInfo);
		GetPhoneInfo siminfo = new GetPhoneInfo(getActivity());  
        String sims = "getProvidersName:"+siminfo.getProvidersName()+
	        				"\r\ngetNativePhoneNumber:"+siminfo.getNativePhoneNumber()+
	        				"\r\ngetPhoneInfo:"+siminfo.getPhoneInfo();
        mPhoneInfo.setText(sims);
	}
}
