package com.korewang.shuishui.animation.activitys;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MySharedPreferenceActivity extends Activity implements View.OnClickListener{
	private static final String TAG = "MySharedPreferenceActivity";
	SharedPreferences preference;
	SharedPreferences.Editor editor;
	private  Button mSave,mCheck,mWrite,mRead;
	private EditText medt;
	private int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_shared_preference_ly);
		mSave = (Button)findViewById(R.id.save);
		mCheck  = (Button)findViewById(R.id.check);
		mWrite  = (Button)findViewById(R.id.write);
		mRead  = (Button)findViewById(R.id.read);
		medt = (EditText)findViewById(R.id.edt);
		mSave.setOnClickListener(this);
		mCheck.setOnClickListener(this);
		mWrite.setOnClickListener(this);
		mRead.setOnClickListener(this);
		preference = getSharedPreferences("count", MODE_PRIVATE);//实例化
		editor = preference.edit();
		count  = preference.getInt("usedapp_count", 0);
		Log.i(TAG, ""+count);
		
		
	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.save:
			editor.putInt("usedapp_count", ++count);
			editor.putBoolean("is_booean", true);
			Set<String> sen = new HashSet<String>();
			sen.add("a");
			sen.add("b");
			sen.add("CD");
			editor.putStringSet("st_set", sen);
			editor.commit();
			break;
		case R.id.check:
			String content="";
			 Map<String, ?> allContent = preference.getAll();  
            //注意遍历map的方法  
            for(Map.Entry<String, ?>  entry : allContent.entrySet()){  
                content+=(entry.getKey()+":"+entry.getValue()+"\\");  
            }  
            Log.i(TAG,content);
			break;
		case R.id.write:
			try{
				String pl = medt.getText().toString();
				FileOutputStream fos = openFileOutput("shared.bin", MODE_PRIVATE);
				PrintStream st = new PrintStream(fos);
				st.println(pl);
				st.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			break;
		case R.id.read:
			try{
				FileInputStream fis = openFileInput("shared.bin");
				byte[] buf = new byte[1024];
				int hasRead =0;
				StringBuilder sbd = new StringBuilder("");
				while((hasRead=fis.read(buf))>0){
					sbd.append(new String(buf,0,hasRead));
				}
				//fis.close();
				final TextView tv = (TextView)findViewById(R.id.msharedtv);
				tv.setText(sbd.toString());
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
		default:
			 
			break;
		}
	}
}
