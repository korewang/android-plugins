package com.korewang.shuishui.unit_content_provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

public class UnitTest extends AndroidTestCase {
	private static final String AUTHORITY = "com.korewang.shuishui.unit_content_provider.StudentContentProvider";

	public UnitTest() {
		// TODO Auto-generated constructor stub
	}

	public void add() {
		ContentResolver contentResolver = getContext().getContentResolver();
		ContentValues values = new ContentValues();
		Uri url = Uri
				.parse("content://com.korewang.shuishui.unit_content_provider.StudentContentProvider/student");
		values.put("name", "Bill_gate");
		
		contentResolver.insert(url, values);
	}

	public void delete() {
		ContentResolver contentResolver = getContext().getContentResolver();
		Uri url = Uri
				.parse("content://com.korewang.shuishui.unit_content_provider.StudentContentProvider/student/1");
		int count = contentResolver.delete(url, null, null);

		System.out.println("----->>>>" + count);
	}

	public void update() {
		ContentResolver contentResolver = getContext().getContentResolver();
		Uri url = Uri
				.parse("content://com.korewang.shuishui.unit_content_provider.StudentContentProvider/student/4");
		ContentValues values = new ContentValues();
		values.put("name", "myrose");
		values.put("age", 12);
		values.put("address", "Beijing");
		int countp = contentResolver.update(url, values, null, null);
		System.out.println("----->>update>>" + countp);
	}

	public void query(){
		ContentResolver contentResolver = getContext().getContentResolver();
		Uri url = Uri.parse("content://com.korewang.shuishui.unit_content_provider.StudentContentProvider/student");
		Cursor cursor = contentResolver.query(url, null, null, null, null);
		  // 要返回的数据字段  WHERE子句    WHERE 子句的参数    Order-by子句
		while (cursor.moveToNext()) {
			System.out.println("----->>>name-->>"+cursor.getString(cursor.getColumnIndex("name"))+"---age-->>"+cursor.getString(cursor.getColumnIndex("name")));
			
		}
	}
}
