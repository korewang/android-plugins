package com.korewang.shuishui.service;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.util.Log;

import com.korwang.shuishui.utils.Data;

public class Contact_connection {
	private static final String TAG = "Contact_connection";
	public  Context context;
	public  ContentResolver resolver ;
	private  Uri co = People.CONTENT_URI;
	public Contact_connection(Context ctx){
		this.context = ctx;
		resolver = context.getContentResolver();
	}
	/**
	 * TODO:获取信息 联系人
	 */
	public void getContactInfo(){
		String[] project = {People._ID,People._COUNT,People.NAME,People.NUMBER,People.NUMBER_KEY};
       
        //resolver.query  replace   managerQuery
        Cursor mcour = resolver.query(co, null, null, null, People.NAME+" ASC");
		if(mcour.moveToFirst()){
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			String name;
			String _id;
			int nameColumn = mcour.getColumnIndex(People.NAME);
			int idColumn = mcour.getColumnIndex(People._ID);
			
			do{
				name = mcour.getString(nameColumn);
				_id = mcour.getString(idColumn);
				//sb.append("{'name':").append(name+",'number':").append(phonenumber+",'_id':").append(_id+"},");
				Uri uri = Uri.parse("content://com.android.contacts/contacts/"+_id+"/data"); //如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data  
		        Cursor cursor2 = resolver.query(uri, new String[]{Data.DATA1,Data.MIMETYPE}, null,null, null);  //data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等  
		        sb.append("{'_id':"+_id);
		        while(cursor2.moveToNext()){  
		            String data = cursor2.getString(cursor2.getColumnIndex("data1"));  
		            if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")){       //如果是名字  
		                sb.append(", 'name:'"+data);  
		            }  
		            else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")){  //如果是电话  
		                sb.append(", 'phone:'"+data);  
		            }  
		            else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")){  //如果是email  
		            	sb.append(",'email:'"+data);  
		            }  
		            else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/postal-address_v2")){ //如果是地址  
		            	sb.append(",'address:'"+data);  
		            }  
		            else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/organization")){  //如果是组织  
		            	sb.append(",'organization:'"+data);  
		            }  
		        }  
		        sb.append("},");
				
			}while(mcour.moveToNext());
			sb.deleteCharAt(sb.length() -1);
			sb.append("]");
			Log.e(TAG, "sd"+sb);
		}
	}
	
	/**
	 * TODO: 根据电话号码 在通讯录查找人
	 */
	public void fromReadNameByPhone(String  num){  
        String phone = num;  
        //uri=  content://com.android.contacts/data/phones/filter/#  
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+phone);      
        
        Cursor cursor = resolver.query(uri, new String[]{Data.DISPLAY_NAME}, null, null, null); //从raw_contact表中返回display_name  
        if(cursor.moveToFirst()){  
            Log.i("Contacts", "name="+cursor.getString(0));  
        }  
    }  
	/**
	 * TODO:根据id 来更新电话号码 **
	 */
	public void updatePhoneNumberById(int ids,String num){
		int id = ids;
		String phone = num;//"9999919"  
		//对data表的所有数据操作  
	    Uri uri = Uri.parse("content://com.android.contacts/data");
	    ContentValues values = new ContentValues();  
	    values.put("data1", phone);  
	    resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2",id+""}) ;  
	}
	/**
	 * TODO:根据name 来删除相应的联系人
	 */
	public void deleteContactByName(String name){
		resolver.delete(co, "NAME='"+name +"'", null);
	}
	/**
	 * TODO: 一步步的添加新增联系人信息
	 */
	public void stepAddContacts(String _name,String _phnum,String _email){  
        Uri uri = co;//Uri.parse("content://com.android.contacts/raw_contacts");  
        ContentValues values = new ContentValues();  
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));  
        //插入data表  
        uri = Uri.parse("content://com.android.contacts/data");  
        //add Name  
        values.put("raw_contact_id", contact_id);  
        values.put(Data.MIMETYPE,"vnd.android.cursor.item/name");  
        values.put("data2", _name);  
        values.put("data1", _name);  
        resolver.insert(uri, values);  
        values.clear();  
        //add Phone  
        values.put("raw_contact_id", contact_id);  
        values.put(Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");  
        values.put("data2", "2");   //手机  
        values.put("data1", _phnum);  
        resolver.insert(uri, values);  
        values.clear();  
        //add email  
        values.put("raw_contact_id", contact_id);  
        values.put(Data.MIMETYPE,"vnd.android.cursor.item/email_v2");  
        values.put("data2", "2");   //单位  
        values.put("data1", _email);  
        resolver.insert(uri, values);  
    } 
	public void testAddContactsInTransaction(String _name,String _phone) throws Exception {  
	    Uri uri = co;  
	  //  ContentResolver resolver = this.getContext().getContentResolver();  
	    ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();  
	    // 向raw_contact表添加一条记录  
	    //此处.withValue("account_name", null)一定要加，不然会抛NullPointerException  
	    ContentProviderOperation operation1 = ContentProviderOperation  
	            .newInsert(uri).withValue("account_name", null).build();  
	    operations.add(operation1);  
	    // 向data添加数据  
	    uri = Uri.parse("content://com.android.contacts/data");  
	    //添加姓名  
	    ContentProviderOperation operation2 = ContentProviderOperation  
	            .newInsert(uri).withValueBackReference("raw_contact_id", 0)  
	            //withValueBackReference的第二个参数表示引用operations[0]的操作的返回id作为此值  
	            .withValue("mimetype", "vnd.android.cursor.item/name")  
	            .withValue("data2", _name).build();  
	    operations.add(operation2);  
	    //添加手机数据  
	    ContentProviderOperation operation3 = ContentProviderOperation  
	            .newInsert(uri).withValueBackReference("raw_contact_id", 0)  
	            .withValue("mimetype", "vnd.android.cursor.item/phone_v2")  
	            .withValue("data2", "2").withValue("data1", _phone).build();  
	    operations.add(operation3);  
	    resolver.applyBatch("com.android.contacts", operations);  
	}  
	
}
