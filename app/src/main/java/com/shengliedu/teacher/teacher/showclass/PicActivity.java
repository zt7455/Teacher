package com.shengliedu.teacher.teacher.showclass;

import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;

public class PicActivity extends BaseActivity {
	private String plan_info;
	private String content_host;
	private String link;
	private ImageView imageView;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		content_host = (String) getIntent().getExtras().get("content_host");
		link = (String) getIntent().getExtras().get("html");
		plan_info = (String) getIntent().getExtras().get("plan_info");
		setBack();
		showTitle(plan_info);
		imageView=getView(R.id.syn_png);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			hideTitle();
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			showTitle(plan_info);
		}
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Log.v("TAG", "link="+link);
		if (!TextUtils.isEmpty(link)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(content_host+link, imageView);
//			BitmapUtils utils=new BitmapUtils(this);
//			utils.display(imageView, content_host+link);
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_synpic;
	}

}
