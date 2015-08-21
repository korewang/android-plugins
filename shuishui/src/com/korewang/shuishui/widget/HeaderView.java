package com.korewang.shuishui.widget;

import com.korewang.shuishui.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeaderView extends RelativeLayout implements View.OnClickListener {
	private Context mContext;
	private ImageButton leftBtn;
	private TextView mTitle;
	public HeaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		getUI(context);
	}
	public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getUI(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getUI(context);
    }
    public void getUI(Context context){
    	this.mContext = context;
    
    	LayoutInflater headerinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	headerinflater.inflate(R.layout.header_view, this,true); 
    	mTitle = (TextView) findViewById(R.id.header_tips);
        leftBtn = (ImageButton)findViewById(R.id.goback);
        leftBtn.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goback:
			((Activity) mContext).finish();
			break;

		default:
			break;
		}
	}
	public void setHeaderTitle(String title){
		mTitle.setText(title);
	}
}
