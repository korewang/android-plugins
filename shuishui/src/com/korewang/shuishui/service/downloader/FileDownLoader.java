package com.korewang.shuishui.service.downloader;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.korewang.shuishui.service.FileService;

import android.content.Context;
import android.util.Log;

public class FileDownLoader {

	private static final String TAG = "FileDownLoader";
	
	private static final int RESPONSEOK = 200;
	private Context context;
	private FileService fileService; //获取本地数据库的业务Bean
	private boolean exited; //停止下载标志
	private int downloadedSize = 0;//已下载文件长度
	private int fileSize = 0;//原始文件长度
	private DownloadThread[] threads;//根据线程数设置下载线程池
	private File saveFile;
	private Map<Integer,Integer> data = new ConcurrentHashMap<Integer, Integer>();//缓存各线程下载的长度
	private int block;//每条县城下载的长度；
	private String downloadUrl;// 下载路径
	
	
	

	/**
	 * 获取线程数
	 */
	public int getThreadSize(){
		return threads.length;
	}
	/**
	 *  退出下载   
	 */
	public void exit(){
		this.exited = true;//  设置退出标志为true
	}
	public boolean getExited(){
		return this.exited;
	}
	/**
	 * 获取文件大小
	 */
	public int getFileSize(){
		return fileSize;// 从类成员变量中获取下载文件的大小
	}
	/**
	 * 累计已下载大小
	 * 
	 */
	protected synchronized void append(int size){
		downloadedSize +=size;//使用同步关键字解决并发访问问题    把实时下载长度加入到总下载长度中
	}
	/**
	 * 更新制定线程最后下载的位置
	 * thread id 线程id
	 * pos 最后下载的位置
	 */
	protected synchronized void update(int threadId, int pos){
		//指定线程id的线程赋予最新的下载长度，以前的值会被覆盖
		this.data.put(threadId, pos);
		//更新数据库指定的线程的现在长度
		this.fileService.update(this.downloadUrl, threadId, pos);
	}
	
	/**
	 * 构建文件下载器
	 * @param  downloadUrl  下载路径
	 * @param filesavedir 文件保存目录
	 * @param threadNUm 下载线程数
	 * @return 
	 * @return 
	 */
	
	public FileDownLoader(Context context, String downloadUrl,File fileSaveDir, int threadNum){
		try{
			this.context = context;
			this.downloadUrl = downloadUrl;//下载路径赋值
			fileService = new FileService(this.context);
			URL url = new URL(this.downloadUrl);//根据下载路径实例化URL
			if(!fileSaveDir.exists()){
				fileSaveDir.mkdirs();
				// mkdirs  和mkdir 的区别  此处可以创建多层目录
			}
			this.threads = new DownloadThread[threadNum];
			//  根据下载的线程数创建下载线程池
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "image/gif,image/jpeg,image/pjpeg,application/x-shockwave-flash,"
					+ "application/xaml+xml,application/vnd.ms-xpsdocument,application/x-ms-xbap,application/x-ms-application,"
					+ "application/vnd.ms-excel,application/vnd.ms-powerpoint,application/vnd.ms-word,*/*");
			//设置请求过得源页面
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer",downloadUrl);//设置请求的来源页面，便于服务端进行来源统计
			conn.setRequestProperty("Charset","UTF-8");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE 8.0;Windows NT 5.2; Trident/4.0;"
					+ ".NET CLR 1.1.4322;.NET CLR 3.0.04506.30;.NET CLR 3.0.4506.2152;.NET CLR 3.5.30729)");//设置用户代理
			conn.setRequestProperty("Connection", "Keep-Alive");//connection 方式
			conn.connect();// 连接 connect
			printResponseHeader(conn);//应答返回的http头字段集合
			
			if(conn.getResponseCode() == RESPONSEOK){
				this.fileSize = conn.getContentLength();
				if(this.fileSize <=0){//当文件大小小于等于零时抛出运行时异常错误
					throw new RuntimeException("Unkown file size");
				}
				String filename = getFileName(conn);//获取文件名称
				this.saveFile = new File(fileSaveDir,filename);//根据文件保存目录和文件名构建保存文件
				Map<Integer,Integer> logdata = fileService.getData(downloadUrl);//获取下载记录
				if(logdata.size() > 0){//如果存在下载记录
					for(Map.Entry<Integer, Integer> entry : logdata.entrySet()){
						data.put(entry.getKey(),entry.getValue());//吧各条线程已经下载的数据长度放入data中
					}
				}
				if(this.data.size() ==this.threads.length){
					//如果已经下载的数据的线程数和现在的设置的线程数相同时则计算所有的线程已经下载的数据总长度 
					for(int i=0;i<this.threads.length;i++){//遍历每条线程已经下载的数据
						this.downloadedSize +=this.data.get(i+1);//计算已经下载的数据之和
					}
					print("已经下载的长度"+this.downloadedSize+"个字节");
				}
				//计算每条线程下载的数据长度
				this.block = (this.fileSize % this.threads.length)==0?this.fileSize/this.threads.length : this.fileSize/this.threads.length + 1;
			}else{
				
				print("服务器相应错误："+ conn.getResponseCode()+conn.getResponseMessage());
				throw new RuntimeException("server response error");
				//
			}
		}catch(Exception e){
			print("print"+e.toString());
			throw new RuntimeException("Can't connection this url3");
		}
	}
	
	/**
	 *  获取文件名
	 */
	
	private String getFileName(HttpURLConnection conn){
		//获取下载路径的字符串中获取文件名称
		String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/')+1);
		
		if(filename ==null || "".equals(filename.trim())){
			// 如果获取不到文件名
			for(int i =0;;i++){//无限循环遍历
				String mine = conn.getHeaderField(i);
				//从返回的流中获取特定索引的头字段值
				
				if(mine == null){ //如果遍历到了返回头末尾着退出循环
					break;	
				}
				if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){
					//获取content-disposition  返回头字段，里面可能包含文件名
					
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());//是用正则查询文件名
					if(m.find()){//如果有符合正则表达式规则的字符串
						return m.group(1);
					}
				}
			}
			filename = UUID.randomUUID()+".tmp";
			//由网卡上的表示数字（唯一）以及CPU 始终的唯一数字生成的一个16字节的二进制座位文件名
			
		}
		return filename;
	}
	/**
	 * 开始下载文件
	 * 
	 * 
	 */
	public  int download(DownloadProgressListener listener) throws Exception{
		//进行下载  并抛出异常给调用和，如果有异常的话
		try{
			RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rw");
			if(this.fileSize>0){
				randOut.setLength(this.fileSize);
			}
			randOut.close();
			URL url = new URL(this.downloadUrl);
			if(this.data.size() != this.threads.length){
				this.data.clear();
				for(int i=0;i<this.threads.length;i++){//遍历线程池
					this.data.put(i+1, 0);//初始化每条线程已经下载的数据长度为0
				}
				this.downloadedSize=0;
			}
			for(int i=0;i<this.threads.length;i++){
				int downloadedLength = this.data.get(i+1);
				if(downloadedLength <this.block && this.downloadedSize <this.fileSize){
					this.threads[i] = new DownloadThread(this,url,this.saveFile,this.block,this.data.get(i+1),i+1);
					this.threads[i].setPriority(7);
					//Thread.NORM_PRIORITY=5; Thread.MIN_PRIORITY =1 ;Thread.MAX_PRIORITY =10;		
					
					this.threads[i].start();
				}else{
					this.threads[i] = null;//表明在线程已经完成下载任务
				}
			}
			fileService.delete(this.downloadUrl);//如果存在下载记录，删除它们，然后重新添加
			fileService.save(this.downloadUrl, data);//把已经下载的实时数据写入数据库
			boolean notFinished =true;//下载未完成
			while(notFinished){//循环判断所有线程是否完成下载
				Thread.sleep(900);
				notFinished = false;//假定全部线程下载完成
				for(int i =0 ;i<this.threads.length;i++){
					if(this.threads[i] != null && this.threads[i].isFinished()){
						//如果发现线程未完成下载
						notFinished = true;//设置标志为下载没有完成
						if(this.threads[i].getDownloadedLength() == -1){
							//如果下载失败，再重新在已经下载的数据长度的基础上下载
							this.threads[i] = new DownloadThread(this,url,this.saveFile,this.block,this.data.get(i+1),i+1);//重新开辟下载线程
							this.threads[i].setPriority(7);//设置下载的优先级
							this.threads[i].start();//开始下载线程
						}
					}
				}
				if(listener !=null){
					//通知目前已经下载完成的数据长度
					listener.onDownloadSize(this.downloadedSize);
				}
				
				
			}
			if(downloadedSize == this.fileSize){//下载完成删除记录
				fileService.delete(this.downloadUrl);
			}
			
		}catch(Exception e){
			print(e.toString());
			throw new Exception("File downloads error");
		}
		return this.downloadedSize;
	}
	
	/**
	 * 获取http响应头字段
	 * http   httpUrlconnection对象
	 * 
	 * @return   返回头字段的linkedhashmap
	 *@author wangxinyan
	 * TODO:this is how to do thing
	 */
	public static Map<String , String> getHttpResponseHeader(HttpURLConnection http){
		Map<String, String> header =new LinkedHashMap<String, String>();
		//使用linkedHashMap 保证写入和遍历的时候的顺序相同，而且允许空值存在
		for(int i=0;;i++){//此处为无线循环，因为不知道头字段的数量
			String fileValue = http.getHeaderField(i);
			//http.getHeaderField(i) 返回第i个头字段的值
			if(fileValue ==null) break;
			//如果第i个字段没有值了，则辨明头字段部分已经循环完毕，此处使用break 退出循环；
			header.put(http.getHeaderFieldKey(i), fileValue);
			//getHeaderFieldKey(i)    用于返回第i个头字段的键
		}
		return header;
	}
	/**
	 *打印http头字段
	 *@param  http Http URlconnction 对象 
	 */
	public static void printResponseHeader(HttpURLConnection http){
		Map<String, String> header = getHttpResponseHeader(http);
		//获取http 相应头字段
		for(Map.Entry<String, String> entry :header.entrySet()){
			//for -each   循环方式遍历获取的头字段的值，此时遍历的循环和输入的顺序相同
			String key = entry.getKey() !=null ? entry.getKey()+":" : "";
			// ？ ：当有键的时候获取，如果没有空字符串
			print("printResponseHeader::"+key+entry.getValue());
		}
	}
	
	
	
	private static void print(String msg){
		Log.i(TAG,msg);
	}
}
