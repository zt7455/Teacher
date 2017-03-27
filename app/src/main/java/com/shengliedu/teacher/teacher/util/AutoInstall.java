package com.shengliedu.teacher.teacher.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AutoInstall {
	private static String mUrl;
	private static Context mContext;

	/**
	 * 外部传进来的url以便定位需要安装的APK
	 * 
	 * @param url
	 */
	public static void setUrl(String url) {
		mUrl = url;
	}

	private static int flashCode;

	private static void getVersion(Context context, String install) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(install,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			Log.i("versionName", info.versionName);
			Log.i("versionCode", info.versionCode + "");
			flashCode = info.versionCode;
		}
	}

	/**
	 * 安装
	 * 
	 * @param context
	 *            接收外部传进来的context
	 */
	public  void install(final Context context, String installName,
			String versionName) {
		mContext = context;
		boolean isInstall = false;
		isInstall = isInstalled(context, versionName);
		
		Log.v("TAG", "ass="+Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/"
				+ installName);
		copyApkFromAssets(context, installName, Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/"
				+ installName);

		if (isInstall) {
			try {
				PackageManager pm = context.getPackageManager();
				PackageInfo pi = pm.getPackageInfo(versionName, 0);
				getVersion(context, Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/" + installName);
				
				
//				if ("com.tencent.x5sdk.demo".equals(versionName)) {
//					Log.v("TAG", "versionName="+versionName);
//					Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
//					resolveIntent.setPackage(pi.packageName);
//					PackageManager pManager = context.getPackageManager();
//					List apps = pManager
//							.queryIntentActivities(resolveIntent, 0);
//
//					ResolveInfo ri = (ResolveInfo) apps.iterator().next();
//					if (ri != null) {
//						String className = ri.activityInfo.name;
//						Intent intent = new Intent(Intent.ACTION_MAIN);
//						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						ComponentName cn = new ComponentName(versionName,
//								className);
//						intent.setComponent(cn);
//						context.startActivity(intent);
//						new Thread() {
//							public void run() {
//								ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//								
//								while (true) {
//									String topActivityName = mActivityManager.getRunningTasks(1)
//											.get(0).topActivity.getPackageName();
//									Log.v("TAG", "topActivityName="+topActivityName);
//									Log.v("TAG", "haha="+("com.tencent.x5sdk.demo".equals(topActivityName)));
//									if ("com.tencent.x5sdk.demo".equals(topActivityName)) {
//										Log.v("TAG","10");
//										handler.sendEmptyMessage(200);
//										Log.v("TAG","11");
//										break;
//									}
//									
//								}
//							}
//						}.start();
//					}
//				}
				if (pi.versionCode >= flashCode) {
					return;
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), installName)),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.v("TAG", "msg="+msg.what);
//			Toast.makeText(mContext, "msg="+msg.what, 0).show();
			switch (msg.what) {
			case 200:
//				Intent it1 = new Intent(mContext, UsbActivity.class);
//				it1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				mContext.getApplicationContext().startActivity(it1);
				break;

			default:
				break;
			}
		}
	};

	public static boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}

	/**
	 * 判断应用是否已安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName) {
		boolean hasInstalled = false;
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> list = pm
				.getInstalledPackages(0);
		for (PackageInfo p : list) {
			if (packageName != null && packageName.equals(p.packageName)) {
				hasInstalled = true;
				break;
			}
		}
		return hasInstalled;
	}
}