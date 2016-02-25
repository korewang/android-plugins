package com.korewang.shuishui.unit_content_provider;

import com.korewang.shuishui.unit.DBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StudentContentProvider extends ContentProvider {
	// 清单文件要加provider
	private static final String AUTHORITY = "com.korewang.shuishui.unit_content_provider.StudentContentProvider";
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int STUDENT = 1;
	private static final int STUDENTS = 2;
	private DBHelper helper = null;
	static{
		URI_MATCHER.addURI(AUTHORITY, "student", STUDENTS);
		URI_MATCHER.addURI(AUTHORITY, "student/#", STUDENT);
	}
	public StudentContentProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		helper =  new DBHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor cursor = null;
		int flag = URI_MATCHER.match(uri);
		SQLiteDatabase databse = helper.getReadableDatabase();
		
		switch (flag) {
		case STUDENT:
			long stuid = ContentUris.parseId(uri);
			String whereString = " stuid ="+stuid;
			if(selection !=null && selection.equals("")){
				whereString+=selection;
			}
			cursor = databse.query("student", projection, whereString, selectionArgs, null, null, null);
			break;
		case STUDENTS:
			cursor = databse.query("student", projection, selection, selectionArgs, null, null, "name ASC");
			break;
		default:
			break;
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int flag = URI_MATCHER.match(uri);
		switch (flag) {
		case STUDENT:
			return "vnd.android.cursor.item/student";
		case STUDENTS:
			return "vnd.android.cursor.dir/students";
		default:
			break;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		int flag = URI_MATCHER.match(uri);
		Uri uri2 = null;
		SQLiteDatabase databse = helper.getWritableDatabase();
		switch (flag) {
		case STUDENTS:
			long  id = databse.insert("student", null, values);
			uri2 = ContentUris.withAppendedId(uri, id);
			break;

		default:
			break;
		}
		
		System.out.println("---->>>"+uri2.toString());
		return uri2;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int  count  = 0;
		int flag = URI_MATCHER.match(uri);
		SQLiteDatabase database = helper.getWritableDatabase();
		
		switch (flag) {
		case STUDENT:
			long stuid = ContentUris.parseId(uri);
			String whereString = " stuid ="+stuid;
			if(selection!=null && selection.equals("")){
				whereString +=selection;
			}
			count = database.delete("student", whereString, selectionArgs);
			break;
		case STUDENTS:
			count = database.delete("student", selection, selectionArgs);
			break;
		default:
			break;
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		int flag = URI_MATCHER.match(uri);
		SQLiteDatabase database = helper.getWritableDatabase();
		switch (flag) {
		case STUDENT:
			long stuid = ContentUris.parseId(uri);
			String whereString = " stuid ="+stuid;
			if(whereString!=null && whereString.equals("")){
				whereString +=selection;
			}
			count = database.update("student", values, whereString, selectionArgs);
			break;
		case STUDENTS:
			count = database.update("student", values, selection, selectionArgs);
			break;
		default:
			break;
		}
		return count;
	}

}
