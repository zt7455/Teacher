package com.shengliedu.teacher.teacher.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.shengliedu.teacher.teacher.R;

public class PopupWindowUtils {

	/**
	 * 注 ：此方法生成的PopUpwindow 是从窗口底部弹出的 因为 我已经写
	 * 
	 * @param view
	 *            要填充的布局
	 * @param id
	 *            这个界面布局的id 例如 R.layout.你所在界面的布局
	 * @param context
	 * @return 返回值是你想要的context
	 */
	@SuppressWarnings("deprecation")
	public static PopupWindow showCamerPopupWindow(View view, int id,
			Context context) {
		// 要获取的popupwindow对象
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		PopupWindow mPopupWindow = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);

		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		mPopupWindow.showAtLocation(
				LayoutInflater.from(context).inflate(id, null), Gravity.BOTTOM,
				0, 0);
		return mPopupWindow;
	}

	/**
	 * 百度地图 的弹出框
	 * 
	 * @param view
	 * @param id
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static PopupWindow showMapPopupWindow(View view, int id,
			Context context) {
		// 要获取的popupwindow对象
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		PopupWindow mPopupWindow = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);

		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.showAtLocation(
				LayoutInflater.from(context).inflate(id, null), Gravity.BOTTOM,
				0, 0);
		mPopupWindow.setOutsideTouchable(true);
		return mPopupWindow;
	}

	/**
	 * 百度地图 标题弹出框
	 * 
	 * @params view
	 * @params id
	 * @params context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static PopupWindow showMapTitlePopupWindow(View view, View anchor,
			Context context, int xoff, int yoff) {
		// 要获取的popupwindow对象
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		PopupWindow mPopupWindow = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);

		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.showAsDropDown(anchor, xoff, yoff);
		mPopupWindow.setOutsideTouchable(true);
		return mPopupWindow;
	}

}
