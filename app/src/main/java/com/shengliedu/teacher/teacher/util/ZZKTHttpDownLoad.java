package com.shengliedu.teacher.teacher.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ZZKTHttpDownLoad {

	private OkHttpClient okHttpClient;
	private boolean isRedirect;
//	private Handler handlerReq=new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (msg.what==1){
//
//			}else if (msg.what==2){
//
//			}
//		}
//	};
	public ZZKTHttpDownLoad() {
		// TODO Auto-generated method stub
		if (okHttpClient==null) {
			okHttpClient=new OkHttpClient();
		}
	}
	private void makeFile(File file) {
		if (file.getParentFile().exists()) {
			if (file.isDirectory()){
				file.mkdir();
			}
		} else {
			makeFile(file.getParentFile());
			makeFile(file);
		}
	}
	public void downloads(String url, String target,
						  final Callback callback) {
		// TODO Auto-generated method stub
		 final File file=new File(target);
		makeFile(file);
//		if(!file.exists()){
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		Log.v("TAG", "url="+url);
		Request request = new Request.Builder().url(url).build();
		okHttpClient.newCall(request).enqueue(new Callback(){
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d("zhangtao", "下载失败");
				callback.onFailure(call,e);
			}

			@Override
			public void onResponse(Call call, Response response) {
				Log.d("zhangtao", "是否重定向："+response.isRedirect());
				InputStream inputStream = response.body().byteStream();
				FileOutputStream fileOutputStream = null;
				try {
					fileOutputStream = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inputStream.read(buffer)) != -1) {
						fileOutputStream.write(buffer, 0, len);
					}
					fileOutputStream.flush();
				} catch (IOException e) {
					Log.i("zhangtao", "IOException");
					e.printStackTrace();
				}
				Log.d("zhangtao", "文件下载成功");
				try {
					callback.onResponse(call,response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

//	class MyhttpRedirectHandler implements HttpRedirectHandler {
//		private String target;
//		private RequestCallBack<File> callback;
//		public MyhttpRedirectHandler(String target,
//				RequestCallBack<File> callback) {
//			super();
//			this.target = target;
//			this.callback = callback;
//		}
//
//		@Override
//		public HttpRequestBase getDirectRequest(HttpResponse response) {
//			// TODO Auto-generated method stub
//			isRedirect=true;
//			Header[] headers = response.getAllHeaders();
//			Log.v("TAG", "response:"+headers.length);
//			for (int i = 0; i < headers.length; i++) {
//				Header header = headers[i];
//				if ("location".equals(header.getName().toLowerCase())) {
//					// 判断当前字符串的编码格式
//					try {
//						String location = new String(header.getValue()
//								.getBytes("iso8859-1"), "utf-8");
//						Log.v("TAG", "location:"+location.replace(" ", ""));
//						downloads(location.replace(" ", ""), target, callback);
//						break;
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			return null;
//		}
//	}
}
