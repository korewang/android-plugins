package com.korewang.shuishui.unit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String name = "myunit.db";
	private static final int version = 6;

	// 数据库表的重命名
	public static final String TEMP_SQL_CREATE_TABLE_SUBSCRIBE = "alter table student rename to student_r_name";
	// 原表的数据结构
	private static final String SQL_CREATE_TABLE_SUBSCRIBE = "create table  if not exists student(stuid integer primary key autoincrement,name varchar(64))";

	// 然后把备份表student_r_name中的数据copy到新创建的数据库表student中，这个表student没发生结构上的变化
	public static final String INSERT_SUBSCRIBE = "select 'insert into student (stuid,name) values ('''||stuid||''','''||name||'''')'  as insertSQL from student_r_name";

	SQLiteDatabase dbase;

	public DBHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
		// testvoid();
	}

	public void testvoid() {
		dbase = this.getReadableDatabase();
		// 暂时用dbase

		dbase.execSQL("DROP TABLE IF EXISTS student_r_name");
		Cursor cursor = dbase.rawQuery("SELECT * FROM student", null);
		System.out.println("cursor.getColumnCount()--->"
				+ cursor.getColumnCount());
		while (cursor.moveToNext()) {
			System.out.println("sutid--->>"
					+ cursor.getString(cursor.getColumnIndex("stuid"))
					+ " name----->>>"
					+ cursor.getString(cursor.getColumnIndex("name"))
					+ "age----->>"
					+ cursor.getString(cursor.getColumnIndex("age")));
		}

	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table student(stuid integer primary key autoincrement,name varchar(64),age integer,address varchar(128),ids integer)";
		db.execSQL(sql);
	}

	/**
	 * 
	 * 设计思想 1. 设计数据库升级 低版本到高版本 防止出错要一步步的升级 2. 重命名原来低版本的大数据库 3. 创建新的数据库 4. 数据迁移
	 * 为什么要在方法里写for循环
	 * ，主要是考虑到夸版本升级，比如有的用户一直不升级版本，数据库版本号一直是1，而客户端最新版本其实对应的数据库版本已经是6了
	 * ，那么我中途可能对数据库做了很多修改，通过这个for循环，可以迭代升级，不会发生错误。
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		ContentValues initialValues = new ContentValues();
		for (int j = oldVersion; j <= newVersion; j++) {
			switch (j) {
			case 6:
				// 创建临时表
				db.execSQL(TEMP_SQL_CREATE_TABLE_SUBSCRIBE);
				// 数据库版本2的sql类型
				String sql = "create table student(stuid integer primary key autoincrement,name varchar(64),age integer,address varchar(128),ids integer)";
				db.execSQL(sql);
				// onCreate(db);
				// rename的数据库数据迁移到新创建的数据库里
				Cursor cursor = db.rawQuery("SELECT * FROM student_r_name",
						null);

				while (cursor.moveToNext()) {

					initialValues.put("ids",
							cursor.getString(cursor.getColumnIndex("stuid")));
					initialValues.put("name",
							cursor.getString(cursor.getColumnIndex("name")));
					db.insert("student", null, initialValues);
				}
				cursor.close();
				db.execSQL("DROP TABLE IF EXISTS student_r_name");
				break;
			}
		}

	}

}
