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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.StudentBehaviorSubmitAdapter;
import com.shengliedu.teacher.teacher.bean.PersonBean;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PHomeworkStudentListSubmitModeActivity extends BaseActivity {

	private ListView listView;
	private StudentBehaviorSubmitAdapter studentBehaviorAdapter;
	private List<PersonBean> sortcount;
	private int activityId;
	private int part;
	private int hour;
	private int classroom;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		activityId = getIntent().getIntExtra("activityId", -1);
		part = getIntent().getIntExtra("part", -1);
		hour = getIntent().getIntExtra("hour", -1);
		classroom = getIntent().getIntExtra("classroom", -1);
		sortcount=(List<PersonBean>) getIntent().getSerializableExtra("list");
		if (sortcount==null) {
			sortcount = new ArrayList<PersonBean>();
		}
		setBack();
		showTitle("批改作业");
		setRightText("检索模式", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		listView = getView(R.id.listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						PHomeworkStudentListSubmitModeActivity.this,
						OneStudentHomeworkActivity.class);
				intent.putExtra("userIdLocation", arg2);
				intent.putExtra("list", (Serializable)sortcount);
				intent.putExtra("activityId", activityId);
				intent.putExtra("part", part);
				intent.putExtra("hour", hour);
				intent.putExtra("realname", sortcount.get(arg2).realname);
//				intent.putExtra("checkNote", sortcount.get(arg2).checkNote);
				startActivityForResult(intent, 20);
			}
		});
		studentBehaviorAdapter = new StudentBehaviorSubmitAdapter(this, sortcount);
		listView.setAdapter(studentBehaviorAdapter);
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		Log.v("TAG", "onActivityResult");
		if (arg0==20 && arg1!=0) {
			requestData();
		}
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
//				JSONObject object=JSON.parseObject((String) msg.obj);
//				List<PersonBean> listarray = JSON.parseArray(
//						object.getString("data"), PersonBean.class);
//				if (!MyApplication.listIsEmpty(listarray)) {
//					sortcount.clear();
//					sortcount.clear();
//					sortcount.addAll(listarray);
//					Collections.sort(sortcount,comparator);
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							studentBehaviorAdapter.notifyDataSetChanged();
//						}
//					});
//				}
				JSONObject object= JSON.parseObject((String) msg.obj);
				List<PersonBean> listarray = JSON.parseArray(
						object.getString("data"), PersonBean.class);
				if (!MyApplication.listIsEmpty(listarray)) {
					sortcount.clear();
					sortcount.addAll(listarray);
					Collections.sort(sortcount,comparator);
					studentBehaviorAdapter.notifyDataSetChanged();
				}
			}else if (msg.what==2){

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
		return R.layout.activity_phomework_submitcount_student_list;
	}

}
