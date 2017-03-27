package com.shengliedu.teacher.teacher.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.HomeworkFinishAdapter;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkFinishActivity extends BaseActivity {
	private ListView homework_finish_list;
	private HomeworkFinishAdapter homeworkFinishAdapter;
	private List<Normal> list = new ArrayList<Normal>();
	private int activityId;
	private int hour;
	private int part;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		activityId=getIntent().getIntExtra("activityId", -1);
		part=getIntent().getIntExtra("part", -1);
		hour=getIntent().getIntExtra("hour", -1);
		setBack();
		showTitle("作业总览");
		homework_finish_list=getView(R.id.homework_finish_list);
		homeworkFinishAdapter=new HomeworkFinishAdapter(this, list);
		homework_finish_list.setAdapter(homeworkFinishAdapter);
		requestData();
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homework_finish;
	}


	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<Normal> list1 = JSON.parseArray((String) msg.obj,
						Normal.class);
				list.clear();
				if (list1 != null && list1.size()>0) {
					list.addAll(list1);
				}
				homeworkFinishAdapter.notifyDataSetChanged();
			}else if (msg.what==2){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activity", activityId);
		map.put("part", part);
		map.put("hour", hour);
		map.put("withAnswerStatistic", 1);
		doGet(Config1.getInstance().IP+"coursewareContents?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json){
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}
}
