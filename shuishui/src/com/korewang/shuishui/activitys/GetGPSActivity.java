package com.korewang.shuishui.activitys;

import java.util.Iterator;

import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;
import com.korwang.shuishui.utils.BadgeUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class GetGPSActivity extends Activity {

    private String TAG = "GetGPSActivity";
    private  LocationManager locationManager;
    private TextView _myTest,txt_gps_state,txt_info;
    private Button _gps;
    
    private WebView _content;
    private Context contexts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_gps_activity_ly);
		contexts = this;
		initView();
		initWeb();
		BadgeUtil.resetBadgeCount(contexts);
	}
	public void initWeb(){
		_content = (WebView)findViewById(R.id.content);
		
		
		String ms = "<div style='font-size:30px;border:1px solid red;'>我的ccccc</div><a href='http://www.baidu.com'>百度</a>";
		
		
		_content.getSettings().setJavaScriptEnabled(true);
		_content.setWebViewClient(new WebViewClient());
		//_content.loadUrl("http://www.baidu.com");
		//_content.loadData(ms, "text/html;charset=UTF-8",  "utf-8");
		// String tpl = getFromAssets("web_tpl.html"); 
		_content.loadDataWithBaseURL(null,ms, "text/html",  "utf-8", "file:///android_asset/testloading.html");
		_content.setWebViewClient(new WebViewClient(){
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					
					return false;
				}
			}); 
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && _content.canGoBack()) {
			_content.goBack();
		return true;
		}
		return super.onKeyDown(keyCode, event);
	} 
	
	@Override
	protected void onResume(){
		super.onResume();
		//initGPS();
		//initWeb();
	}
	public void initView(){
		
		_gps = (Button)findViewById(R.id.gps_btn);
		txt_gps_state = (TextView)findViewById(R.id.getGps);
        txt_info = (TextView)findViewById(R.id.txt_info);
        
        int height = _gps.getLayoutParams().height;
        int width =  _gps.getLayoutParams().width;
        
        final String sText = "<div style='font-size:30px;border:1px solid red;'>测试自定义标签：</div><br><h1><mxgsa>测试自定义标签</mxgsa></h1>";
        txt_gps_state.setText(Html.fromHtml(sText));
        
        _gps.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
           
            	initGPS();

            }
        });
	}
	public void initGPS(){
		 Log.i(TAG,"init data");
		 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
	            txt_gps_state.setText("OFF");
	            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            startActivityForResult(intent, 0);
	            return;
	        } else {
	            txt_gps_state.setText("ON");
	        }
	        String bestProvider = locationManager.getBestProvider(getCriteria(),
	                true);
	        Location location = locationManager.getLastKnownLocation(bestProvider);
	        updateView(location);
	        // locationManager.addGpsStatusListener(listener);
	        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
	                1000, 0, locationListener);
	}

	private void updateView(Location location) {
        if (location != null) {
            txt_info.setText("设备位置信息\n经度：");
            txt_info.append(String.valueOf(location.getLongitude()));
            txt_info.append("\n纬度：");
            txt_info.append(String.valueOf(location.getLatitude()));
            txt_info.append("\n海拔：");
            txt_info.append(String.valueOf(location.getAltitude()));
        } else {
            // 清空EditText对象
            txt_info.setText("");
        }
    }
	 private GpsStatus.Listener listener = new GpsStatus.Listener() {

	        @Override
	        public void onGpsStatusChanged(int event) {
	            switch (event) {
	                // 第一次定位
	                case GpsStatus.GPS_EVENT_FIRST_FIX:
	                    Log.i(TAG, "第一次定位");
	                    break;
	                // 卫星状态改变
	                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	                    Log.i(TAG, "卫星状态改变");
	                    // 获取当前状态
	                    GpsStatus gpsStatus = locationManager.getGpsStatus(null);
	                    // 获取卫星颗数的默认最大值
	                    int maxSatellites = gpsStatus.getMaxSatellites();
	                    // 创建一个迭代器保存所有卫星
	                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
	                            .iterator();
	                    int count = 0;
	                    while (iters.hasNext() && count <= maxSatellites) {
	                        GpsSatellite s = iters.next();
	                        count++;
	                    }
	                    System.out.println("搜索到：" + count + "颗卫星");
	                    break;
	                // 定位启动
	                case GpsStatus.GPS_EVENT_STARTED:
	                    Log.i(TAG, "定位启动");
	                    break;
	                // 定位结束
	                case GpsStatus.GPS_EVENT_STOPPED:
	                    Log.i(TAG, "定位结束");
	                    break;
	            }
	        }
	    };
	    private Criteria getCriteria() {
	        Criteria criteria = new Criteria();
	        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        // 设置是否要求速度
	        criteria.setSpeedRequired(false);
	        // 设置是否允许运营商收费
	        criteria.setCostAllowed(false);
	        // 设置是否需要方位信息
	        criteria.setBearingRequired(false);
	        // 设置是否需要海拔信息
	        criteria.setAltitudeRequired(true);
	        // 设置对电源的需求
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        return criteria;
	    }

	    private LocationListener locationListener = new LocationListener() {

	        /**
	         * 位置信息变化时触发
	         */
	        public void onLocationChanged(Location location) {
	            updateView(location);
	            Log.i(TAG, "时间：" + location.getTime());
	            Log.i(TAG, "经度：" + location.getLongitude());
	            Log.i(TAG, "纬度：" + location.getLatitude());
	            Log.i(TAG, "海拔：" + location.getAltitude());
	        }

	        /**
	         * GPS状态变化时触发
	         */
	        public void onStatusChanged(String provider, int status, Bundle extras) {
	            switch (status) {
	                // GPS状态为可见时
	                case LocationProvider.AVAILABLE:
	                    Log.i(TAG, "当前GPS状态为可见状态");
	                    break;
	                // GPS状态为服务区外时
	                case LocationProvider.OUT_OF_SERVICE:
	                    Log.i(TAG, "当前GPS状态为服务区外状态");
	                    break;
	                // GPS状态为暂停服务时
	                case LocationProvider.TEMPORARILY_UNAVAILABLE:
	                    Log.i(TAG, "当前GPS状态为暂停服务状态");
	                    break;
	            }
	        }

	        /**
	         * GPS开启时触发
	         */
	        public void onProviderEnabled(String provider) {
	            Location location = locationManager.getLastKnownLocation(provider);
	            updateView(location);
	        }

	        /**
	         * GPS禁用时触发
	         */
	        public void onProviderDisabled(String provider) {
	            updateView(null);
	        }

	    };
}
/*
 * 在使用loadDataWithBaseURL时，需要注意的就是 baseUr：虽然API上写的是要传一个Url，但我在用时，
 * 发现传一个Url并不可以，我发现这个就是一个标志位，用来标志当前页面的Key值的，而historyUrl就是一个value值
 * ，在加载时，它会把baseUrl和historyUrl传到List列表中，当作历史记录来使用，当前进和后退时，
 * 它会通过baseUrl来寻找historyUrl的路径来加载historyUrl路径来加载历史界面，
 * 需要注意的就是history所指向的必须是一个页面，并且页面存在于SD卡中或程序中（assets），loadDataWithBaseURL，
 * 它本身并不会向历史记录中存储数据，要想实现历史记录，需要我们自己来实现，也许是我的技术有限，
 * 我有了比较笨的访求来实现：就是在加载页面时，我把数据另外的写到一个html页面中，并把它保存到SD中，
 * 当点击返回时，它会通过historyUrl指向的路径来加载页面，这样就解决了历史记录问题。
 
上面这两种方法，我建议使用后者，虽然loadData的历史记录不需要我们自己来实现，但在使用时，
我们必须把所有的%，#，\，?转换掉，在转换时，也许会遇到别的困难，我也没有测完。
这就两个加载上后者比前者快一到两倍。
 * 
 * */
