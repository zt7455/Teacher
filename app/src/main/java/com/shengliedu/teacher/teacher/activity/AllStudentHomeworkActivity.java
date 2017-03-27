package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.PAllHomeworkStudentAdapter;
import com.shengliedu.teacher.teacher.bean.BeanHomework2;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.pdf.ShowPdfFromUrlActivity;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.HtmlImage;
import com.shengliedu.teacher.teacher.util.ImageLoader;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class AllStudentHomeworkActivity extends BaseActivity {

	private ListView listView;
	private PAllHomeworkStudentAdapter pAllHomeworkStudentAdapter;
	private List<BeanHomework2> data = new ArrayList<BeanHomework2>();
	private int activityId;
	private int part;
	private int hour;
	private String classroom_name;
	private BeanHomework2 question;
	private List<Normal> studentArr = new ArrayList<Normal>();

	private LinearLayout all_student_homework_title_lin,
			all_student_homework_item_lin, all_student_homework_two_title_lin;
	private TextView all_student_homework_title,
			all_student_homework_two_title, all_student_homework_item_text,
			all_student_homework_right_answer,
			all_student_homework_answer_analysis, select_pdf;
	private ImageView homework_last, homework_next;
	private int location = 0;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		activityId = getIntent().getIntExtra("activityId", -1);
		part = getIntent().getIntExtra("part", -1);
		hour = getIntent().getIntExtra("hour", -1);
		classroom_name = getIntent().getStringExtra("classroom_name");
		setBack();
		showTitle("查看作业");
		select_pdf = getView(R.id.select_pdf);
		all_student_homework_title_lin = getView(R.id.all_student_homework_title_lin);
		all_student_homework_two_title_lin = getView(R.id.all_student_homework_two_title_lin);
		all_student_homework_item_lin = getView(R.id.all_student_homework_item_lin);
		all_student_homework_title = getView(R.id.all_student_homework_title);
		all_student_homework_two_title = getView(R.id.all_student_homework_two_title);
		all_student_homework_item_text = getView(R.id.all_student_homework_item_text);
		all_student_homework_right_answer = getView(R.id.all_student_homework_right_answer);
		all_student_homework_answer_analysis = getView(R.id.all_student_homework_answer_analysis);
		homework_last = getView(R.id.homework_last);
		homework_next = getView(R.id.homework_next);
		listView = getView(R.id.listview);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		requestData();
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<BeanHomework2> data1 = JSON.parseArray(
						(String) msg.obj, BeanHomework2.class);
				if (data1 != null && data1.size()>0) {
					data.clear();
					data.addAll(data1);
					if (data.size() > 0) {
						question = data.get(location);
						dealQuestion();
					}
					homework_last
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									if (location - 1 >= 0) {
										location--;
										question = data.get(location);
										dealQuestion();
									} else {
										toast("已经是第一道题了");
									}
								}
							});
					homework_next
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									if (location + 1 <= data.size() - 1) {
										location++;
										question = data.get(location);
										dealQuestion();
									} else {
										toast("已经是最后一道题了");
									}
								}
							});
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
			}else if (msg.what==4){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activity", activityId);
		map.put("hour", hour);
		map.put("part", part);
		map.put("withStudentAnswer", 1);
		map.put("content_type", 2);
		doGet(Config1.getInstance().IP+"coursewareContents?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response, String json){
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
		return R.layout.activity_all_student_homework;
	}

	private void dealQuestion() {
		// TODO Auto-generated method stub
		if (question.questionInfo != null) {
			all_student_homework_title_lin.setVisibility(View.GONE);
			all_student_homework_two_title_lin.setVisibility(View.GONE);
			all_student_homework_item_lin.setVisibility(View.GONE);
			all_student_homework_item_text.setVisibility(View.GONE);
			all_student_homework_right_answer.setVisibility(View.GONE);
			all_student_homework_answer_analysis.setVisibility(View.GONE);
			final JSONObject object=JSON.parseObject(question.questionInfo.question);
			if (question.content_type == 2) {
				if (TextUtils.isEmpty(object.getString("main"))) {// 不是一题多问
					all_student_homework_title_lin.setVisibility(View.VISIBLE);
					all_student_homework_title
							.setText((location + 1)
									+ "."
									+ Html.fromHtml(HtmlImage
											.deleteSrc(object.getString("question"))));
					// select_title.setMovementMethod(LinkMovementMethod.getInstance());
					String name = object.getString("question");
					List<String> imgList = HtmlImage.getImgSrc(name);
					ImageLoader loader= ImageLoader.getInstance();
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									AllStudentHomeworkActivity.this);
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView
									.setLayoutParams(new LayoutParams(
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							loader.loadImage(url,imageView);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated
									// method stub
									HtmlImage htmlImage = new HtmlImage();
									htmlImage.showDialog(
											AllStudentHomeworkActivity.this,
											url);
								}
							});
							all_student_homework_title_lin.addView(imageView);
						}
					}
				} else {// 一题多问
					all_student_homework_title_lin.setVisibility(View.VISIBLE);
					all_student_homework_title
							.setText((location + 1)
									+ "."
									+ Html.fromHtml(HtmlImage
											.deleteSrc(object.getString("main"))));
					// select_title.setMovementMethod(LinkMovementMethod.getInstance());
					String name = object.getString("main");
					List<String> imgList = HtmlImage.getImgSrc(name);
					ImageLoader loader=ImageLoader.getInstance();
					if (imgList.size() > 0) {
						for (int j = 0; j < imgList.size(); j++) {
							final String url = imgList.get(j);
							ImageView imageView = new ImageView(
									AllStudentHomeworkActivity.this);
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView
									.setLayoutParams(new LayoutParams(
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							loader.loadImage(url,imageView);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated
									// method stub
									HtmlImage htmlImage = new HtmlImage();
									htmlImage.showDialog(
											AllStudentHomeworkActivity.this,
											url);
								}
							});
							all_student_homework_title_lin.addView(imageView);
						}
					}
					all_student_homework_two_title_lin
							.setVisibility(View.VISIBLE);
					all_student_homework_two_title
							.setText(question.questionInfo.order
									+ ")  "
									+ Html.fromHtml(HtmlImage
											.deleteSrc(object.getString("question"))));
					// select_title.setMovementMethod(LinkMovementMethod.getInstance());
					String name1 = object.getString("question");
					List<String> imgList1 = HtmlImage.getImgSrc(name1);

					if (imgList1.size() > 0) {
						for (int j = 0; j < imgList1.size(); j++) {
							final String url = imgList1.get(j);
							ImageView imageView = new ImageView(
									AllStudentHomeworkActivity.this);
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView
									.setLayoutParams(new LayoutParams(
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
											android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
							loader.loadImage(url,imageView);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated
									// method stub
									HtmlImage htmlImage = new HtmlImage();
									htmlImage.showDialog(
											AllStudentHomeworkActivity.this,
											url);
								}
							});
							all_student_homework_two_title_lin
									.addView(imageView);
						}
					}
				}
				JSONObject answer=JSON.parseObject(question.questionInfo.answer);
				if (answer != null) {
					if (TextUtils.isEmpty(answer.getString("answers"))) {
						all_student_homework_item_text
								.setVisibility(View.VISIBLE);
						all_student_homework_item_lin
								.setVisibility(View.VISIBLE);
						String item = "";
						JSONArray jsonArray = JSON.parseArray(answer.getString("answers"));
						if (jsonArray!=null && jsonArray.size()>0) {
						 for (int i = 0; i < jsonArray
								.size(); i++) {

							 final JSONObject answers = jsonArray.getJSONArray(i)
									 .getJSONObject(0);
							 if (answers!=null){
							 item += answers.getString("title") + "." + answers.getString("content")
									 + "     ";
							 if (!TextUtils.isEmpty(answers.getString("image"))) {
								 all_student_homework_item_text
										 .setVisibility(View.GONE);
								 TextView textView = new TextView(
										 AllStudentHomeworkActivity.this);
								 textView.setText(answers.getString("title") + "."
										 + answers.getString("content"));
								 ImageView imageView = new ImageView(
										 AllStudentHomeworkActivity.this);
								 imageView
										 .setScaleType(ScaleType.FIT_CENTER);
								 imageView
										 .setLayoutParams(new LayoutParams(
												 android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
												 android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
								 ImageLoader imageLoader = ImageLoader
										 .getInstance();
								 imageLoader.loadImage(Config1.IP
										 + answers.getString("image"), imageView);
								 imageView
										 .setOnClickListener(new OnClickListener() {

											 @Override
											 public void onClick(View arg0) {
												 // TODO Auto-generated
												 // method stub
												 HtmlImage htmlImage = new HtmlImage();
												 htmlImage
														 .showDialog(
																 AllStudentHomeworkActivity.this,
																 Config1.IP
																		 + answers.getString("image"));
											 }
										 });
								 all_student_homework_item_lin
										 .addView(imageView);
							 } else {
								 all_student_homework_item_lin
										 .setVisibility(View.GONE);
							 }
							 item += "\n";
						 }
						}
					}
						all_student_homework_item_text.setText(item);
					}
				} else {

				}
				if (question.questionInfo.correct != null) {
					all_student_homework_right_answer
							.setVisibility(View.VISIBLE);
					String correct = "";
					JSONArray jsonArray=JSON.parseArray(question.questionInfo.correct);
					if (jsonArray!=null && jsonArray.size()>0){
						correct = jsonArray.getJSONObject(0).getString("title")
								+ "  ";
					}

					all_student_homework_right_answer.setText("正确答案:"
							+ Html.fromHtml(correct));
				}
				if (question.questionInfo.explanation != null) {
					all_student_homework_answer_analysis
							.setVisibility(View.VISIBLE);
					all_student_homework_answer_analysis.setText("答案解析:"
							+ Html.fromHtml(question.questionInfo.explanation));
				}
				studentArr.clear();
				if (question.studentArr!=null && question.studentArr.size()>0){
					studentArr.addAll(question.studentArr);
				}else{

				}
				pAllHomeworkStudentAdapter = new PAllHomeworkStudentAdapter(
						this, studentArr, 2);
				listView.setAdapter(pAllHomeworkStudentAdapter);
			} else if (question.content_type == 3) {
				all_student_homework_title_lin.setVisibility(View.VISIBLE);
				all_student_homework_title.setText(Html
						.fromHtml(object.getString("question")));
				studentArr.clear();
				studentArr.addAll(question.studentArr);
				pAllHomeworkStudentAdapter = new PAllHomeworkStudentAdapter(
						this, studentArr, 3);
				listView.setAdapter(pAllHomeworkStudentAdapter);
			}
			if (!TextUtils.isEmpty(object.getString("contentLink")) && object.getString("contentLink").toLowerCase().endsWith(".pdf")) {
				select_pdf.setVisibility(View.VISIBLE);
				select_pdf.setText("查看PDF题目");
				select_pdf.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(AllStudentHomeworkActivity.this,ShowPdfFromUrlActivity.class);
						intent.putExtra("url", Config1.IP + object.getString("contentLink"));
						startActivity(intent);
					}
				});
			} else {
				select_pdf.setVisibility(View.GONE);
			}
		}
	}
}
