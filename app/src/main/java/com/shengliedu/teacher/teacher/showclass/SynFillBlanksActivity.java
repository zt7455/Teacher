package com.shengliedu.teacher.teacher.showclass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.SynQuestion;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.HtmlImage;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynFillBlanksActivity extends BaseActivity {
	private String type;
	private String name;
	private String content_host;
	private int school;
	private int from;
	private int id;
	

	public TextView select_title, pandun, zhengquelv, syn_question_head;
	public Button select_btn;
	private List<SynQuestion> dataList = new ArrayList<SynQuestion>();
	private LinearLayout synsingleLin;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		id = (Integer) getIntent().getExtras().get(
				"id");
		type = (String) getIntent().getExtras().get("type");
		content_host = (String) getIntent().getExtras().get("content_host");
		school = (Integer) getIntent().getExtras().get("school");
		from = (Integer) getIntent().getExtras().get("from");
		name = (String) getIntent().getExtras().get(
				"name");
		setBack();
		showTitle(Html.fromHtml(name)+"");
		synsingleLin = (LinearLayout) findViewById(R.id.synsingleLin);
		select_title = (TextView) findViewById(R.id.syn_question_title);
		pandun = (TextView) findViewById(R.id.syn_question_pandun);
		select_btn = (Button) findViewById(R.id.jiexi);
		zhengquelv = (TextView) findViewById(R.id.zhengquelv);
		select_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("explanation", dataList.get(0).explanation);
				iteIntent.putExtras(bundle);
				iteIntent.setClass(SynFillBlanksActivity.this,
						SynSeletQuestionAnalyticalActivity.class);
				if (TextUtils.isEmpty(dataList.get(0).explanation)) {
					toast("没有解析");
				}else {
					startActivity(iteIntent);
				}
			}
		});
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject object=JSON.parseObject((String) msg.obj);
				List<SynQuestion> dataListTemp = JSON.parseArray(
						object.getString("data"), SynQuestion.class);
				if (dataListTemp != null) {
					dataList.clear();
					dataList.addAll(dataListTemp);
					// Spanned spanned =
					// Html.fromHtml(dataList.get(0).question.question,new
					// MyImageGetter(SynSingleActivity.this,select_title),null);
					select_title.setText(Html.fromHtml(HtmlImage.deleteSrc(dataList.get(0).question.question)));
					// select_title.setMovementMethod(LinkMovementMethod.getInstance());
					String name = dataList.get(0).question.question;
					List<String> imgList = HtmlImage.getImgSrc(name);
					ImageLoader imageLoader=ImageLoader.getInstance();
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									SynFillBlanksActivity.this);
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							imageLoader.displayImage(url,imageView);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated
									// method stub
									HtmlImage htmlImage=new HtmlImage();
									htmlImage.showDialog(SynFillBlanksActivity.this,url);
								}
							});
							synsingleLin.addView(imageView);
						}
					}
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", id+"");
		map1.put("school", school+"");
		map1.put("from", from+"");
		map1.put("content_type", 2);

		doGet(content_host + Config1.getInstance().SHOWCLASSSINGLE(), map1,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json)  {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_syn_fill_blanks;
	}

}
