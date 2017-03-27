package com.shengliedu.teacher.teacher.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shengliedu.teacher.teacher.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

@SuppressLint("ValidFragment")
public class BookPageHtmFragment extends Fragment {

	private View view;
	private String urlString;
	private WebView webview;

	public BookPageHtmFragment() {
		super();
	}

	public BookPageHtmFragment(String urlString) {
		super();
		this.urlString = urlString;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_ziyuan, null);
		webview = (WebView) view.findViewById(R.id.web);
		initWeb();
		webview.loadUrl(urlString);
		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWeb() {
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				super.onProgressChanged(view, progress);

			}
		});
		WebSettings webSetting = webview.getSettings();
		webSetting.setUserAgentString(webSetting.getUserAgentString()
				+ "parent");
		webSetting.setJavaScriptEnabled(true);
		webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSetting.setMediaPlaybackRequiresUserGesture(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(false);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setPluginsPath("/data/data/"
				+ getActivity().getPackageName() + "/app_plugins/"); // 注意
		webSetting.setAppCachePath(getActivity().getDir("appcache", 0)
				.getPath());
		webSetting.setDatabasePath(getActivity().getDir("databases", 0)
				.getPath());
		webSetting.setGeolocationDatabasePath(getActivity().getDir(
				"geolocation", 0).getPath());
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		Log.d("Fragmet事件", "OK");
		if (webview.canGoBack()) {
			Log.v("TAG", "2");
			webview.goBack();
			return true;
		}
		return false;
	}
}
