package com.shengliedu.teacher.teacher.activity;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.util.Config1;

public class MessageInfoActivity extends BaseActivity {
	private int id;
	private WebView webView;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		id = getIntent().getIntExtra("id", 0);
		setBack();
		showTitle("通知");
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_message_info;
	}

	private void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.web);
		WebSettings settings = webView.getSettings();
//		int screenDensity = getResources().getDisplayMetrics().densityDpi;
//		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//		switch (screenDensity) {
//		case DisplayMetrics.DENSITY_LOW:
//			zoomDensity = WebSettings.ZoomDensity.CLOSE;
//			break;
//		case DisplayMetrics.DENSITY_MEDIUM:
//			zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//			break;
//		case DisplayMetrics.DENSITY_HIGH:
//			zoomDensity = WebSettings.ZoomDensity.FAR;
//			break;
//		}
//		settings.setDefaultZoom(zoomDensity);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		showRoundProcessDialog(this);
		webView.loadUrl(Config1.getInstance().MESSAGE_INFO() + id);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				showRoundProcessDialogCancel();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
