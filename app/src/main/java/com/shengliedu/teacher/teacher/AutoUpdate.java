package com.shengliedu.teacher.teacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.shengliedu.teacher.teacher.util.HandlerMessageObj;
import com.shengliedu.teacher.teacher.view.CustomDialog;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AutoUpdate {
	private static String apk_url;
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				HandlerMessageObj handlerMessageObj= (HandlerMessageObj) msg.obj;
				String json= handlerMessageObj.getJson();
				String type= handlerMessageObj.getType();
				final Context context= handlerMessageObj.getContext();
				Log.v("TAG", "ceshi2" + json);
				try {
					JSONObject ja = new JSONObject(json);
					String version = ja.getString("version");
					final int size = ja.getInt("size");
					apk_url = ja.getString("downloadUrl");
					String vson = getVersionName(context).trim();
					if (!vson.equals(version)) {
						CustomDialog.Builder customDialog = new CustomDialog.Builder(
								context);
						customDialog.setMessage("亲,有新版本了,是否下载更新？");
						customDialog.setTitle("提示");
						customDialog.setPositiveButton("更新",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										downLoadApk(context, size);
										dialog.dismiss();
										// 设置你的操作事项
									}
								});
						customDialog.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						customDialog.create().show();
					} else {
						if ("1".equals(type)) {
							Toast.makeText(context, "已是最新版本！", Toast.LENGTH_SHORT)
									.show();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (msg.what==2){
				Context context= (Context) msg.obj;
				Toast.makeText(context, "版本信息获取失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	/**
	 * 版本自动检测更新
	 */
	public  void getVersionData(final Context context, final String type) {

		OkHttpClient client=new OkHttpClient();
		// RequestParams params = new RequestParams();
		Log.v("TAG", "ceshi");
		Request.Builder requestBuilder = new Request.Builder().url("http://dc.yunclass.com/apk/teacherAppVersion.json");
		Request request = requestBuilder.build();
		Call mcall = client.newCall(request);
		mcall.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				Message message=Message.obtain();
				message.what=1;
				HandlerMessageObj handlerMessageObj=new HandlerMessageObj();
				handlerMessageObj.setContext(context);
				handlerMessageObj.setType(type);
				handlerMessageObj.setJson(response.body().string());
				message.obj=handlerMessageObj;
				handlerReq.sendMessage(message);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Message message=Message.obtain();
				message.what=2;
				message.obj=context;
				handlerReq.sendMessage(message);
			}
		});
	}

	/*
	 * 02 * 从服务器中下载APK 03
	 */
	protected static void downLoadApk(final Context context, final int size) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(apk_url, pd, size);
					sleep(1000);
					installApk(file, context);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 安装apk
	protected static void installApk(File file, Context context) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
		context.startActivity(intent);
	}

	public static File getFileFromServer(String path, ProgressDialog pd,
			int size) throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 60 * 1000);
			// 获取到文件的大小
			// pd.setMax(conn.getContentLength());
			pd.setMax(size);
			InputStream is = conn.getInputStream();
			String ph = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/elc";
			File file1 = null;
			file1 = new File(ph);
			if (!file1.exists()) {
				file1.mkdir();
			}
			File file = new File(ph + File.separator + "elc.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}

	/*
	 * 获取当前程序的版本号
	 */
	private static String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packInfo.versionName;
	}

}
