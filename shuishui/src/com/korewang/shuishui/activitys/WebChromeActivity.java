package com.korewang.shuishui.activitys;

import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;
import com.korewang.shuishui.widget.HeaderView;
import com.korwang.shuishui.utils.MD5Utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WebChromeActivity extends Activity implements OnClickListener{
	private WebView _co;
	private Button id_rsa,id_rsa_public;
	private EditText inputText,canPaste;
	private TextView showText;
	private String str;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_chrome);
		
		HeaderView view = (HeaderView)findViewById(R.id.headerViewweb);
		view.setHeaderTitle("web");
		
		String url = "http://www.baidu.com"; // "file:///android_asset/testloading.html"
		_co = (WebView)findViewById(R.id.co);
		_co.getSettings().setJavaScriptEnabled(true);
		_co.setWebViewClient(new WebViewClient());
		_co.loadUrl(url);
		initsq();
		initView();
	}
	public void initView(){
		id_rsa = (Button)findViewById(R.id.id_rsa);
		
		id_rsa_public = (Button)findViewById(R.id.id_rsa_public);
		inputText = (EditText)findViewById(R.id.comemsm);
		showText  =(TextView)findViewById(R.id.msmContext);
		canPaste =  (EditText)findViewById(R.id.canPaste);
		id_rsa.setOnClickListener( this);
		id_rsa_public.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
	  
        switch (v.getId()) {
         	case R.id.id_rsa:
         		Log.i("key","id key");
         		 str = inputText.getText().toString();
         		showText.setText("加密：="+MD5Utils.convertMD5(str));
         		canPaste.setText(MD5Utils.convertMD5(str));
         		break;
         	case R.id.id_rsa_public:
         		Log.i("key", "id public ");
         		 str = inputText.getText().toString();
         		
         		showText.setText("加密：="+MD5Utils.convertMD5(MD5Utils.convertMD5(str)));
         		canPaste.setText(MD5Utils.convertMD5(MD5Utils.convertMD5(str)));
         		 break;
     		 default:
     			Log.i("key", "nothing ");
     			 break;
        }
	}
	public void initsq(){
		//打开或创建test.db数据库  
        SQLiteDatabase db = openOrCreateDatabase("shuishui.db", Context.MODE_PRIVATE, null);  
        db.execSQL("DROP TABLE IF EXISTS person");  
        //创建person表  
        db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");  
        Person person = new Person();  
        person.name = "john";  
        person.age = 30;  
        //插入数据  
        db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[]{person.name, person.age});  
          
        person.name = "david";  
        person.age = 33;  
        //ContentValues以键值对的形式存放数据  
        ContentValues cv = new ContentValues();  
        cv.put("name", person.name);  
        cv.put("age", person.age);  
        //插入ContentValues中的数据  
        db.insert("person", null, cv);  
          
        cv = new ContentValues();  
        cv.put("age", 35);  
        //更新数据  
        db.update("person", cv, "name = ?", new String[]{"john"});  
          
        Cursor c = db.rawQuery("SELECT * FROM person WHERE age >= ?", new String[]{"33"});  
        while (c.moveToNext()) {  
            int _id = c.getInt(c.getColumnIndex("_id"));  
            String name = c.getString(c.getColumnIndex("name"));  
            int age = c.getInt(c.getColumnIndex("age"));  
            Log.i("db", "_id=>" + _id + ", name=>" + name + ", age=>" + age);  
        }  
        c.close();  
          
        //删除数据  
        db.delete("person", "age < ?", new String[]{"35"});  
          
        //关闭当前数据库  
        db.close();  
          
        //删除test.db数据库  
//      deleteDatabase("test.db");  
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_chrome, menu);
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
