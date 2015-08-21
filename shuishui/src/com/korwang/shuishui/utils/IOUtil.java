package com.korwang.shuishui.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;



public class IOUtil {
	 private static final String TAG = "IOUtil";
		private IOUtil(){}

		public static void fromTo( InputStream is, OutputStream os ) throws IOException {
			if( is == null || os == null ) {
				return;
			}

			byte [] buffer = new byte[1024];//1k bytes buffer
			int size = is.read( buffer, 0, 1024);
			while( size > 0 ){
				os.write( buffer, 0 , size );
				size = is.read( buffer, 0, 1024);
			}

			os.flush();
		}

		public static String read( Reader reader ) throws IOException {

			char [] buffer = new char[512];
			StringBuilder sb = new StringBuilder();
			int count = reader.read(buffer);

			while( count > 0 ){
				sb.append( buffer, 0, count );
				count = reader.read(buffer);
			}

			return sb.toString();

		}

		public static String read(InputStream is, String charSet){
			if( is == null ) {
				return null;
			}

			StringBuilder sb = new StringBuilder();
			char [] chBuf = new char[200];
			try {
				InputStreamReader reader = new InputStreamReader(is,charSet);
				int len = reader.read(chBuf);
				while(len != -1){
					sb.append(chBuf, 0, len);
					len = reader.read(chBuf);
				}
			} catch (IOException e) {
				LogUtil.d(TAG, e.getMessage());
			} finally{
				IOUtil.close(is);
			}
			
			return sb.toString();
		}

		public static String read(InputStream is){
			return read(is, "utf-8");
		}

		public static byte [] readBytes( InputStream is ) throws IOException {
			ByteArrayOutputStream bos= new ByteArrayOutputStream();
			IOUtil.fromTo( is, bos );
			return bos.toByteArray();
		}

		public static void close( Closeable... closeables){
			for(Closeable closeable : closeables){
				if(null != closeable){
					try {
						closeable.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
}
