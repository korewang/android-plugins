package com.korewang.shuishui.activitys;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.korewang.shuishui.R;

public class FragmentPageThree extends Fragment{
	private View rootView;
	/*
	 * import android.support.v4.app.Fragment;
	 * 而不是import android.Fragment;
	 * 
	 * */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_three, null);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
}
