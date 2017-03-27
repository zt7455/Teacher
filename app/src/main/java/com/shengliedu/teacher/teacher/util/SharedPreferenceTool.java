package com.shengliedu.teacher.teacher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceTool {


	public static String getChatTime(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("Tdate",
				Context.MODE_PRIVATE);
		return preferences.getString("Ttime", "");
	}

	public static void setChattime(Context context, String time) {
		SharedPreferences preferences = context.getSharedPreferences("Tdate",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("Ttime", time);
		editor.commit();
	}
	public static int getAppRunTimes(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		return preferences.getInt("time", 0);
	}
	
	public static void setAppRunTimes(Context context, int time) {
		SharedPreferences preferences = context.getSharedPreferences("App",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("time", time);
		editor.commit();
	}
	public static String getTeachBkll(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		return preferences.getString("bkll", "");
	}
	
	public static void setTeachBkll(Context context, String bkll) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("bkll", bkll);
		editor.commit();
	}
	public static String getTeachBkll2(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		return preferences.getString("bkll2", "");
	}
	
	public static void setTeachBkll2(Context context, String bkll) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("bkll2", bkll);
		editor.commit();
	}
	public static String getTeachBkjc(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		return preferences.getString("bkjc", "");
	}
	
	public static void setTeachBkjc(Context context, String bkll) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("bkjc", bkll);
		editor.commit();
	}
	public static String getTeachBkjc2(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		return preferences.getString("bkjc2", "");
	}
	
	public static void setTeachBkjc2(Context context, String bkll) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("bkjc2", bkll);
		editor.commit();
	}
	public static String getUserBookPage(Context context,String user_bookId) {
		SharedPreferences preferences = context.getSharedPreferences("tbook",
				Context.MODE_PRIVATE);
		return preferences.getString(user_bookId, "");
	}
	
	public static void setUserBookPage(Context context, String user_bookId,String page) {
		SharedPreferences preferences = context.getSharedPreferences("tbook",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(user_bookId, page);
		editor.commit();
	}
	
	public static String getUserName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("TeacherUser",
				Context.MODE_PRIVATE);
		return preferences.getString("userName", "");
	}
	
	public static void setUserName(Context context, String userName) {
		SharedPreferences preferences = context.getSharedPreferences("TeacherUser",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("userName", userName);
		editor.commit();
	}

	public static String getPassword(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("TeacherUser",
				Context.MODE_PRIVATE);
		return preferences.getString("password", "");
	}

	public static void setPassword(Context context, String password) {
		SharedPreferences preferences = context.getSharedPreferences("TeacherUser",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("password", password);
		editor.commit();
	}
}
