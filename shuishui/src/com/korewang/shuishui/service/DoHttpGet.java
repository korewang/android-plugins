package com.korewang.shuishui.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;


/**
 * @params 此类是测试用的，只是作为login登录的一个判断，请求验证参数
 * @author wangxinyan
 * @date 2015年12月9日 &*& by class DoHttpGet
 * @params 如何使用 boolean result= DoHttpGet.login(title, length) result 是true
 *         成功，false 失败 在子线程执行
 * 
 */
public class DoHttpGet {
	private static final int REQUESTOK = 200;
	static String baseUrl = BaseConstants.BASEURL;

	public DoHttpGet(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public static boolean login(String title, String length) throws Exception {
		if (baseUrl.isEmpty()) {
			baseUrl = BaseConstants.BASEURL;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", title);
		params.put("userpsd", length);
		return sendGETRequest(baseUrl, params, "UTF-8");
	}

	/**
	 * 
	 * @author wangxinyan TODO:发送GET请求
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 */
	private static boolean sendGETRequest(String baseUrl2,
			Map<String, String> params, String encoding) throws Exception {
		// TODO Auto-generated method stub
		// 可以用baseUrl
		StringBuilder sb = new StringBuilder(baseUrl2);
		if (params != null && !params.isEmpty()) {
			sb.append("?");
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=");
				sb.append(URLEncoder.encode(entry.getValue(), encoding));
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString())
				.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == REQUESTOK) {
			return true;
		}
		return false;
	}

	/**
	 * @ inputstream
	 */
	private static InputStream getInputStreamGetHttp(String baseUrl2,
			Map<String, String> params, String encoding) throws Exception {
		// TODO Auto-generated method stub
		InputStream ips = null;

		StringBuilder sb = new StringBuilder(baseUrl2);
		if (params != null && !params.isEmpty()) {
			sb.append("?");
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=");
				sb.append(URLEncoder.encode(entry.getValue(), encoding));
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString())
				.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == REQUESTOK) {
			ips = conn.getInputStream();
		}
		return ips;
	}

	private static byte[] getByteGetHttp(String baseUrl2,
			Map<String, String> params, String encoding) {
		// 设置图片的byte 流获取
		byte[] data = null;
		InputStream ips = null;
		// / 不关闭输出流，直接内存存起来
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		StringBuilder sb = new StringBuilder(baseUrl2);
		try {
			if (params != null && !params.isEmpty()) {
				sb.append("?");
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey()).append("=");
					sb.append(URLEncoder.encode(entry.getValue(), encoding));
					sb.append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString())
					.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			byte[] b_data = new byte[1024];
			int len = 0;
			if (conn.getResponseCode() == REQUESTOK) {
				ips = conn.getInputStream();
				while ((len = ips.read(b_data)) != -1) {
					byteArrayOutputStream.write(b_data, 0, len);
				}
				data = byteArrayOutputStream.toByteArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ips != null) {
				try {
					ips.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		return data;
		/*
		 *   针对imageview获取图片byte使用的
		 *    byte[]  bt = 
		 *   Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length)
		 * */
	}

	/**
	 * 
	 * @author wangxinyan TODO:POST
	 */
	private static boolean sendPOSTRequest(String baseUrl2,
			Map<String, String> params, String encoding) throws Exception {
		StringBuilder sb = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=");
				sb.append(URLEncoder.encode(entry.getValue(), encoding));
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		byte[] data = sb.toString().getBytes();

		HttpURLConnection conn = (HttpURLConnection) new URL(baseUrl2)
				.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);// 允许对外传输数据
		conn.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", data.length + "");
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		if (conn.getResponseCode() == REQUESTOK) {
			return true;
		}
		return false;

	}

	/**
	 * http 封装
	 */
	private static boolean sendHttpPOST(String baseUrl2,
			Map<String, String> params, String encoding) throws Exception {
		// TODO Auto-generated method stub
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				pairs.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);

		HttpPost post = new HttpPost(baseUrl2);
		post.setEntity(entity);
		DefaultHttpClient client = new DefaultHttpClient();

		// 请求超时
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
		// 读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

		HttpResponse response = client.execute(post);// 异步操作
		if (response.getStatusLine().getStatusCode() == REQUESTOK) {
			return true;
		}
		return false;

	}

	public static HttpURLConnection doGet(String url) throws Exception {
		StringBuilder str = new StringBuilder(url);
		URL url1 = new URL(str.toString());
		HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
		conn.setRequestMethod("GET");
		/*
		 * if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){ return conn; }
		 */
		return conn;
	}

}

class BaseConstants {
	public static final String BASEURL = "http://172.16.130.4:8080/ServletTest/JsonList";

	// 本地的servlet测试， 模拟器用 10.0.0.2 alices
	public BaseConstants() {
		HttpURLConnection coo;
		try {
			coo = (HttpURLConnection) DoHttpGet.doGet(BASEURL);
			int resultCode = coo.getResponseCode();
			if (resultCode == HttpURLConnection.HTTP_OK) {

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
