package com.shengliedu.teacher.teacher.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;

public class LoadingDialog extends Dialog {
	private Context context = null;
	private static LoadingDialog LoadingDialog = null;

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LoadingDialog createDialog(Context context) {
		LoadingDialog = new LoadingDialog(context, R.style.CustomProgressDialog);
		LoadingDialog.setContentView(R.layout.loading_process_dialog_color);
		LoadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return LoadingDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (LoadingDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) LoadingDialog
				.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	/**
	 * 
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LoadingDialog setTitile(String strTitle) {
		return LoadingDialog;
	}

	/**
	 * 
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) LoadingDialog
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return LoadingDialog;
	}
}
