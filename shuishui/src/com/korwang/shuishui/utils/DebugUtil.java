package com.korwang.shuishui.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.korewang.shuishui.Constants;

import android.os.Environment;
import android.text.TextUtils;



public final class DebugUtil {
		private static final String TAG = "DebugUtil";
		public static boolean isDEBUG = false;
		private static final String INFO = "Info:";
		private static final String WARNNING = "Warning:";
		private static final String ERROR = "Error:";
		private static final int TRACE_LOG_FILE_SIZE = 1024 * 1024 * 2;// 2M
	
	private DebugUtil()
	{
		
	}
	public static void setUncaughtExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                traceThrowableLog(ERROR, e);
            }
        });
	}
	public static void traceThrowableLog(Throwable e) {
		traceThrowableLog(WARNNING, e);
	}
	public static void traceThrowableLog(String label, Throwable e) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PrintWriter writer = null;
		try {
			// 1.打印堆栈到Console
			e.printStackTrace();

			// 2.记录堆栈到trace.log文件
			writer = getDebugLogWriter();
			if (null == writer) {
				return;
			}

			writer.write(label + "Exception time: " + format.format(new Date())
					+ "\n");
			e.printStackTrace(writer);
			writer.write("\n\n");
			writer.flush();
		} catch (Exception ex) {
			//
		} finally {
			IOUtil.close(writer);
		}
	}
	public static void traceLog(String logContent) {
        LogUtil.d(TAG, logContent);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PrintWriter writer = null;
		try {
			writer = getDebugLogWriter();
			if (null == writer) {
				return;
			}
			writer.write(INFO + format.format(new Date()) + "\n");
			writer.write(logContent);
			writer.write("\n\n");
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtil.close(writer);
		}
	}

	
	private static PrintWriter getDebugLogWriter()
			throws FileNotFoundException, UnsupportedEncodingException {
		String rootPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File logDir = new File(rootPath + File.separator + Constants.PRIVATE_DATA_PATH);
		if (!TextUtils.isEmpty(rootPath)
				&& (logDir.exists() || logDir.mkdirs())) {
			FileOutputStream fos = null;
			File logFile = new File(rootPath + File.separator + Constants.PRIVATE_DATA_PATH + "trace.txt");
			// 日志文件大于2M后，覆盖重写
			if (logFile.exists() && logFile.length() > TRACE_LOG_FILE_SIZE) {
				fos = new FileOutputStream(logFile, false);
			} else {
				fos = new FileOutputStream(logFile, true);
			}
			return new PrintWriter(new OutputStreamWriter(fos, Constants.DEFAULT_ENCODEING), false);
		}
		return null;
	}
}
