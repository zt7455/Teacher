package com.shengliedu.teacher.teacher.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;

public class AppValue {
	
	/**
	 * 判断Service是否运行
	 * @param mContext
	 * @param className
	 * @return
	 */
	public static boolean isServiceRunning(Context mContext,String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
        mContext.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList 
                   = activityManager.getRunningServices(30);
        Log.v("TAGG",serviceList.toString());
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
        	Log.v("TAGG", serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
	
	
	/**
	 * 判断app是否在运行
	 * @param context
	 * @return
	 */
	public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                // find it, break
                break;
            }
        }
        return isAppRunning;
    }
	
	
	/**
	 * 判断app是否在后台运行
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {  
        ActivityManager activityManager = (ActivityManager) context  
                .getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningAppProcessInfo> appProcesses = activityManager  
                .getRunningAppProcesses();  
        for (RunningAppProcessInfo appProcess : appProcesses) {  
            if (appProcess.processName.equals(context.getPackageName())) {  
                /* 
                BACKGROUND=400 EMPTY=500 FOREGROUND=100 
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200 
                 */  
                Log.i(context.getPackageName(), "此appimportace ="  
                        + appProcess.importance  
                        + ",context.getClass().getName()="  
                        + context.getClass().getName());  
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                    Log.i(context.getPackageName(), "处于后台"  
                            + appProcess.processName);  
                    return true;  
                } else {  
                    Log.i(context.getPackageName(), "处于前台"  
                            + appProcess.processName);  
                    return false;  
                }  
            }  
        }  
        return false;  
    }  
}
