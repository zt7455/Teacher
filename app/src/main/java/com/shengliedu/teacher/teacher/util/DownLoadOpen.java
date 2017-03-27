package com.shengliedu.teacher.teacher.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.shengliedu.teacher.teacher.view.CustomDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownLoadOpen {
	
	public static final int TYPE_CT_WAP = 5;
	public static final int TYPE_CT_NET = 6;
	public static final int TYPE_CT_WAP_2G = 7;
	public static final int TYPE_CT_NET_2G = 8;

	public static final int TYPE_CM_WAP = 9;
	public static final int TYPE_CM_NET = 10;
	public static final int TYPE_CM_WAP_2G = 11;
	public static final int TYPE_CM_NET_2G = 12;

	public static final int TYPE_CU_WAP = 13;
	public static final int TYPE_CU_NET = 14;
	public static final int TYPE_CU_WAP_2G = 15;
	public static final int TYPE_CU_NET_2G = 16;

	public static final int TYPE_OTHER = 17;
	
	/** 没有网络 */
	public static final int TYPE_NET_WORK_DISABLED = 0;

	/** wifi网络 */
	public static final int TYPE_WIFI = 4;

	
	private Context context;
	private String pathName = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/zzkt";

	public DownLoadOpen(Context context) {
		super();
		this.context = context;
		File file = new File(pathName);
		if (!file.exists()) {
			file.mkdir();
			Log.v("TAG", "no");
		}else {
			for (int i = 0; i < file.listFiles().length; i++) {
				Log.v("TAG", "yes="+file.listFiles()[i].getAbsolutePath());
			}
		}
	}

	public void downOpen(final String url, final String name,final long size) {
		String namereString = name;
		if (namereString.contains("/")) {
			namereString = name.substring(name.lastIndexOf("/") + 1);
		}
		File file = new File(pathName +File.separator+ namereString);
		Log.v("TAG", "yes1="+pathName +File.separator+ namereString);
		if (file.exists()) {
			CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
			callOtherOpeanFile.openFile(context, file);
		} else {
			int checkNetworkType = NetWorkUtils.checkNetworkType(context);

    		switch (checkNetworkType) {
    		case TYPE_WIFI:
    			Log.i("NetType", "================wifi");
    			downLoadFile(url, name,size);
    			break;
    		case TYPE_NET_WORK_DISABLED:
    			Log.i("NetType", "================no network");
    			Toast.makeText(context, "无网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
    			break;
    		case TYPE_CT_WAP:
    			Log.i("NetType", "================ctwap");
    		case TYPE_CT_WAP_2G:
    			Log.i("NetType", "================ctwap_2g");
    		case TYPE_CT_NET:
    			Log.i("NetType", "================ctnet");
    		case TYPE_CT_NET_2G:
    			Log.i("NetType", "================ctnet_2g");
    		case TYPE_CM_WAP:
    			Log.i("NetType", "================cmwap");
    		case TYPE_CM_WAP_2G:
    			Log.i("NetType", "================cmwap_2g");
    		case TYPE_CM_NET:
    			Log.i("NetType", "================cmnet");
    		case TYPE_CM_NET_2G:
    			Log.i("NetType", "================cmnet_2g");
    		case TYPE_CU_NET:
    			Log.i("NetType", "================cunet");
    			break;
    		case TYPE_CU_NET_2G:
    			Log.i("NetType", "================cunet_2g");
    		case TYPE_CU_WAP:
    			Log.i("NetType", "================cuwap");
    		case TYPE_CU_WAP_2G:
    			Log.i("NetType", "================cuwap_2g");
    		case TYPE_OTHER:
    			Log.i("NetType", "================other");
    			PushDeliog(url, name, size);
    			break;
    		default:
    			break;
    		}
		}
	}	

	private void PushDeliog(final String url, final String name, final long size) {
		CustomDialog.Builder customDialog = new CustomDialog.Builder(
				context);
		customDialog.setMessage("您正在使用移动网络数据,是否下载该文件？");
		customDialog.setTitle("提示");
		customDialog.setPositiveButton("下载",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						downLoadFile(url, name,size);
						dialog.dismiss();
						// 设置你的操作事项
					}
				});
		customDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		customDialog.create().show();
	}

	/*
	 * 02 * 从服务器中下载
	 */
	protected void downLoadFile(final String url, final String name,final long size) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("正在下载...");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(url, name, pd,size);
					sleep(1000);
					CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
					callOtherOpeanFile.openFile(context, file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private File getFileFromServer(String path, String name, ProgressDialog pd,long size)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		System.out.println(path);
		System.out.println(name);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Log.v("path", path+"path");
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setRequestProperty("Accept-Encoding", "identity"); 
			//获得文件的长度  
			conn.setConnectTimeout(5 * 60 * 1000);
			int contentLength = conn.getContentLength();  
			System.out.println("长度 :"+contentLength);  
			// 获取到文件的大小
			// pd.setMax(conn.getContentLength());
			Log.v("size", size+"size");
			pd.setMax(Integer.parseInt(size+""));
			int code = conn.getResponseCode();  
			System.out.println("code :"+code);  
			InputStream is = conn.getInputStream();
//			File file1 = null;
//			file1 = new File(pathName);
//			if (!file1.exists()) {
//				file1.mkdir();
//			}
			if (name.contains("/")) {
				name = name.substring(name.lastIndexOf("/") + 1);
			}
			pd.setTitle(name);
			File file = new File(pathName + File.separator + name);
			Log.v("TAG", "path=" + (pathName + File.separator + name));
			Log.v("TAG", "file=" + (!file.exists()));
			if (!file.exists()) {
				Log.v("TAG", "in");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len=0;
				int total = 0;
				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
					total += len;
					Log.v("TAG", "total=" + total);
					// 获取当前下载量
					pd.setProgress(total);
				}
				fos.close();
				bis.close();
				is.close();
			}
			return file;
		} else {
			return null;
		}
	}
}
