package com.korewang.shuishui.activitys;

import java.util.List;
import java.util.Map;

import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;
import com.korewang.shuishui.adapter.AppListAdapter;
import com.korwang.shuishui.utils.BadgeUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ListAppActivity extends Activity {
	private ListView app_listView;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_app_activity_ly);
		mContext = this;
		initAppView();
		BadgeUtil.setBadgeCount(mContext, 3);
	}
	public void initAppView(){
		app_listView = (ListView)findViewById(R.id.app_listView);
		
		initAppData();
	}
	public void initAppData(){
		AppListAdapter madapter = new AppListAdapter(mContext);
		app_listView.setAdapter(madapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
