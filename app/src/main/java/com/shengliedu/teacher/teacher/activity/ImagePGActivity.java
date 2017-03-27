package com.shengliedu.teacher.teacher.activity;

import android.text.TextUtils;
import android.util.Log;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class ImagePGActivity extends BaseActivity {
	private String link;
	private WebView webview;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		link = (String) getIntent().getExtras().get("link");
		initViewSwf();
	}
	private void initViewSwf() {
		Log.v("TAG", "isTbsCoreInited:"+QbSdk.isTbsCoreInited());
		webview = (WebView) findViewById(R.id.webView);
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView webView, String s) {
				super.onPageFinished(webView, s);
				showRoundProcessDialogCancel();
			}
		});
		WebSettings webSetting = webview.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(link)) {
			webview.loadUrl(link);
			showRoundProcessDialog(this);
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_pg_image;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		ViewGroup view = (ViewGroup) getWindow().getDecorView();
//        view.removeAllViews();
		if(webview != null ) {
			webview.destroy();
			webview=null;
		}
        super.finish();

		super.onDestroy();
	}
}
