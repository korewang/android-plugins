package com.korewang.shuishui.activitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.baidu.navisdk.util.common.DateTimeUtils;
import com.korewang.shuishui.R;
import com.korewang.shuishui.widget.HeaderView;
import com.korwang.shuishui.utils.BadgeUtil;
import com.korwang.shuishui.utils.TestBroadcast;
import com.korwang.shuishui.utils.TestThread;
import com.korwang.shuishui.utils.UIControl;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
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
	private String[] data = new String[]{"获取GPS","获取手机app信息","百度定位","sqlite","下载一个图片显示在imageview里","Fragment learning","CameraSet","a","v","dddd","ddaaa","dsssaa"};
	public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	private LocalBroadcastManager mLocalBroadcastManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_activity_ly);
		mContext = this;
		initView();
		 
         String name = "我的水水";
       //  addShortcut();
         Log.i("DateUtils", DateUtils.formatElapsedTime(0)+(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new java.util.Date()));
        
         Long time = System.currentTimeMillis()/1000;
         
         Log.i("Timestamp", time.toString());
         mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
         IntentFilter mintentfiler = new IntentFilter();
         mintentfiler.addAction("add_action");
         mintentfiler.addAction("add_action_name");
         mLocalBroadcastManager.registerReceiver(mreceive, mintentfiler);
          new Thread(new TestThread(mContext)).start();
	}
	
	private BroadcastReceiver mreceive = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context,Intent intent){
			if(intent.getAction() == "add_action"){
				Log.i("BroadcastReceiver", "add_action");
			}else if(intent.getAction() == "add_action_name"){
				Log.i("BroadcastReceiver", "add_action_name");
			}
			
		}
	};
	protected boolean addShortcut() {
    	try {
	        if(!isShortCutExists()) {
	            Intent shortcut = new Intent();
	
	            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "ss");
	            shortcut.putExtra("duplicate", false); //不允许重复创建
	            
	            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, getIntentShortcut());
	            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
	            shortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	            sendOrderedBroadcast(shortcut,null);
	            return true;
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    protected Intent getIntentShortcut() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setComponent(getComponentName());
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        return i;
    }
  	protected boolean isShortCutExists() {
        Cursor c = null;
        try {
            ContentResolver cr = this.getContentResolver();
            String AUTHORITY;
            if(Build.VERSION.SDK_INT > 8) {
            	AUTHORITY = "com.android.launcher2.settings";
            } else {
            	AUTHORITY = "com.android.launcher.settings";
            }
            Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
            c = cr.query(CONTENT_URI, null, "title = '" + "ss" + "'",null, null);
            return (c != null && c.getCount() > 0);
        } finally {
            if(c != null) {
                c.close();
            }
        }
    }
	private void addShortcut(String name) {
        Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this,
                        R.drawable.ic_launcher));

        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(this, InitActivity.class);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        addShortcutIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        sendBroadcast(addShortcutIntent);
    }
	public void initView(){
		mListView = (ListView)findViewById(R.id.init_listView);
		View goback = findViewById(R.id.goback);
		goback.setVisibility(View.GONE);
		initValidate();
		mListView.setSelection(1);
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
            	}else if(arg2==4){
            		UIControl.startDownImageActivity(mContext);
            	}else if(arg2==5){
            		UIControl.startFragmentActivity(mContext);
            	}else if(arg2==6){
            		UIControl.startCameraSettingActivity(mContext);
            		
            	}
            }
        });
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mLocalBroadcastManager.unregisterReceiver(mreceive);
		Log.i("mLocalBroadcastManager", "mLocalBroadcastManager-destroy");
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
			 Toast.makeText(this, "action_settings", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	   * 重写onKeyDown方法可以拦截系统默认的处理
	   */
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	      Toast.makeText(this, "后退键", Toast.LENGTH_SHORT).show();
	      finish();
	      return true;
	    } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
	      Toast.makeText(this, "声音+", Toast.LENGTH_SHORT).show();
	      return false;
	    } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
	      Toast.makeText(this, "声音-", Toast.LENGTH_SHORT).show();
	      return false;
	    } else if (keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
	      Toast.makeText(this, "静音", Toast.LENGTH_SHORT).show();
	      return false;
	    } else if (keyCode == KeyEvent.KEYCODE_HOME) {
	      Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
	      return true;
	    }
	    return super.onKeyDown(keyCode, event);
	  }
}
