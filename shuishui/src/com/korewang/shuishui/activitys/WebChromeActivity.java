package com.korewang.shuishui.activitys;

import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;
import com.korewang.shuishui.widget.HeaderView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebChromeActivity extends Activity {
	private WebView _co;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_chrome);
		
		HeaderView view = (HeaderView)findViewById(R.id.headerViewweb);
		view.setHeaderTitle("web");
		
		String url = "http://www.baidu.com";
		_co = (WebView)findViewById(R.id.co);
		_co.getSettings().setJavaScriptEnabled(true);
		_co.setWebViewClient(new WebViewClient());
		_co.loadUrl(url);
		initsq();
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
