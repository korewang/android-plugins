package com.korewang.shuishui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.korewang.shuishui.R;
import com.korewang.shuishui.animation.activitys.GestrueZoomActivity;
import com.korewang.shuishui.widget.HeaderView;

public class BmapActivity extends Activity {
	private TextView bMapPosition;
	MapView mMapView = null;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.bmap_activity_ly);
		 //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);  
		initBmapView();
		
		
	}
	public void initBmapView(){
		bMapPosition = (TextView)findViewById(R.id.setPosition);
		HeaderView headerview = (HeaderView)findViewById(R.id.headerView);
		headerview.setHeaderTitle("百度map");
		
		
		bMapPosition.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent m = new Intent(BmapActivity.this,GestrueZoomActivity.class);
				 startActivityForResult(m, 100);
				// startActivity(m);
				
			}
		});
	}
	
	/**/
	 @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	    {  super.onActivityResult(requestCode, resultCode, data);
		
	        switch (requestCode)  
	        {  
	        case 100:  
	        	 Bundle MoonBuddles = data.getExtras();  
		            String MoonMessages = MoonBuddles.getString("FromTwo");  
	            Log.i("base","MoonMessages:"+MoonMessages);
	             
	            break;  
	        case 200:  
	            Bundle MoonBuddle = data.getExtras();  
	            String MoonMessage = MoonBuddle.getString("FromTwo");  
	            break;  
	        }  
	    } 
	
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
    }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
    }  
}
