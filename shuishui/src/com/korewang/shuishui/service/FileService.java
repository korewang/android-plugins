package com.korewang.shuishui.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 
 * @author wangxinyan
 * @date 2015年12月8日
 * &*& by class FileService
 * 业务bean实现对数据操作
 */
public class FileService {
	private DBOpenHelper openHelper;
	public FileService(Context context){
		openHelper = new DBOpenHelper(context);// 此处根据DBOpenHelper类上下文对象实例化数据库管理器
	}
	/**
	 * 
	 * TODO: 获取特定的uri 的每一条现成已经下载的文件长度
	 * @param path
	 * @return null
	 */
	public Map<Integer,Integer> getData(String path){
		
		//获取刻度的数据库句柄，一般情况下在该操作的内部实现中其返回的其实是科协的数据库句柄
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?",new String[]{path});
		Map<Integer, Integer>data = new HashMap<Integer, Integer>();
		//根据下载路径查询所有的线程下载数据，返回的cursor指向第一条记录之前
		while(cursor.moveToNext()){//从第一条记录开始遍历cursor对象
			data.put(cursor.getInt(0), cursor.getInt(1));
			//不确定是否  注释   把线程id 和该线程已下载的长度设置进data hashmap里 
			//data.put(cursor.getInt(cursor.getColumnIndexOrThrow("threadid")), cursor.getInt(cursor.getColumnIndexOrThrow("downlength")));
		}
		cursor.close();
		db.close();
		return data;
	}
	/**
	 *   path 下载路径
	 *   map 现在的id 和已经下载的长度的集合
	 *    
	 */
	public void save(String path, Map<Integer,Integer> map){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		//读写句柄
		db.beginTransaction();//开始事物，此处要插入多笔数据
		try{
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){
				db.execSQL("insert into filedownlog(downpath, "
						+ "threadid, downlength) values(?,?,?)", 
						new Object[]{path,entry.getKey(),entry.getValue()});
				
				//插入特定下载路径特定现成id已经下载的数目
			}
			db.setTransactionSuccessful();
			// 设置事务执行的标志为成功
		}finally{
			db.endTransaction();
			//结束一个事务，如果事务设置了成功标志，则提交事务，否则回滚事务
		}
		db.close();
	}
	/**
	 * 实时更新每条线程已经下载的文件长度
	 * @param path 
	 * @param map
	 */
	public void update(String path, int threadid, int pos){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("update filedownlog set downlength=?where downpath=? and threadid=?",new Object[]{pos,path,threadid});
		db.close();
	}
	/**
	 * @param path
	 * 当下载文件完成后，删除对应的下载记录
	 */
	public void delete(String path){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		//删除特定下载路径的线程记录
		db.execSQL("delete from filedownlog where downpath=?",new Object[]{path});
		db.close();
	}
}

/**
	 SQLite数据库的童鞋对 Cursor 应该不陌生，加深自己和大家对Android 中使用 Cursor 的理解。
	关于 Cursor
	在你理解和使用 Android Cursor 的时候你必须先知道关于 Cursor 的几件事情：
	Cursor 是每行的集合。使用 moveToFirst() 定位第一行。你必须知道每一列的名称。你必须知道每一列的数据类型。Cursor 是一个随机的数据源。所有的数据都是通过下标取得。
	关于 Cursor 的重要方法：
	·close()——关闭游标，释放资源
	·copyStringToBuffer(int columnIndex, CharArrayBuffer buffer)——在缓冲区中检索请求的列的文本，将将其存储
	·getColumnCount()——返回所有列的总数
	·getColumnIndex(String columnName)——返回指定列的名称，如果不存在返回-1
	·getColumnIndexOrThrow(String columnName)——从零开始返回指定列名称，如果不存在将抛出IllegalArgumentException 异常。
	·getColumnName(int columnIndex)——从给定的索引返回列名
	·getColumnNames()——返回一个字符串数组的列名
	·getCount()——返回Cursor 中的行数
	·moveToFirst()——移动光标到第一行
	·moveToLast()——移动光标到最后一行
	·moveToNext()——移动光标到下一行
	·moveToPosition(int position)——移动光标到一个绝对的位置
	·moveToPrevious()——移动光标到上一行
 */



