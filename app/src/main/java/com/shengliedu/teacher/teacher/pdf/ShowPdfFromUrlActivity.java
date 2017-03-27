package com.shengliedu.teacher.teacher.pdf;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.util.ZZKTHttpDownLoad;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowPdfFromUrlActivity extends BaseActivity implements
		OnPageChangeListener {
	private String url;
	private PDFView pdfView;

	private void display(File fileName) {
		if (fileName != null) {
			Log.v("TAG", "fileName=" + fileName.getAbsolutePath());
			pdfView.fromFile(fileName).defaultPage(1).onPageChange(this).load();
		}
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				File file= (File) msg.obj;
				showRoundProcessDialogCancel();
				display(file);
			}else if (msg.what==2){
				showRoundProcessDialogCancel();
			}
		}
	};
	private void downLoad(String url) {
		// TODO Auto-generated method stub
		File file00 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/zzkt/");
		if (!file00.exists()) {
			file00.mkdir();
		}
		File file0 = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/zzkt/pdf/");
		if (!file0.exists()) {
			file0.mkdir();
		}
		final File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/zzkt/pdf/"
				+ url.substring(url.lastIndexOf("/") + 1));
		Log.v("TAG", "aaaa=" + replace(url));
		if (!file.exists()) {
			showRoundProcessDialog(this);
			ZZKTHttpDownLoad zzktHttpDownLoad = new ZZKTHttpDownLoad();
			zzktHttpDownLoad.downloads(replace(url), Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/zzkt/pdf/" + url.substring(url.lastIndexOf("/") + 1),
					new Callback() {
						@Override
						public void onFailure(Call call, IOException e) {
							handlerReq.sendEmptyMessage(2);
						}

						@Override
						public void onResponse(Call call, Response response) throws IOException {
							Message message=Message.obtain();
							message.what=1;
							message.obj=file;
							handlerReq.sendMessage(message);
						}
					});

		} else {
			display(file);
		}
	}

	private String replace(String str) {
		// TODO Auto-generated method stub
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int v = (int) ch;
			if (v >= 19968 && v <= 171941) {
				str = str.replace(ch + "", Uri.encode(ch + ""));
				// xxx是你要替换成的内容
				str = str.replace("\\", "/");
			}
		}
		return str;
	}

	@Override
	public void onPageChanged(int page, int pageCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		url = getIntent().getStringExtra("url");
		pdfView = getView(R.id.pdfView);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(url)) {
			downLoad(url);
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_showpdf;
	}
}
