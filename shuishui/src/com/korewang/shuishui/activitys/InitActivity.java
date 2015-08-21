package com.korewang.shuishui.activitys;

import java.util.ArrayList;
import java.util.HashMap;

import com.korewang.shuishui.R;
import com.korewang.shuishui.widget.HeaderView;
import com.korwang.shuishui.utils.BadgeUtil;
import com.korwang.shuishui.utils.UIControl;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InitActivity extends Activity {
	private Context mContext;
	private ListView mListView;
	private String[] data = new String[]{"获取GPS","获取手机app信息","愉快的去玩耍","sqlite"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_activity_ly);
		mContext = this;
		initView();
		
		
	}
	
	public void initView(){
		mListView = (ListView)findViewById(R.id.init_listView);
		View goback = findViewById(R.id.goback);
		goback.setVisibility(View.GONE);
		initValidate();
		
	}
	public void initValidate(){
		//mListView.setAdapter(new ArrayAdapter<String>(this,
          //      android.R.layout.simple_list_item_1, data));
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();/*在数组中存放数据*/
		 
		for(int i=0;i<data.length;i++)  
		        {  
		            HashMap<String, Object> map = new HashMap<String, Object>();  
		            map.put("ItemImage", R.drawable.ss_icon);//加入图片            map.put("ItemTitle", "第"+i+"行");  
		            map.put("ItemText", data[i]);  
		            listItem.add(map);  
		        } 
		SimpleAdapter myAdapter = new SimpleAdapter(this,listItem,R.layout.init_listview,new String[] {"ItemImage","ItemText"},   
				new int[] {R.id.ItemImage,R.id.ItemText} );
		
		mListView.setAdapter(myAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                    //点击后在标题上显示点击了第几行                    setTitle("你点击了第"+arg2+"行");
            	
            	if(arg2==0 ){
            		UIControl.startGPSActivity(mContext);            	
            	}else if(arg2==1){
            		UIControl.startListAppActivity(mContext);
            	}
            	else if(arg2==2){            		
            		Toast.makeText(mContext, "你点击了第"+arg2+"行的"+data[arg2], Toast.LENGTH_LONG).show();
            		UIControl.startBmapActivity(mContext);
            	}else if(arg2==3){
            		UIControl.startWebViewActivity(mContext);
            	}
            }
        });
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
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
