package com.shengliedu.teacher.teacher.showclass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.util.AutoInstall;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class FlashHtmlActivity extends BaseActivity {
	private String plan_info;
	private String link;
	private String content_host;
	private WebView webview;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		link = (String) getIntent().getExtras().get("html");
		content_host = (String) getIntent().getExtras().get("content_host");
		plan_info = (String) getIntent().getExtras().get("plan_info");
		setBack();
		showTitle(plan_info);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			hideTitle();
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			showTitle(plan_info);
		}
		installApk();
		initViewSwf();
	}
	private void installApk() {
		AutoInstall autoInstall=new AutoInstall();
		autoInstall.install(FlashHtmlActivity.this,
				"Adobe+Flash+Player+Android+4.4.apk", "com.adobe.flashplayer");
		autoInstall.install(FlashHtmlActivity.this, "TBSDemo.apk", "com.tencent.x5sdk.demo");
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initViewSwf() {
		Log.v("TAG", "isTbsCoreInited:"+QbSdk.isTbsCoreInited());
		webview = (WebView) findViewById(R.id.syn_flash);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		webview.setWebViewClient(new WebViewClient(){
			
			/**
			 * 防止加载网页时调起系统浏览器
			 */
			public boolean shouldOverrideUrlLoading(WebView view,String url){
					view.loadUrl(url);
					return true;	
//				return false;
				}
			public void onReceivedHttpAuthRequest(WebView webview, com.tencent.smtt.export.external.interfaces.HttpAuthHandler httpAuthHandlerhost, String host, String realm) {
				boolean flag = httpAuthHandlerhost.useHttpAuthUsernamePassword();
				Log.i("yuanhaizhou", "useHttpAuthUsernamePassword is"+flag);
				Log.i("yuanhaizhou", "HttpAuth host is" +host);
				Log.i("yuanhaizhou", "HttpAuth realm is" + realm);
				
				}
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				 handler.proceed(); 
				super.onReceivedSslError(view, handler, error);
			}
			@Override
			public void onDetectedBlankScreen(String arg0, int arg1) {
				// TODO Auto-generated method stub
				super.onDetectedBlankScreen(arg0, arg1);
			}
			
			
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView arg0,
					String arg1) {
				// TODO Auto-generated method stub
				
				return super.shouldInterceptRequest(arg0, arg1);
			}
			
		
			
			
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				super.onProgressChanged(view, progress);

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
		Log.v("TAG", "link="+link);
		if (!TextUtils.isEmpty(link)) {
			if (link.contains("http:")||link.contains("https:")) {
				webview.loadUrl(link);
				Log.v("TAG", "ur="+link);
			}else {
				try {
					Log.v("TAG", "ur="+content_host+link);
					webview.loadUrl(URLDecoder.decode(content_host+link, "UTF-8"));
//					appendFlashPlugin(URLDecoder.decode(content_host+link, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_flashhtml;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
		if(webview != null ) {
			webview.destroy();
		}
		super.onDestroy();
	}
	
	private void appendFlashPlugin(String flashUrl){
		String temp = "<html><body bgcolor=\"" + "black"  
                + "\"> <br/><embed src=\"" + flashUrl + "\" width=\"" + "100%"  
                + "\" height=\"" + "90%" + "\" scale=\"" + "noscale"  
                + "\" type=\"" + "application/x-shockwave-flash"  
                + "\"> </embed></body></html>";  
		String mimeType = "text/html";  
		String encoding = "utf-8";  
		webview.loadDataWithBaseURL("null", temp, mimeType, encoding, ""); 
	}
}
