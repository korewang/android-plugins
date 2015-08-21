package com.korewang.shuishui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.korewang.shuishui.R;
import com.korewang.shuishui.plugins.RoundImageView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class AppListAdapter extends BaseAdapter {
	private Context context;
    private LayoutInflater mInflater;
    private ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据
    
	public AppListAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
        this.context  = context;
        init();
	}
	public void init(){
		
		 List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		         
		         for(int i=0;i<packages.size();i++) { 
		         PackageInfo packageInfo = packages.get(i); 
		         AppInfo tmpInfo = new AppInfo(); 
		         tmpInfo.appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString(); 
		         tmpInfo.packageName = packageInfo.packageName; 
		         tmpInfo.versionName = packageInfo.versionName; 
		         tmpInfo.versionCode = packageInfo.versionCode; 
		         tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
		         //Only display the non-system app info
		         if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
		         {
		             appList.add(tmpInfo);//如果非系统应用，则添加至appList
		         }
		         
		        }
		 
    }
	@Override
    public int getCount() {
        // TODO Auto-generated method stub
        return appList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         
        ViewHolder holder = null;
        final int selectedID = position;
        if (convertView == null) {
             
            holder=new ViewHolder(); 
             
            convertView = mInflater.inflate(R.layout.app_grid_item, null);
            holder.icon = (RoundImageView)convertView.findViewById(R.id.image_item);
            holder.title = (TextView)convertView.findViewById(R.id.text_item);
            holder.info = (TextView)convertView.findViewById(R.id.version_item);
            holder.viewBtn = (Button)convertView.findViewById(R.id.reg);
            convertView.setTag(holder);
             
        }else {
             
            holder = (ViewHolder)convertView.getTag();
        }
         
        // holder.icon.setImageResource((Integer)mData.get(position).get("icon"));
         holder.icon.setImageDrawable((Drawable)appList.get(position).appIcon);
       // holder.title.setText((String)mData.get(position).get("description"));
         holder.title.setText("包名："+(String)appList.get(position).packageName+"版本号："+(String)appList.get(position).versionName);
        //holder.info.setText((String)mData.get(position).get("version"));
        holder.info.setText("appname："+(String)appList.get(position).appName); 
        
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View v) {
              //  showInfo(selectedID);  
               // SchemeBackend.getInfo(selectedID);
           	 Log.i("3#","id"+selectedID);
           	 Toast.makeText(context, "包名：" + 
           			 				(String)appList.get(selectedID).packageName + 
           			 				"版本号：" + 
           			 				(String)appList.get(selectedID).versionName, Toast.LENGTH_SHORT).show();
           //	 Log.i("appList.get(position).appIcon", appList.get(selectedID).appIcon);
         	Intent mintent = new Intent("my.action");
	    	mintent.putExtra("app_id", selectedID+"");
	        context.sendBroadcast(mintent);
        	
            }
        });
         
         
        return convertView;
    }
    public void showInfo(int id){
    	
    }
    public final class ViewHolder{
        public RoundImageView icon;
        public TextView title;
        public TextView info;
        public Button viewBtn;
    }
    public class AppInfo {
        public String appName="";
        public String packageName="";
        public String versionName="";
        public int versionCode=0;
        public Drawable appIcon=null;
        public void print()
        {
            Log.v("app","Name:"+appName+" Package:"+packageName);
            Log.v("app","Name:"+appName+" versionName:"+versionName);
            Log.v("app","Name:"+appName+" versionCode:"+versionCode);
        }

    }
}
