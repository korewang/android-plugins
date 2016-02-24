package com.korewang.shuishui.activitys;



import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;

import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.korewang.shuishui.R;

public class FragmentPageOne extends Fragment implements View.OnClickListener,View.OnTouchListener{

	private View rootView;
	private Spinner mSpinner;
	private boolean isFirstIn = true;
	private Button mSelectImg;
	private ImageView mImage;
	private static final int RESULT = 1;
	private final String IMAGE_TYPE = "image/*";
	private  FragmentManager fm,fmt;
	private  FragmentTransaction ft,ftt;
	
	Target target;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_one, null);
			initSpinner();
			initView();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	public void initView(){
		fm = getFragmentManager(); 
		fmt = getFragmentManager(); 
		ft = fm.beginTransaction();//注意。一个transaction 只能commit一次，所以不要定义成全局变量
		ftt = fm.beginTransaction();//注意。一个transaction 只能commit一次，所以不要定义成全局变量
		
		
		
		mSelectImg = (Button)rootView.findViewById(R.id.selectpic);
		mImage = (ImageView)rootView.findViewById(R.id.resultimg);
		mSelectImg.setOnClickListener(this);
		ObjectAnimator animation = ObjectAnimator.ofFloat(mSelectImg, View.ALPHA, 0f,1f);
		animation.setDuration(500);
		animation.start();
		mImage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ImageView mImage =  (ImageView)v;
				 AnimationDrawable ad = (AnimationDrawable)mImage.getDrawable();
				 ad.stop();
				 ad.start();
				return true;
			}
		});
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
    				
	    				/*FragmentPageTwo df = new FragmentPageTwo();  
	    				Bundle bundle = new Bundle();  
	    				bundle.putLong("id", id);  
	    				bundle.putString("name", "选择的色彩：");  
	    				df.setArguments(bundle);  
	    				ft.replace(R.id.main_fragment, df);  
	    				ft.addToBackStack(null);  
	    				ft.commit();
	    				*/
	    				
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
	 private void showPhoto(ImageView photo,String ipath){  
	       /* String picturePath = target.getInfo(Target.TargetPhotoPath);  
	        if(picturePath.equals(""))  
	            return;*/
		 	String picturePath = ipath;
		 	File file = new File(picturePath);
		 	if(!file.exists()){
		 		return;
		 	}
	        // 缩放图片, width, height 按相同比例缩放图片  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片  
	        options.inJustDecodeBounds = true;  
	        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);  
	        int scale = (int)( options.outWidth / (float)300);  
	        if(scale <= 0)  
	            scale = 1;  
	        options.inSampleSize = scale;  
	        options.inJustDecodeBounds = false;  
	        bitmap = BitmapFactory.decodeFile(picturePath, options);  
	          
	        photo.setImageBitmap(bitmap);  
	        photo.setMaxHeight(350);  
	    }  
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data); 
        
        if(requestCode == RESULT && resultCode == getActivity().RESULT_OK && data != null){  
              
        	Uri uri = data.getData();  //此处的地址是数据库映射地址
            ContentResolver cr = getActivity().getContentResolver();  
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null, null,null);
            int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                .getString(actual_image_column_index);
            showPhoto(mImage,img_path);
            
            int degree = readPictureDegree(img_path); 
            BitmapFactory.Options opts=new BitmapFactory.Options();//获取缩略图显示到屏幕上
            opts.inSampleSize=2;
            Bitmap cbitmap=BitmapFactory.decodeFile(img_path,opts);
            Bitmap newbitmap = rotaingImageView(degree, cbitmap);  
            mImage.setImageBitmap(newbitmap);
           
            /*
             * 目前Android SDK定义的Tag有:
            TAG_DATETIME 时间日期
            TAG_FLASH 闪光灯
            TAG_GPS_LATITUDE 纬度
            TAG_GPS_LATITUDE_REF 纬度参考 
            TAG_GPS_LONGITUDE 经度
            TAG_GPS_LONGITUDE_REF 经度参考 
            TAG_IMAGE_LENGTH 图片长
            TAG_IMAGE_WIDTH 图片宽
            TAG_MAKE 设备制造商
            TAG_MODEL 设备型号
            TAG_ORIENTATION 方向
            TAG_WHITE_BALANCE 白平衡
            */ 
            try {
                //android读取图片EXIF信息
                ExifInterface exifInterface=new ExifInterface(img_path);
                String smodel=exifInterface.getAttribute(ExifInterface.TAG_MODEL);
                String width=exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
                String height=exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
                String longitude=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                Toast.makeText(getActivity(), smodel+"  "+width+"*"+height
                		+ "longitude"+longitude, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }  
    }  
	public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
	       //旋转图片 动作   
	       Matrix matrix = new Matrix();
	       matrix.postRotate(angle);  
	       // 创建新的图片   
	       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
	               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	       return resizedBitmap;  
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.selectpic:
			Log.i("R.id.selectpic", "R.id.selectpic");
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType(IMAGE_TYPE);
            startActivityForResult(getAlbum, RESULT);
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		 
		return true;
	}
	
}
