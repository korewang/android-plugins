package com.korewang.shuishui.service.downloader;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class DownloadThread extends Thread{
	private static final String TAG = "DownloadThread";
	
	private File saveFile;
	private URL downUrl;
	private int block;
	private int threadId = -1;
	private int downloadedLength;
	private boolean finished = false;
	private FileDownLoader downloader;
	
	
	public DownloadThread(FileDownLoader downloader,URL downUrl,File saveFile,int block,int downloadedLength,int threadId){
		this.downUrl = downUrl;
		this.saveFile = saveFile;
		this.block=block;
		this.downloader = downloader;
		this.threadId = threadId;
		this.downloadedLength = downloadedLength;
		
	}
	
	@Override
	public void run(){
		if(downloadedLength <block){//无下载完成
			try{
				
				HttpURLConnection http = (HttpURLConnection)downUrl.openConnection();
				http.setConnectTimeout(5*1000);
				http.setRequestMethod("GET");
				http.setRequestProperty("Accept", "image/gif,image/jpeg,image/pjpeg,application/x-shockwave-flash,"
						+ "application/xaml+xml,application/vnd.ms-xpsdocument,application/x-ms-xbap,application/x-ms-application,"
						+ "application/vnd.ms-excel,application/vnd.ms-powerpoint,application/vnd.ms-word,*/*");
				//设置请求过得源页面
				http.setRequestProperty("Accept-Language", "zh-CN");
				http.setRequestProperty("Referer",downUrl.toString());
				http.setRequestProperty("Charset","UTF-8");
				int startPos = block * (threadId - 1) + downloadedLength;//开始位置
				int endPos  = block *threadId -1;//结束位置
				http.setRequestProperty("Range", "byte="+startPos +"-"+endPos);
				//设置获取实体数据的范围，如果超过了实体数据的大小会自动返回实际的数据大小
				http.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE 8.0;Windows NT 5.2; Trident/4.0;"
						+ ".NET CLR 1.1.4322;.NET CLR 3.0.04506.30;.NET CLR 3.0.4506.2152;.NET CLR 3.5.30729)");//设置用户代理
				http.setRequestProperty("Connection", "Keep-Alive");//connection 方式
				InputStream inStream  = http.getInputStream();//获取远程连接的输入流
				byte[] buffer = new byte[1024];
				int offset = 0;
				print("Thread"+this.threadId + "starts to download from position"+startPos);
				RandomAccessFile threadFile = new RandomAccessFile(this.saveFile, "rwd");
				threadFile.seek(startPos);//指向开始下载的位置
				while(!downloader.getExited() && (offset = inStream.read(buffer,0,1024)) != -1){
					threadFile.write(buffer,0,offset);
					downloadedLength += offset;
					downloader.update(this.threadId, downloadedLength);
					downloader.append(offset);
				}
				threadFile.close();
				inStream.close();
				if(downloader.getExited()){
					print("Thread"+this.threadId +"has been paused");
				}else{
					print("Thread"+this.threadId +"download finished");
				}
				this.finished = true;
				
			}catch(Exception e){
				this.downloadedLength = -1;//设置该现成已经下载的长度为 -1
				print("Thread"+this.threadId +"： "+e);
			}
		}
	}
	private static void print(String msg){
		Log.i(TAG, msg);
	}
	public boolean isFinished(){
		
		return finished;
	}
	public long getDownloadedLength(){
		return downloadedLength;
	}
}
