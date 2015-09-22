package com.korewang.shuishui.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.korewang.shuishui.R;

public class FragmentPageOne extends Fragment implements View.OnClickListener,View.OnTouchListener{

	private View rootView;
	private Spinner mSpinner;
	private boolean isFirstIn = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_one, null);
			initSpinner();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	public void initSpinner(){
		mSpinner = (Spinner)rootView.findViewById(R.id.spinner);
		ArrayAdapter adapter=  ArrayAdapter.createFromResource(   
				getActivity(), R.array.colors, android.R.layout.simple_spinner_item);
		//设置下拉样式   
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
        //为下拉列表设置适配器   
        mSpinner.setAdapter(adapter); 
        //定义子元素选择监听器   
        OnItemSelectedListener listenerIO=  new OnItemSelectedListener() {   
			@Override  
            public void onItemSelected(AdapterView<?> parent, View view,   
                            int position, long id) {   
        			if(isFirstIn){
        				isFirstIn = false;
        			}else{
    				Toast.makeText(getActivity(),"选择的色彩： " +   
                        						parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();   
                        			}
                    }   
  
            @Override  
            public void onNothingSelected(AdapterView<?> parent) {
            	Toast.makeText(getActivity(),"nothing selected ", Toast.LENGTH_LONG).show();
            }   
        };   
        //为下拉列表绑定事件监听器   
        mSpinner.setOnItemSelectedListener(listenerIO); 
         mSpinner.setSelection(2, true); //默认选择的
        
        /*下拉菜单弹出的内容选项触屏事件处理*/    
        mSpinner.setOnTouchListener(new Spinner.OnTouchListener(){    
            public boolean onTouch(View v, MotionEvent event) {    
                // TODO Auto-generated method stub    
                /** 
                 *  
                 */  
                return false;    
            }  
        });    
        /*下拉菜单弹出的内容选项焦点改变事件处理*/    
        mSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
        public void onFocusChange(View v, boolean hasFocus) {    
            // TODO Auto-generated method stub    
  
        }    
        });    
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
