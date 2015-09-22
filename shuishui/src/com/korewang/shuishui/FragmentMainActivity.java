package com.korewang.shuishui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.korewang.shuishui.activitys.FragmentPageFour;
import com.korewang.shuishui.activitys.FragmentPageOne;
import com.korewang.shuishui.activitys.FragmentPageThree;
import com.korewang.shuishui.activitys.FragmentPageTwo;

public class FragmentMainActivity extends FragmentActivity {
	/**
	   * FragmentTabhost
	   */
	  private FragmentTabHost mTabHost;

	  /**
	   * 布局填充器
	   * 
	   */
	  private LayoutInflater mLayoutInflater;

	  /**
	   * Fragment数组界面
	   * 
	   */
	  private Class mFragmentArray[] = { FragmentPageOne.class,
			  FragmentPageTwo.class, FragmentPageThree.class,FragmentPageFour.class};
	  /**
	   * 存放图片数组
	   * 
	   */
	  private int mImageArray[] = { R.drawable.t_check,
	      R.drawable.t_linkstagekey, R.drawable.t_index_seriesreport, R.drawable.t_claim};

	  /**
	   * 选修卡文字
	   * 
	   */
	  private String mTextArray[] = { "首页", "消息", "更多", "图片"};
	  /**
	   * 
	   * 
	   */
	  private  int arrayFrag[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_ly);
		initView();
	}
	/**
	   * 初始化组件
	   */
	  private void initView() {
	    mLayoutInflater = LayoutInflater.from(this);

	    // 找到TabHost
	    mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
	    mTabHost.setup(this, getSupportFragmentManager(), R.id.main_fragment);
	    // 得到fragment的个数
	    int count = mFragmentArray.length;
	    arrayFrag = getResources().getIntArray(R.array.itemfrag);
	   // count = arrayFrag.length;
	    for (int i = 0; i < count; i++) {
	      // 给每个Tab按钮设置图标、文字和内容
	      TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
	          .setIndicator(getTabItemView(i));
	      // 将Tab按钮添加进Tab选项卡中
	      mTabHost.addTab(tabSpec, mFragmentArray[i], null);
	      // 设置Tab按钮的背景
	   /*
	    *   mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.bt_selector);
	    *   作用是点击的时候设置的背景图
	    *   
	    * */ 	      
	    }
	  }
	  /**
	   *
	   * 给每个Tab按钮设置图标和文字
	   */
	  private View getTabItemView(int index) {
	    View view = mLayoutInflater.inflate(R.layout.tabcontent, null);
	    ImageView imageView = (ImageView) view.findViewById(R.id.image);
	    imageView.setImageResource(mImageArray[index]);
	    TextView textView = (TextView) view.findViewById(R.id.text);
	    textView.setText(mTextArray[index]);

	    return view;
	  }
}
