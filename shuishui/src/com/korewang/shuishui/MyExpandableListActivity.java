package com.korewang.shuishui;

import com.korewang.shuishui.activitys.BmapActivity;
import com.korewang.shuishui.activitys.InitActivity;
import com.korewang.shuishui.animation.activitys.GestrueZoomActivity;
import com.korwang.shuishui.utils.UIControl;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyExpandableListActivity extends ExpandableListActivity {
	private Context mcontext;
	private static final String TAG = "MyExpandableListActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.my_expandable_list_ly); //  自定义去掉setcontetView
		mcontext = this;
		final ExpandableListAdapter adapter = new ExpandableListAdapter() {
			
			int[] logos = new int[]{
					R.drawable.ic_launcher,
					R.drawable.ic_launcher,
					R.drawable.ic_launcher,
					R.drawable.ic_launcher
			};
			private String[] armTypes = new String[]{
					"国家","州省份","语言","Android图形图像"
			};
			private String[][] arms = new String[][]{
					{"中国","美国","德国"},
					{"北京","迈阿密","柏林汉堡市"},
					{"汉语","美式英语","德语"},
					{"Frame(逐帧)动画","shader动画","sharedPreference","GestrueZoom","Matrix缩放"}
					
			};
			@Override
			public void unregisterDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void registerDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGroupExpanded(int groupPosition) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGroupCollapsed(int groupPosition) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				// 决定每个组选项的外观
				LinearLayout ll = new LinearLayout(MyExpandableListActivity.this);
				ll.setOrientation(0);
				ll.setGravity(Gravity.CENTER_VERTICAL);
				ImageView logo = new ImageView(MyExpandableListActivity.this);
				logo.setImageResource(logos[groupPosition]);
				ll.addView(logo);
				TextView textView = getTextView();
				textView.setText(getGroup(groupPosition).toString());
				
				ll.addView(textView);
				return ll;
			}
			
			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return groupPosition;
			}
			
			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return armTypes.length;
			}
			
			@Override
			public Object getGroup(int groupPosition) {
				// 获取指定组位置处的组数据
				
				return armTypes[groupPosition];
			}
			
			@Override
			public long getCombinedGroupId(long groupId) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getCombinedChildId(long groupId, long childId) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return arms[groupPosition].length;
			}
			
			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView textView = getTextView();
				textView.setText(getChild(groupPosition, childPosition).toString());
				return textView;
			}
			
			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return childPosition;
			}
			
			@Override
			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				
				return arms[groupPosition][childPosition];
			}
			
			@Override
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
			private TextView getTextView(){
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,64);
				TextView textView = new TextView(MyExpandableListActivity.this);
				textView.setLayoutParams(lp);
				textView.setPadding(36, 0, 0, 0);
				textView.setTextSize(20);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				return textView;
			}
		};
		//ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandlist);
        //expandableListView.setAdapter(adapter);
	       setListAdapter(adapter);
	      this.getExpandableListView().setOnChildClickListener(new OnChildClickListener() {
			
			

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(MyExpandableListActivity.this, "groupPosition:"+groupPosition+"&childPosition"+childPosition, Toast.LENGTH_SHORT).show();
				String st = groupPosition+""+childPosition;
				if(st.equals("30")){
					UIControl.startAnimationFrameActivity(mcontext);
				}else if(st.equals("31")){
					UIControl.startWrapActivity(mcontext);
				}else if(st.equals("32")){
					UIControl.startSharedActivity(mcontext);
				}else if(st.equals("33")){
					UIControl.startGestrueZoomActivity(mcontext);
					new Thread(new myVideo()).start();
					
//					Handler mg = new Handler();
//					mg.postDelayed(myVideo, 12000);
					/*
					----------------------
					中国建设银行
					----------------------
					中国邮政储蓄
					   银行
					-----------------------
					*/
				}else if(st.equals("34")){
					UIControl.startMatrixActivity(mcontext);
				}
				
				return true;
			}
		});
	}
	
	 class myVideo implements Runnable{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Message m = Message.obtain();
			m.what=1;
			//mH.postDelayed(myVideo, 8000); 每8秒发一次
			//mH.sendMessage(m);
			mH.sendMessageDelayed(m, 8000);
			Log.i("test","name is :" + Thread.currentThread().getName());
		}
	};
	private static Handler mH = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==1){
				Log.i("test", "mesg1");
			}
		}
	};


}
