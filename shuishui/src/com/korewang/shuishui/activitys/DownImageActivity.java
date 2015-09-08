package com.korewang.shuishui.activitys;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;
import com.korewang.shuishui.widget.HeaderView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class DownImageActivity extends Activity implements View.OnClickListener{

	private static final String TAG = "DownImageActivity";
	private Context context;
	private Button downBtn,GetBatteryMSG,Scanwifi;
	private EditText  inputText;
	private ImageView showImageV;
	
	private ProgressDialog dialog;
	//建立一个hander  生命hander为静态的
//	private static Handler handler = new Handler();
	private static int IS_FINISH = 1;
	private String image_path = null;
	
	private int BatteryN;       //目前电量  
    private int BatteryV;       //电池电压  
    private double BatteryT;        //电池温度  
    private int BatteryS;  		//电量的总刻度
    private String BatteryStatus;   //电池状态  
    private String BatteryTemp;     //电池使用情况  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.down_image_activity_ly);
		downBtn = (Button)findViewById(R.id.DownBtn);
		inputText = (EditText)findViewById(R.id.inputText);
		showImageV = (ImageView)findViewById(R.id.showImage);
		GetBatteryMSG = (Button)findViewById(R.id.GetBatteryMSG);
		Scanwifi = (Button)findViewById(R.id.ScanWifi);
		HeaderView view = (HeaderView)findViewById(R.id.headerDownView);
		view.setHeaderTitle("DOWN_image");
		GetBatteryMSG.setOnClickListener(this);
		Scanwifi.setOnClickListener(this);
		downBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image_path = inputText.getText().toString();
				downBtn.setText("DownBTN");
				if(image_path.length() <8){
					image_path = "http://www.baidu.com/img/bdlogo.png";
					
				}
				dialog.show();
				new Thread(new MyThread()).start();
			}
		});
		initDialog();
	}
	
	@Override
	 public void onClick(View v){
		switch (v.getId()) {
		case R.id.GetBatteryMSG:
				//起动查询电量
				//注册广播接受者java代码
				IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
				//创建广播接受者对象
				BatteryReceiver batteryReceiver = new BatteryReceiver();
				
				//注册receiver
				registerReceiver(batteryReceiver, intentFilter);
			break;
		case R.id.ScanWifi:
			WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
			List<ScanResult> wifiList = wm.getScanResults();  
			for (int i = 0; i < wifiList.size(); i++) {  
			ScanResult result = wifiList.get(i);  
			Log.d("wifi===========", "level="+result.level+"ssid="+result.SSID+"bssid=" + result.BSSID);  
			}  
			break;
		default:
			break;
		}
	}
	private void initDialog(){
		dialog = new ProgressDialog(context);
		dialog.setTitle("提示");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);
	}
	private  Handler handler = new Handler() {
        // 在Handler中获取消息，重写handleMessage()方法
        @Override
        public void handleMessage(Message msg) {            
            // 判断消息码是否为1
            if(msg.what==IS_FINISH){
                byte[] data=(byte[])msg.obj;
                Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
                showImageV.setImageBitmap(bmp);
                dialog.dismiss();
            }
        }
    };
	public class MyThread implements Runnable{

        @Override
        public void run() {
        	
    	    	  	HttpClient httpClient = new DefaultHttpClient();
    	            HttpGet httpGet = new HttpGet(image_path);
    	            HttpResponse httpResponse = null;
	    	            try {
	    	                httpResponse = httpClient.execute(httpGet);
	    	                if (httpResponse.getStatusLine().getStatusCode() == 200) {
	    	                    byte[] data = EntityUtils.toByteArray(httpResponse
	    	                            .getEntity());
	    	                    // 获取一个Message对象，设置what为1
	    	                    Message msg = Message.obtain();
	    	                    msg.obj = data;
	    	                    msg.what = IS_FINISH;
	    	                    // 发送这个消息到消息队列中
	    	                    //handler.sendMessage(msg);
	    	                    handler.sendMessageDelayed(msg, 5000);
	    	                }
	    	            } catch (Exception e) {
	    	                e.printStackTrace();
	    	            }
    	      		
        }
	}
	
	//获取电池电量
	/**
	 * 广播接受者
	 */
	class BatteryReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//判断它是否是为电量变化的Broadcast Action
			if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
				
					//电量的总刻度
				 	BatteryS = intent.getIntExtra("scale", 100);
				 	BatteryN = intent.getIntExtra("level", 0);    //目前电量  
	                BatteryV = intent.getIntExtra("voltage", 0);  //电池电压  
	                BatteryT = intent.getIntExtra("temperature", 0);  //电池温度  
				//把它转成百分比
				downBtn.setText("电池电量为"+((BatteryN*100)/BatteryS)+"%");
				
				 switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN))   
	                {  
	                case BatteryManager.BATTERY_STATUS_CHARGING:  
	                    BatteryStatus = "充电状态";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_DISCHARGING:  
	                    BatteryStatus = "放电状态";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:  
	                    BatteryStatus = "未充电";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_FULL:  
	                    BatteryStatus = "充满电";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_UNKNOWN:  
	                    BatteryStatus = "未知道状态";  
	                    break;  
	                }  
	                  
	                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN))   
	                {  
	                case BatteryManager.BATTERY_HEALTH_UNKNOWN:  
	                    BatteryTemp = "未知错误";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_GOOD:  
	                    BatteryTemp = "状态良好";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_DEAD:  
	                    BatteryTemp = "电池没有电";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:  
	                    BatteryTemp = "电池电压过高";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_OVERHEAT:  
	                    BatteryTemp =  "电池过热";  
	                    break;  
	                }  
	                Toast.makeText(context, "目前电量为" + BatteryN + "% --- " + 
            						BatteryStatus + "\n" + "电压为" + BatteryV + 
            						"mV --- " + BatteryTemp + "\n" + 
            						"温度为" + (BatteryT*0.1) + "℃", 
            						Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.down_image, menu);
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
