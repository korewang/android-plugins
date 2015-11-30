package com.korewang.shuishui.activitys;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.baidu.navisdk.ui.routeguide.subview.O;
import com.korewang.shuishui.R;
import com.korewang.shuishui.R.id;
import com.korewang.shuishui.R.layout;
import com.korewang.shuishui.R.menu;
import com.korewang.shuishui.widget.HeaderView;
import com.korwang.shuishui.utils.FileDownloadThread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownImageActivity extends Activity implements View.OnClickListener{

	private static final String TAG = "DownImageActivity";
	private Context context;
	private Button downBtn,GetBatteryMSG,Scanwifi,downsdCard;
	private EditText  inputText;
	private ImageView showImageV;
	
	private ProgressDialog dialog;
	private ProgressBar progressBarh;
	//建立一个hander  生命hander为静态的
//	private static Handler handler = new Handler();
	 private MediaPlayer mediaPlayer;
	 private static final float BEEP_VOLUME = 0.10f;
	 private boolean  playBeep;
     private boolean vibrate;
	private static int IS_FINISH = 1;
	private String image_path = null;
	
	private int BatteryN;       //目前电量  
    private int BatteryV;       //电池电压  
    private double BatteryT;        //电池温度  
    private int BatteryS;  		//电量的总刻度
    private String BatteryStatus;   //电池状态  
    private String BatteryTemp;     //电池使用情况  
    private int FileLength;
    private int DownedFileLength=0;
    private URLConnection connection;
    private OutputStream outputStream;
    private InputStream inputStream;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.down_image_activity_ly);
		downBtn = (Button)findViewById(R.id.DownBtn);
		inputText = (EditText)findViewById(R.id.inputText);
		showImageV = (ImageView)findViewById(R.id.showImage);
		GetBatteryMSG = (Button)findViewById(R.id.GetBatteryMSG);
		Scanwifi = (Button)findViewById(R.id.ScanWifi);
		progressBarh = (ProgressBar)findViewById(R.id.progressBarh);
		downsdCard = (Button)findViewById(R.id.DownSD);
		HeaderView view = (HeaderView)findViewById(R.id.headerDownView);
		view.setHeaderTitle("DOWN_image");
		GetBatteryMSG.setOnClickListener(this);
		Scanwifi.setOnClickListener(this);
		initDialog();
		new Thread(connectNet).start(); 
		downBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image_path = inputText.getText().toString();
				downBtn.setText("DownBTN");
				if(image_path.length() <8){
					image_path = "http://www.baidu.com/img/bdlogo.png";
					
				}
				dialog.show();
				new Thread(new MyThread()).start();
			}
		});
		downsdCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DownedFileLength=0;
				image_path = inputText.getText().toString();
				//downBtn.setText("DownBTN");
				if(image_path.length() <8){
					image_path = "http://www.baidu.com/img/bdlogo.png";
					
				}
				dialog.show();
				Thread thread = new Thread(){
					public void run(){
						try{
							DownSDFile(image_path);
						}catch(Exception e){
							 e.printStackTrace();
						}
					}
				};
				thread.start();
				
				//new Thread(saveFileRunnable).start(); 
				//doDownload(); //apk
			}
		});
		
	}
	/*
	 * down apk
	 * */
	 /** 
     * 使用Handler更新UI界面信息 
     */  
    
    Handler mHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
  
            progressBarh.setProgress(msg.getData().getInt("size"));  
  
            float temp = (float) progressBarh.getProgress()  
                    / (float) progressBarh.getMax();  
  
            int progress = (int) (temp * 100);  
            if (progress == 100) {  
                Toast.makeText(DownImageActivity.this, "下载完成！", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                
                File mFile = new File(msg.getData().getString("uri"));
            	Intent install = new Intent();
            	install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	install.setAction(android.content.Intent.ACTION_VIEW);
            	install.setDataAndType(Uri.fromFile(mFile),
            	"application/vnd.android.package-archive");
            	startActivity(install);
            }  
            dialog.setMessage("下载进度:" + progress + " %");
        }  
    }; 
	 /** 
     * 下载准备工作，获取SD卡路径、开启线程 
     */  
    private void doDownload() {  
        // 获取SD卡路径  
        String path = Environment.getExternalStorageDirectory()+"/shuishui/MyCameraApp/";  
        File file = new File(path);  
        // 如果SD卡目录不存在创建  
        if (!file.exists()) {  
            file.mkdir();  
        }  
        // 设置progressBar初始化  
        progressBarh.setProgress(0);  
  
        // 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到  
        String downloadUrl = "http://msoftdl.360.cn/360batterydoctor/360BatteryDoctor_offical.apk";  
        String fileName = "360BatteryDoctor_offical.apk";  
        int threadNum = 5;  
        String filepath = path + fileName;  
        Log.d(TAG, "download file  path:" + filepath);  
        downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);  
        task.start();  
    }  
  
    /** 
     * 多线程文件下载 
     *  
     * @author 
     * @2014-8-7 
     */  
    class downloadTask extends Thread {  
        private String downloadUrl;// 下载链接地址  
        private int threadNum;// 开启的线程数  
        private String filePath;// 保存文件路径地址  
        private int blockSize;// 每一个线程的下载量  
  
        public downloadTask(String downloadUrl, int threadNum, String fileptah) {  
            this.downloadUrl = downloadUrl;  
            this.threadNum = threadNum;  
            this.filePath = fileptah;  
        }  
  
        @Override  
        public void run() {  
  
            FileDownloadThread[] threads = new FileDownloadThread[threadNum];  
            try {  
                URL url = new URL(downloadUrl);  
                Log.d(TAG, "download file http path:" + downloadUrl);  
                URLConnection conn = url.openConnection();  
                // 读取下载文件总大小  
                int fileSize = conn.getContentLength();  
                if (fileSize <= 0) {  
                    System.out.println("读取文件失败");  
                    return;  
                }  
                // 设置ProgressBar最大的长度为文件Size  
                progressBarh.setMax(fileSize);  
  
                // 计算每条线程下载的数据长度  
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum  
                        : fileSize / threadNum + 1;  
  
                Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");  
  
                File file = new File(filePath);  
                for (int i = 0; i < threads.length; i++) {  
                    // 启动线程，分别下载每个线程需要下载的部分  
                    threads[i] = new FileDownloadThread(url, file, blockSize,  
                            (i + 1));  
                    threads[i].setName("Thread:" + i);  
                    threads[i].start();  
                }  
  
                boolean isfinished = false;  
                int downloadedAllSize = 0;  
                while (!isfinished) {  
                    isfinished = true;  
                    // 当前所有线程下载总量  
                    downloadedAllSize = 0;  
                    for (int i = 0; i < threads.length; i++) {  
                        downloadedAllSize += threads[i].getDownloadLength();  
                        if (!threads[i].isCompleted()) {  
                            isfinished = false;  
                        }  
                    }  
                    // 通知handler去更新视图组件  
                    Message msg = new Message();  
                    msg.getData().putInt("size", downloadedAllSize);  
                    msg.getData().putString("uri", filePath);
                    mHandler.sendMessage(msg);  
                    // Log.d(TAG, "current downloadSize:" + downloadedAllSize);  
                    Thread.sleep(1000);// 休息1秒后再读取下载进度  
                }  
                Log.d(TAG, " all of downloadSize:" + downloadedAllSize);  
  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
  
        }  
    }  
	
	/*
	 * down apk end
	 * */
	/*
	 * add
	 * */
	 private Bitmap mBitmap;  
	    private String mFileName;  
	    private String mSaveMessage; 
	    private final static String ALBUM_PATH   =Environment.getExternalStorageDirectory()+"/shuishui/MyCameraApp/";// = Environment.getExternalStorageDirectory() + "/download_test/";
	    
	    /** 
	     * Get image from newwork 
	     * @param path The path of image 
	     * @return byte[] 
	     * @throws Exception 
	     */  
	    public byte[] getImage(String path) throws Exception{  
	        URL url = new URL(path);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5 * 1000);  
	        conn.setRequestMethod("GET");  
	        InputStream inStream = conn.getInputStream();  
	        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
	            return readStream(inStream);  
	        }  
	        return null;  
	    }  
	  
	    
	    public static byte[] readStream(InputStream inStream) throws Exception{  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while( (len=inStream.read(buffer)) != -1){  
	            outStream.write(buffer, 0, len);  
	        }  
	        outStream.close();  
	        inStream.close();  
	        return outStream.toByteArray();  
	    }  
	private Runnable saveFileRunnable = new Runnable(){  
        @Override  
        public void run() {  
            try {  
                saveFile(mBitmap, mFileName);  
                mSaveMessage = "图片保存成功！";  
            } catch (IOException e) {  
                mSaveMessage = "图片保存失败！";  
                e.printStackTrace();  
            }  
            messageHandler.sendMessage(messageHandler.obtainMessage());  
        }  
  
    };  
    private Handler messageHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            dialog.dismiss();  
            Log.d(TAG, mSaveMessage);  
           // Toast.makeText(IcsTestActivity.this, mSaveMessage, Toast.LENGTH_SHORT).show();  
        }  
    };  
    /** 
     * 保存文件 
     * @param bm 
     * @param fileName 
     * @throws IOException 
     */  
    public void saveFile(Bitmap bm, String fileName) throws IOException {  
        File dirFile = new File(ALBUM_PATH);  
        if(!dirFile.exists()){  
            dirFile.mkdir();  
        }  
        File myCaptureFile = new File(ALBUM_PATH + fileName);  
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
        bos.flush();  
        bos.close();  
    }  
    
    /** 
     * Get image from newwork 
     * @param path The path of image 
     * @return InputStream 
     * @throws Exception 
     */  
    public InputStream getImageStream(String path) throws Exception{  
        URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("GET");  
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
            return conn.getInputStream();  
        }  
        return null;  
    }  
    private Runnable connectNet = new Runnable(){  
        @Override  
        public void run() {  
            try {  
                String filePath = "http://img.my.csdn.net/uploads/201402/24/1393242467_3999.jpg";  
                mFileName = "test.jpg";  
  
                //以下是取得图片的两种方法  
                //////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap  
                byte[] data = getImage(filePath);  
                if(data!=null){  
                    mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
                }else{  
                   // Toast.makeText(IcsTestActivity.this, "Image error!", 1).show();  
                }  
                ////////////////////////////////////////////////////////  
  
                //******** 方法2：取得的是InputStream，直接从InputStream生成bitmap ***********/  
              //  mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));  
                //********************************************************************/  
  
                // 发送消息，通知handler在主线程中更新UI  
               // connectHanlder.sendEmptyMessage(0);  
                Log.d(TAG, "set image ...");  
            } catch (Exception e) {  
              //  Toast.makeText(,"无法链接网络！", 1).show();  
                e.printStackTrace();  
            }  
  
        }  
  
    };  
    private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display image");  
            // 更新UI，显示图片  
            if (mBitmap != null) {  
              //  mImageView.setImageBitmap(mBitmap);// display image  
            }  
        }  
    };  
	/*
	 * add end 
	 * */
	public void DownSDFile(String path){
		/*
         * 连接到服务器
         */
         
        try {
             URL url=new URL(path);
             connection=url.openConnection();
             if (connection.getReadTimeout()==5) {
                Log.i("---------->", "当前网络有问题");
                // return;
               }
             inputStream=connection.getInputStream();
             
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        /*
         * 文件的保存路径和和文件名其中pci是在手机SD卡上要保存的路径，如果不存在则新建
         */
        String savePAth=Environment.getExternalStorageDirectory()+"/shuishui/MyCameraApp/";
        File file1=new File(savePAth);
        if (!file1.exists()) {
            file1.mkdir();
        }
        String pathAry[] = path.split("/");
        String filename = pathAry[pathAry.length-1];
        String savePathString=Environment.getExternalStorageDirectory()+"/shuishui/MyCameraApp/"+filename;
        File file =new File(savePathString);
        if (!file.exists()) {
           
        	 try {
        		 ///.createNewFile(); 在win dow 系统可用，但是在Linux系统中不存在使用
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
        }
        /*
         * 向SD卡中写入文件,用Handle传递线程
         */
        Message message=new Message();
        
        try {  
            outputStream=new FileOutputStream(file);  
            byte [] buffer=new byte[1024];  
            FileLength=connection.getContentLength();  
            message.what=0;  
            handlersd.sendMessage(message);  
           
            int len;  
            /*
             * inputStream.read(Buffer)！= -1 表示从InputStream中读取一个数组的数据，
             * 如果返回-1 则表示数据读取完成了。
             * */
            while ((len = inputStream.read(buffer, 0, 1024)) != -1) {  
            	 /*    raf.write(buffer);  
                downloadLength += len;  
            } 
             * */
          //  while (DownedFileLength<FileLength) {  
                outputStream.write(buffer,0,len);  
                DownedFileLength+=len;//inputStream.read(buffer);  
                Log.i("-------->", DownedFileLength+"");  
                Message message1=new Message();  
                message1.what=1;  
                handlersd.sendMessage(message1);  
            }  
            Message message2=new Message();  
            message2.what=2;  
            handlersd.sendMessage(message2);
            if(path.endsWith(".jpg")||path.endsWith(".png")){
            	try {
    				mBitmap = BitmapFactory.decodeStream(getImageStream(path));
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            }else if(path.endsWith(".apk")){
            	File mFile = new File(savePathString);
            	Intent install = new Intent();
            	install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	install.setAction(android.content.Intent.ACTION_VIEW);
            	install.setDataAndType(Uri.fromFile(mFile),
            	"application/vnd.android.package-archive");
            	startActivity(install);
            }
            
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
		  
	}
	private Handler handlersd=new Handler()
    {
         public void handleMessage(Message msg)
        {
        if (!Thread.currentThread().isInterrupted()) {
            switch (msg.what) {
            case 0:
                progressBarh.setMax(FileLength);
                Log.i("文件长度----------->", progressBarh.getMax()+""); 
                break;
            case 1:
            	progressBarh.setProgress(DownedFileLength);
                int x=DownedFileLength*100/FileLength;
                //textView.setText(x+"%");
                dialog.setMessage("已下载 "+x+"%，请稍后...");
                
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                break;
                 
            default:
                break;
            }
        }  
        }
          
    };
  
	@Override
	 public void onClick(View v){
		switch (v.getId()) {
		case R.id.GetBatteryMSG:
				//起动查询电量
				//注册广播接受者java代码
				IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
				//创建广播接受者对象
				BatteryReceiver batteryReceiver = new BatteryReceiver();
				
				//注册receiver
				registerReceiver(batteryReceiver, intentFilter);
			break;
		case R.id.ScanWifi:
			WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
			List<ScanResult> wifiList = wm.getScanResults();  
			for (int i = 0; i < wifiList.size(); i++) {  
				ScanResult result = wifiList.get(i);  
				Log.d("wifi===========", "level="+result.level+"ssid="+result.SSID+"bssid=" + result.BSSID);  
			}  
			playBeepSoundAndVibrate();
			break;
		default:
			break;
		}
	}
	@Override
    public void onResume() {
        super.onResume();
        /*
         * getRingerMode() ——返回当前的铃声模式。
         * 如RINGER_MODE_NORMAL（普通）、
         * 	RINGER_MODE_SILENT（静音）、
         * 	RINGER_MODE_VIBRATE（震动）
         * 这样子做的bug还是有的
         * */
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }
	private void initDialog(){
		dialog = new ProgressDialog(context);
		dialog.setTitle("提示");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);
	}
	private  Handler handler = new Handler() {
        // 在Handler中获取消息，重写handleMessage()方法
        @Override
        public void handleMessage(Message msg) {            
            // 判断消息码是否为1
        	
            if(msg.what==IS_FINISH){
                byte[] data=(byte[])msg.obj;
                Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
                showImageV.setImageBitmap(bmp);
                dialog.dismiss();
                try {
					saveFile(bmp, "abv.jpg");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    };
	public class MyThread implements Runnable{

        @Override
        public void run() {
        	
    	    	  	HttpClient httpClient = new DefaultHttpClient();
    	            HttpGet httpGet = new HttpGet(image_path);
    	            HttpResponse httpResponse = null;
	    	            try {
	    	                httpResponse = httpClient.execute(httpGet);
	    	               /* Message m0 = new Message();
	    	                
	    	                m0.what=0;
	    	                handler.sendMessage(m0);*/
	    	               
	    	                if (httpResponse.getStatusLine().getStatusCode() == 200) {
	    	                    byte[] data = EntityUtils.toByteArray(httpResponse
	    	                            .getEntity());
	    	                    // 获取一个Message对象，设置what为1
	    	                    Message msg = Message.obtain();
	    	                    msg.obj = data;
	    	                    msg.what = IS_FINISH;
	    	                    // 发送这个消息到消息队列中
	    	                    handler.sendMessage(msg);
	    	                   // handler.sendMessageDelayed(msg, 5000);
	    	                }
	    	            } catch (Exception e) {
	    	                e.printStackTrace();
	    	            }
    	      		
        }
	}
	
	//获取电池电量
	/**
	 * 广播接受者
	 */
	class BatteryReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//判断它是否是为电量变化的Broadcast Action
			if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
				
					//电量的总刻度
				 	BatteryS = intent.getIntExtra("scale", 100);
				 	BatteryN = intent.getIntExtra("level", 0);    //目前电量  
	                BatteryV = intent.getIntExtra("voltage", 0);  //电池电压  
	                BatteryT = intent.getIntExtra("temperature", 0);  //电池温度  
				//把它转成百分比
				downBtn.setText("电池电量为"+((BatteryN*100)/BatteryS)+"%");
				
				 switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN))   
	                {  
	                case BatteryManager.BATTERY_STATUS_CHARGING:  
	                    BatteryStatus = "充电状态";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_DISCHARGING:  
	                    BatteryStatus = "放电状态";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:  
	                    BatteryStatus = "未充电";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_FULL:  
	                    BatteryStatus = "充满电";  
	                    break;  
	                case BatteryManager.BATTERY_STATUS_UNKNOWN:  
	                    BatteryStatus = "未知道状态";  
	                    break;  
	                }  
	                  
	                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN))   
	                {  
	                case BatteryManager.BATTERY_HEALTH_UNKNOWN:  
	                    BatteryTemp = "未知错误";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_GOOD:  
	                    BatteryTemp = "状态良好";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_DEAD:  
	                    BatteryTemp = "电池没有电";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:  
	                    BatteryTemp = "电池电压过高";  
	                    break;  
	                case BatteryManager.BATTERY_HEALTH_OVERHEAT:  
	                    BatteryTemp =  "电池过热";  
	                    break;  
	                }  
	                Toast.makeText(context, "目前电量为" + BatteryN + "% --- " + 
            						BatteryStatus + "\n" + "电压为" + BatteryV + 
            						"mV --- " + BatteryTemp + "\n" + 
            						"温度为" + (BatteryT*0.1) + "℃", 
            						Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.down_image, menu);
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
	private static final long VIBRATE_DURATION = 200L;
	private void playBeepSoundAndVibrate() {
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }else{
        	playBeep = true;
        }
		if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
       
		if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
	 private void initBeepSound() {
			
	        if (playBeep && mediaPlayer == null) {
	            // The volume on STREAM_SYSTEM is not adjustable, and users found it
	            // too loud,
	            // so we now play on the music stream.
	            setVolumeControlStream(AudioManager.STREAM_MUSIC);
	            mediaPlayer = new MediaPlayer();
	            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	            mediaPlayer.setOnCompletionListener(beepListener);

	            AssetFileDescriptor file = getResources().openRawResourceFd(
	                    R.raw.beep);
	            try {
	                mediaPlayer.setDataSource(file.getFileDescriptor(),
	                        file.getStartOffset(), file.getLength());
	                file.close();
	                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
	                mediaPlayer.prepare();
	            } catch (IOException e) {
	                mediaPlayer = null;
	            }
	        }
	 }
	 /**
	     * When the beep has finished playing, rewind to queue up another one.
	     */
	    private final OnCompletionListener beepListener = new OnCompletionListener() {
	        public void onCompletion(MediaPlayer mediaPlayer) {
	            mediaPlayer.seekTo(0);
	        }
	    };
	
}
