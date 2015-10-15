package com.korwang.shuishui.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


//暂时没用到
class Preview extends SurfaceView implements SurfaceHolder.Callback{
	SurfaceHolder mHolder;
	Camera mCamera ;
	Bitmap CameraBitmap;
	Preview(Context context){
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	/*surface view */
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		/* open camera*/
		mCamera = Camera.open();
		try{
			
			mCamera.setPreviewDisplay(holder);
		}
		catch(IOException exception){
			/*release*/
			mCamera.release();
			mCamera = null;
		}
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		mCamera.stopPreview();
		mCamera = null;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
		//create camera parameters
		Camera.Parameters parameteres = mCamera.getParameters();
		/*拍照格式*/
		parameteres.setPictureFormat(PixelFormat.JPEG);
		/*preview 尺寸*/
		parameteres.setPreviewSize(320, 480);
		/*图像分辨率*/
		parameteres.setPictureSize(320, 480);
		/*设置相机使用parameters*/
		mCamera.setParameters(parameteres);
		/*开始预览*/
		mCamera.startPreview();
	}
	/* take pic*/
	public void takePicture(){
		if(mCamera != null){
			mCamera.takePicture(null, null, jpegCallback);
		}
	}
	/*pull */
	private PictureCallback jpegCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			CameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			File myCaptureFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCameraApp");
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			try{
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile + File.separator + timeStamp + ".jpg"));
				CameraBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
				Canvas canvas = mHolder.lockCanvas();
				canvas.drawBitmap(CameraBitmap, 0, 0, null);
				mHolder.unlockCanvasAndPost(canvas);
			}
			catch(Exception e){
				e.getMessage();
			}
		}
	};
}