package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.StudentBehaviorAdapter;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.PersonBean;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.PinyinComparator;
import com.shengliedu.teacher.teacher.util.PinyinUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SideBar;
import com.shengliedu.teacher.teacher.util.SideBar.OnTouchingLetterChangedListener;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PHomeworkStudentListActivity extends BaseActivity {

	private ListView listView;
	private StudentBehaviorAdapter studentBehaviorAdapter;
	private List<IdName> students;
	private List<PersonBean> data = new ArrayList<PersonBean>();
	private List<PersonBean> sortcount = new ArrayList<PersonBean>();
	private SideBar sidebar;
	private TextView dialog;
	private int activityId;
	private int classroom;
	private int part;
	private int hour;
	private String classroom_name;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		activityId = getIntent().getIntExtra("activityId", -1);
		part = getIntent().getIntExtra("part", -1);
		hour = getIntent().getIntExtra("hour", -1);
		classroom = getIntent().getIntExtra("classroom", -1);
		classroom_name = getIntent().getStringExtra("classroom_name");
		setBack();
		showTitle("批改作业");
		sidebar = getView(R.id.sidebar);
		listView = getView(R.id.listview);
		dialog = getView(R.id.dialog);
		sidebar.setTextView(dialog);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						PHomeworkStudentListActivity.this,
						OneStudentHomeworkActivity.class);
				intent.putExtra("userIdLocation", arg2);
				intent.putExtra("list", (Serializable)data);
				intent.putExtra("activityId", activityId);
				intent.putExtra("part", part);
				intent.putExtra("hour", hour);
				intent.putExtra("realname", data.get(arg2).realname);
//				intent.putExtra("checkNote", data.get(arg2).checkNote);
				startActivityForResult(intent, 20);
			}
		});
		sidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// TODO Auto-generated method stub
				int position = studentBehaviorAdapter.getPositionForSelection(s.charAt(0));

				if (position != -1) {
					listView.setSelection(position);
				}
			}
		});
		Collections.sort(data, new PinyinComparator());
		studentBehaviorAdapter = new StudentBehaviorAdapter(this, data);
		listView.setAdapter(studentBehaviorAdapter);
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		Log.v("TAG", "onActivityResult");
		if (arg0==20 && arg1!=0) {
			getDatas();
		}
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
				JSONObject object=JSON.parseObject((String) msg.obj);
				List<PersonBean> listarray = JSON.parseArray(
						object.getString("data"), PersonBean.class);
				if (!MyApplication.listIsEmpty(listarray)) {
					data.clear();
					sortcount.clear();
					sortcount.addAll(listarray);
					Collections.sort(sortcount,comparator);
					setRightText("提交模式", new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(PHomeworkStudentListActivity.this,PHomeworkStudentListSubmitModeActivity.class);
							intent.putExtra("list", (Serializable)sortcount);
							intent.putExtra("activityId", activityId);
							intent.putExtra("part", part);
							intent.putExtra("hour", hour);
							intent.putExtra("classroom", classroom);
							startActivity(intent);
						}
					});
					for (int i = 0; i < listarray.size(); i++) {
						String pinyin = PinyinUtils
								.getPingYin(listarray.get(i).realname);
						String Fpinyin = pinyin.substring(0, 1)
								.toUpperCase();

						PersonBean person = new PersonBean();
						person.realname = listarray.get(i).realname;
						person.behavior = listarray.get(i).behavior;
						person.behaviorNote = listarray.get(i).behaviorNote;
						person.id = listarray.get(i).id;
						person.checkFinish = listarray.get(i).checkFinish;
						person.submitCount = listarray.get(i).submitCount;
						person.checkNote = listarray.get(i).checkNote;
						person.submitTime = listarray.get(i).submitTime;
						person.pinyin = pinyin;
						if (Fpinyin.matches("[A-Z]")) {
							person.firstpinyin = Fpinyin;
						} else {
							person.firstpinyin = "#";
						}
						data.add(person);
					}
					Collections.sort(data, new PinyinComparator());
							studentBehaviorAdapter.notifyDataSetChanged();
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				JSONObject object=JSON.parseObject((String) msg.obj);
				if (object!=null){
					students=JSON.parseArray(object.getString("students"),IdName.class);
					if(students!=null&& students.size()>0){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("activity", activityId);
						map.put("type", (part==1?8:9));
						doGet(Config1.getInstance().IP+"activityStudent?", map,
								new ResultCallback() {
									@Override
									public void onFailure(Call call, IOException e) {
										handlerReq.sendEmptyMessage(6);
									}

									@Override
									public void onResponse(Call call, Response response,String json)  {
										Message message=Message.obtain();
										message.what=5;
										message.obj=json;
										handlerReq.sendMessage(message);
									}
								});
					}
				}
			}else if (msg.what==4){

			}else if (msg.what==5){
				List<IdName> studentsTemp=JSON.parseArray((String) msg.obj,IdName.class);
				if (studentsTemp!=null && studentsTemp.size()>0){
					for (int i=0;i<students.size();i++){
						for (IdName idNames:studentsTemp){
							if (idNames.student==students.get(i).id){
								students.get(i).count++;
								SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								try {
									long date=simpleDateFormat.parse(idNames.updated_at).getTime();
									students.get(i).addTime=students.get(i).addTime>date?students.get(i).addTime:date;
									students.get(i).updated_at=simpleDateFormat.format(new Date(students.get(i).addTime));
								} catch (ParseException e) {
									e.printStackTrace();
								}

							}
						}
					}
				}
				data.clear();
				sortcount.clear();
				for (int i=0;i<students.size();i++){
					String pinyin = PinyinUtils
							.getPingYin(students.get(i).realname);
					String Fpinyin = pinyin.substring(0, 1)
							.toUpperCase();

					PersonBean person = new PersonBean();
					person.realname = students.get(i).realname;
//					person.behavior = students.get(i).behavior;
//					person.behaviorNote = listarray.get(i).behaviorNote;
					person.id = students.get(i).id;
//					person.checkFinish = listarray.get(i).checkFinish;
					person.submitCount = students.get(i).count;
//					person.checkNote = listarray.get(i).checkNote;
					person.submitTime = students.get(i).updated_at;
					person.pinyin = pinyin;
					if (Fpinyin.matches("[A-Z]")) {
						person.firstpinyin = Fpinyin;
					} else {
						person.firstpinyin = "#";
					}
					data.add(person);
					sortcount.add(person);
				}
				Collections.sort(sortcount,comparator);
				setRightText("提交模式", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(PHomeworkStudentListActivity.this,PHomeworkStudentListSubmitModeActivity.class);
						intent.putExtra("list", (Serializable)sortcount);
						intent.putExtra("activityId", activityId);
						intent.putExtra("part", part);
						intent.putExtra("hour", hour);
						intent.putExtra("classroom", classroom);
						startActivity(intent);
					}
				});
				Collections.sort(data, new PinyinComparator());
				studentBehaviorAdapter.notifyDataSetChanged();
			}else if (msg.what==6){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("withStudents", 1);
		doGet(Config1.getInstance().IP+"classroom/"+classroom+"?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(4);
					}

					@Override
					public void onResponse(Call call, Response response,String json)  {
						Message message=Message.obtain();
						message.what=3;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}
	
	 private static Comparator<PersonBean> comparator = new Comparator<PersonBean>() {
         public int compare(PersonBean s1, PersonBean s2) {
                 return s2.submitCount - s1.submitCount;
         }
     };
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_phomework_student_list;
	}

}
