package com.shengliedu.teacher.teacher.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 日志输出 管理
 * 
 * @author zhangtao
 * 
 */
public class LogUtils {
	public static final boolean LOG_ENABLE = true;
	public static final boolean TOAST_ENABLE = true;

	public static void i(String tag, String message) {
		if (LOG_ENABLE) {
			Log.i(tag, message);
//			 log2view(tag, message);
//			 LogDoc.i("["+tag+"] "+message);
		}
	}
	public static void i_http(String tag, String message) {
		if (LOG_ENABLE) {
			Log.i(tag, message);
//			 log2view(tag, message);
//			 LogDoc.i("["+tag+"] "+message);
		}
	}

	public static void e(String tag, String message) {
		if (LOG_ENABLE) {
			Log.e(tag, message);
//			 LogDoc.i("["+tag+"] "+message);
		}
	}

	public static void c(String s) {
		// e("hj", "" + s);
	}

	public static void b(String s) {
		// e("hj", "" + s);
	}

	public static void a(String s) {
		// e("hj", "" + s);
	}

	public static void h(String s) {
		// e("hj", "" + s);
	}

	public static void j(String s) {
		// e("hj", "" + s);
	}

	public static void hj(String s) {
		// e("hj", "" + s);
	}

	public static void d(String tag, String message) {
		if (LOG_ENABLE)
			Log.d(tag, message);
	}

//	public static void toast(Context context, String message) {
//		if (TOAST_ENABLE) {
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//		}
//	}

	private static final String TEST_TAG = "TEST_TAG";

	public static void msg(String message) {
		// Log.i(TEST_TAG, message);
		// logs.add(message);
	}

	private static List<String> logs1 = new ArrayList<String>();

	public static synchronized void log2view(String message) {
		// String sm = ConstantConfig.DATA_FORMAT_TIME.format(new
		// Date())+" |-- "+message;
		// LogUtil.i("LogUtil", "记录日志:"+sm);
		// if(logs1.size()>500){
		// logs1.remove(0);
		// }
		// logs1.add(sm);
		// if(LOG2SD_ENABLE){
		// LogDog.i(message);
		// }

	}

	public static synchronized void log2view(String tag, String message) {
		// if(LOG2SD_ENABLE){
		// LogDog.i("["+tag+"] "+message);
		// }

	}

	private static final int log_limit = 10 * 1024 * 1024;

	public static void Log2SD() {
		// if(!LOG2SD_ENABLE ||
		// !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		// logs1.clear();
		// return;
		// }
		// if(logs1.size()<=0){
		// return;
		// }
		// final List<String> logs = new ArrayList<String>();
		// logs.addAll(logs1);
		// Log.i("LogUtil", "写日志sd "+logs.size());
		// logs1.clear();
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// File logDir = new
		// File(Environment.getExternalStorageDirectory()+"/BilinLog");
		// if(!logDir.exists() || !logDir.isDirectory()){
		// logDir.mkdir();
		// }
		//
		// File file = new
		// File(Environment.getExternalStorageDirectory()+"/BilinLog/bilin_log.txt");
		// if(file.exists() && file.length()>log_limit){
		// File newPath = new
		// File(Environment.getExternalStorageDirectory()+"/BilinLog/bilin_log_"+ConstantConfig.DATA_FORMAT_TIME.format(new
		// Date())+".txt");
		// boolean ok = file.renameTo(newPath);
		// Log.i("LogUtil","重命�?"+ok);
		// }
		// try {
		// RandomAccessFile raf = new RandomAccessFile(file, "rw");
		// raf.seek(raf.length());
		// int size = logs.size();
		// for (int i = 0; i < size; i++) {
		// String log = logs.get(i);
		// raf.writeUTF("\n"+log);
		// }
		// raf.close();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }).start();
	}

	public static synchronized void clearLogs() {
		LogUtils.i("LogUtil", "清空日志");
		logs1.clear();
	}

	public static synchronized List<String> getLogs() {
		LogUtils.i("LogUtil", "获取日志");
		return logs1;
	}

	public static synchronized void cleanSipLog() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/WukongLog/appLog.txt");
		if (f.exists()) {
			try {
				FileWriter wf = new FileWriter(f);
				wf.write("");
				wf.flush();
				wf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
