package com.shengliedu.teacher.teacher.showclass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.SynLianXianAdapter;
import com.shengliedu.teacher.teacher.bean.AfterClassLianXian;
import com.shengliedu.teacher.teacher.pdf.ShowPdfFromUrlActivity;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.HtmlImage;
import com.shengliedu.teacher.teacher.util.NumberToCode;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.view.NoScrollListview;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynSanLianXianActivity extends BaseActivity {
	private String type;
	private String name;
	private String content_host;
	private String questionContentType;
	private int school;
	private int from;
	private int id;

	public TextView select_title, pandun, zhengquelv, syn_question_head;
	public Button select_btn;
	private String ques = "";
	private String rques = "";
	private Map<Integer, Boolean> map2 = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private ListView listView;
	private NoScrollListview listView1;
	private NoScrollListview listView2;
	private AfterClassLianXian data;
	private LinearLayout sanlianxLin;
	private TextView select_pdf;
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
		questionContentType = (String) getIntent().getExtras().get(
				"questionContentType");
		setBack();
		showTitle(Html.fromHtml(name)+"");
		sanlianxLin = (LinearLayout) findViewById(R.id.sanlianxLin);
		select_title = (TextView) findViewById(R.id.syn_question_title);
		select_pdf = (TextView) findViewById(R.id.select_pdf);
		pandun = (TextView) findViewById(R.id.syn_question_pandun);
		listView = (NoScrollListview) findViewById(R.id.syn_question_lv);
		listView1 = (NoScrollListview) findViewById(R.id.syn_question_lv1);
		listView2 = (NoScrollListview) findViewById(R.id.syn_question_lv2);
		select_btn = (Button) findViewById(R.id.jiexi);
		zhengquelv = (TextView) findViewById(R.id.zhengquelv);
		select_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iteIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("explanation", data.explanation);
				iteIntent.putExtras(bundle);
				iteIntent.setClass(SynSanLianXianActivity.this,
						SynSeletQuestionAnalyticalActivity.class);
				if (TextUtils.isEmpty(data.explanation)) {
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
				List<AfterClassLianXian> dataListTemp = JSON
						.parseArray(object.getString("data"),
								AfterClassLianXian.class);
				if (dataListTemp != null) {
					data = dataListTemp.get(0);
					select_title.setText(Html.fromHtml(HtmlImage
							.deleteSrc(data.question.question)));
					String name = data.question.question;
					List<String> imgList = HtmlImage.getImgSrc(name);
					ImageLoader imageLoader=ImageLoader.getInstance();
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									SynSanLianXianActivity.this);
							imageView
									.setScaleType(ScaleType.FIT_CENTER);
							imageView
									.setLayoutParams(new LayoutParams(
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							imageLoader.displayImage(url,imageView);
							imageView
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated
											// method stub
											HtmlImage htmlImage = new HtmlImage();
											htmlImage
													.showDialog(
															SynSanLianXianActivity.this,
															url);
										}
									});
							sanlianxLin.addView(imageView);
						}
					}
					if ("1".equals(questionContentType)) {
						select_pdf.setVisibility(View.VISIBLE);
						select_pdf.setText("查看PDF题目");
						select_pdf.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(SynSanLianXianActivity.this,ShowPdfFromUrlActivity.class);
								intent.putExtra("url", Config1.IP + data.question.contentLink);
								startActivity(intent);
							}
						});
					} else {
						select_pdf.setVisibility(View.GONE);
					}
					if (data.studentAnswer != null
							&& data.studentAnswer.size() > 0) {
						ques = data.studentAnswer.get(0).title;
					}
					rques = data.correct.get(0).title;
					Log.e("ques", "haha" + ques);
					Log.e("rques", rques);
					String per = data.percentage;
//							Log.e("per", per);
					if (ques != null && ques.length() == 3) {
						map.put(NumberToCode.codeToNumber(ques
								.substring(0, 1)), true);
						map1.put(NumberToCode.codeToNumber(ques
								.substring(1, 2)), true);
						map2.put(NumberToCode.codeToNumber(ques
								.substring(2, 3)), true);
					}
					SynLianXianAdapter adapter = new SynLianXianAdapter(
							SynSanLianXianActivity.this,
							data.answer.answersF.get(0).items, map);
					listView.setAdapter(adapter);
					SynLianXianAdapter adapter1 = new SynLianXianAdapter(
							SynSanLianXianActivity.this,
							data.answer.answersF.get(1).items, map1);
					listView1.setAdapter(adapter1);
					SynLianXianAdapter adapter2 = new SynLianXianAdapter(
							SynSanLianXianActivity.this,
							data.answer.answersF.get(2).items, map2);
					listView2.setAdapter(adapter2);
					String rqueStr = "";
					if (data.correct != null & data.correct.size() != 0) {
						rques = data.correct.get(0).title;
						for (int i = 0; i < data.correct.size(); i++) {
							rqueStr = rqueStr
									+ data.correct.get(i).title + " ";
						}
					}
					int i = 0;
					String text = "";
					if (data.studentAnswer != null
							&& data.studentAnswer.size() > 0) {
						for (int j = 0; j < data.studentAnswer.size(); j++) {
							if (rqueStr.contains(data.studentAnswer
									.get(i).title)) {
								i++;
							}
							text = text
									+ data.studentAnswer.get(j).title
									+ " ";
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
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("id", id+"");
		map3.put("school", school+"");
		map3.put("from", from+"");
		map3.put("content_type", 2);
		doGet(content_host + Config1.getInstance().SHOWCLASSSINGLE(), map3,
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
		return R.layout.activity_syn_sanlianxian;
	}

}
