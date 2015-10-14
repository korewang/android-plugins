package com.korewang.shuishui.activitys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.message.BufferedHeader;

import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;
import com.korwang.shuishui.utils.CameraPreview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraSettingActivity extends Activity {

	/**
	 设置camera setting
	 */
	private Camera mCamera;
    private CameraPreview mPreview;

    int mNumberOfCameras;
    int mCameraCurrentlyLocked;

    // The first rear facing camera
    int mDefaultCameraId;

    int mScreenWidth, mScreenHeight;
    
	private Button mButton_capture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 无标题栏的窗口
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置布局
        setContentView(R.layout.camera_set_activity_ly);
        // 得到屏幕的大小
        WindowManager wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        mScreenHeight = display.getHeight();
        mScreenWidth = display.getWidth();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

        // 将相机预览图加入帧布局里面
        preview.addView(mPreview, 0);

        // 使用按钮进行拍摄动作监听
        
		mButton_capture = (Button)findViewById(R.id.button_capture);		
		mButton_capture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("button","click");
				// get an image from the camera
                mCamera.takePicture(null, null, mPicture);
			}
		});
		 // 得到默认的相机ID
        mDefaultCameraId = getDefaultCameraId();
        mCameraCurrentlyLocked = mDefaultCameraId;
	}
	 @Override
    protected void onResume()
    {
        Log.d(AppConstants.LOG_TAG, "onResume");
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = getCameraInstance(mCameraCurrentlyLocked);
        
        mPreview.setCamera(mCamera);
    }

    @Override
    protected void onPause()
    {
        Log.d(AppConstants.LOG_TAG, "onPause");
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null)
        {
            mPreview.setCamera(null);
            Log.d(AppConstants.LOG_TAG, "onPause --> Realease camera");
            mCamera.release();
            mCamera = null;
        }

    }

    @Override
    protected void onDestroy()
    {
        Log.d(AppConstants.LOG_TAG, "onDestroy");
        super.onDestroy();

    }

    /**
     * 得到默认相机的ID
     * 
     * @return
     */
    private int getDefaultCameraId()
    {
        Log.d(AppConstants.LOG_TAG, "getDefaultCameraId");
        int defaultId = -1;

        // Find the total number of cameras available
        mNumberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++)
        {
            Camera.getCameraInfo(i, cameraInfo);
            Log.d(AppConstants.LOG_TAG, "camera info: " + cameraInfo.orientation);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK)
            {
                defaultId = i;
            }
        }
        if (-1 == defaultId)
        {
            if (mNumberOfCameras > 0)
            {
                // 如果没有后向摄像头
                defaultId = 0;
            }
            else
            {
                // 没有摄像头
                Toast.makeText(getApplicationContext(), R.string.no_camera,
                        Toast.LENGTH_LONG).show();
            }
        }
        return defaultId;
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int cameraId)
    {
        Log.d(AppConstants.LOG_TAG, "getCameraInstance");
        Camera c = null;
        try
        {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e)
        {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
            Log.e(AppConstants.LOG_TAG, "Camera is not available");
        }
        return c; // returns null if camera is unavailable
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type)
    {
        Log.d(AppConstants.LOG_TAG, "getOutputMediaFile");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try
        {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCameraApp");

            Log.d(AppConstants.LOG_TAG,
                    "Successfully created mediaStorageDir: " + mediaStorageDir);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(AppConstants.LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                // 在SD卡上创建文件夹需要权限：
                Log.d(AppConstants.LOG_TAG,
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }
        else
        {
            return null;
        }

        return mediaFile;
    }

    private PictureCallback mPicture = new PictureCallback()
    {

        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            Log.d(AppConstants.LOG_TAG, "onPictureTaken");

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null)
            {
                Log.d(AppConstants.LOG_TAG,
                        "Error creating media file, check storage permissions: ");
                return;
            }

            try
            {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            }
            catch (FileNotFoundException e)
            {
                Log.d(AppConstants.LOG_TAG, "File not found: " + e.getMessage());
            }
            catch (IOException e)
            {
                Log.d(AppConstants.LOG_TAG,
                        "Error accessing file: " + e.getMessage());
            }

            // 拍照后重新开始预览
            mCamera.stopPreview();
            mCamera.startPreview();
        }
    };

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context)
    {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA))
        {
            // this device has a camera
            return true;
        }
        else
        {
            // no camera on this device
            return false;
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
class AppConstants{
	 public static final String LOG_TAG = "CameraSetting";
}
