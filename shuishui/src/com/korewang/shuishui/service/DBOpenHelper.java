package com.korewang.shuishui.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{
	private static final String TAG = "DBOpenHelper";
	private static final String DBNAME = "eric.db";
	private static final int VERSION = 1;
	
	/**
	 * 
	 * @param SQLITE 管理器  实现创建数据库和表，但版本变化时实现对表的数据库的操作
	 * @param AUTHOR WANGXY
	 * @param version 1
	 */
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
		//构造里的name 也可以使fileServerice给的
	}
	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// create db table  filedownlog表
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement,downpath varchar(100),threadid INTEGER,downlength INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//当版本变化时系统会调用该方法
		db.execSQL("DROP TABLE IF EXISTS filedownlog");//删除数据表，实际项目要备份
		
		onCreate(db);//重新创建数据表，也可以根据自己的需求创建新的数据表
	}

}
