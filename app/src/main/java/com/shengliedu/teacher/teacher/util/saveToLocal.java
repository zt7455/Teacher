package com.shengliedu.teacher.teacher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class saveToLocal {
	public static String getBkLlPosition(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		return preferences.getString("bkll", "");
	}
	public static void setBkLlPosition(Context context, String userName) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("bkll", userName);
		editor.commit();
	}
	public static String getBkjcPosition(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		return preferences.getString("bkjc", "");
	}
	public static void setBkjcPosition(Context context, String userName) {
		SharedPreferences preferences = context.getSharedPreferences("bk",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("bkjc", userName);
		editor.commit();
	}
}
