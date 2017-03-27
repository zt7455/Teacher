package com.shengliedu.teacher.teacher.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.StudentHomeworkAdapter;
import com.shengliedu.teacher.teacher.bean.courseware_content;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.bean.PersonBean;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;
import okhttp3.Response;

public class OneStudentHomeworkActivity extends BaseActivity {

	private ListView listView;
	private StudentHomeworkAdapter studentHomeworkAdapter;
	private List<Normal> data = new ArrayList<Normal>();
	public static int activityId;
	public static int part;
	public static int hour;
	private int userId;
	private String realname;
	private String checkNote;
	private EditText et_py;
	
	private int userIdLocation;
	private List<PersonBean> persons;
	private ImageView p_ok,p_last,p_next;
	public int result;
	private List<courseware_content> homeworks=new ArrayList<>();
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				toast("评价成功");
			}else if (msg.what==2){

			}else if (msg.what==3){
				homeworks.clear();
				List<courseware_content> homeworksTemp= JSON.parseArray((String) msg.obj,courseware_content.class);
				if (homeworksTemp!=null && homeworksTemp.size()>0){
					for (courseware_content homework:homeworksTemp){
						if (homework.content_type==1&& homework.submitType==5) {

						}else {
							homeworks.add(homework);
						}
					}
					String a="";
					for (int i=0;i<homeworksTemp.size();i++){
						if (i==homeworksTemp.size()-1){
							a+=homeworksTemp.get(i).id;
						}else {
							a+=homeworksTemp.get(i).id+",";
						}
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("content", a);
					map.put("student", userId);
					map.put("type", "8,9");
					doGet(Config1.getInstance().IP+"activityStudent?", map, new ResultCallback() {
						@Override
						public void onResponse(Call call, Response response, String json) {
							Message message=Message.obtain();
							message.what=5;
							message.obj=json;
							handlerReq.sendMessage(message);
						}

						@Override
						public void onFailure(Call call, IOException exception) {
							handlerReq.sendEmptyMessage(6);
						}
					});
				}
//				List<Normal> listarray = JSON.parseArray(object.getString("data"), Normal.class);
//				data.clear();
//				if (listarray!=null&&listarray.size()>0) {
//					for (int i = 0; i < listarray.size(); i++) {
//						if (listarray.get(i).content_type==1&&listarray.get(i).submitType==5) {
//						}else {
//							data.add(listarray.get(i));
//						}
//					}
//				}
//				studentHomeworkAdapter = new StudentHomeworkAdapter(OneStudentHomeworkActivity.this, data,userId);
//				listView.setAdapter(studentHomeworkAdapter);
//				studentHomeworkAdapter.notifyDataSetChanged();
			}else if (msg.what==4){

			}else if (msg.what==5){
				List<courseware_content> homeworksTemp= JSON.parseArray((String) msg.obj,courseware_content.class);
				if (homeworksTemp!=null&&homeworksTemp.size()>0){
					for (int i=0;i<homeworksTemp.size();i++){
						for (int j=0;j<homeworks.size();j++){
							if (homeworksTemp.get(i).content==homeworks.get(j).id){
								homeworks.get(j).content=homeworksTemp.get(i).id;
								homeworks.get(j).answer=homeworksTemp.get(i).answer;
								homeworks.get(j).evaluate=homeworksTemp.get(i).evaluate;
							}
						}
					}
				}
				studentHomeworkAdapter.notifyDataSetChanged();
			}else if (msg.what==6){
				studentHomeworkAdapter.notifyDataSetChanged();
			}
		}
	};
	@Override
	public void initViews() {

		// TODO Auto-generated method stub
		activityId = getIntent().getIntExtra("activityId", -1);
		part = getIntent().getIntExtra("part", -1);
		hour = getIntent().getIntExtra("hour", -1);
		userIdLocation = getIntent().getIntExtra("userIdLocation", -1);
		realname = getIntent().getStringExtra("realname");
		checkNote = getIntent().getStringExtra("checkNote");
		persons=(List<PersonBean>) getIntent().getSerializableExtra("list");
		if (persons!=null) {
			userId=persons.get(userIdLocation).id;
			showTitle(realname + "作业");
		}
		setBack();
		p_ok = getView(R.id.p_ok);
		p_last = getView(R.id.p_last);
		p_next = getView(R.id.p_next);
		et_py = getView(R.id.et_py);
		if (!TextUtils.isEmpty(checkNote)) {
			et_py.setText(""+checkNote);
		}
		listView = getView(R.id.listview);
		studentHomeworkAdapter = new StudentHomeworkAdapter(this, homeworks,userId);
		listView.setAdapter(studentHomeworkAdapter);
		p_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(et_py.getText().toString())) {
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("activityId",activityId+"");
					map.put("userId",userId+"");
					map.put("note",et_py.getText().toString());
					doPost(Config1.getInstance().ZT_PJ(), map,
							new ResultCallback() {
								@Override
								public void onFailure(Call call, IOException e) {
									handlerReq.sendEmptyMessage(2);
								}

								@Override
								public void onResponse(Call call, Response response,String json) {
									Message message=Message.obtain();
									message.what=1;
									message.obj=json;
									handlerReq.sendMessage(message);

								}
							});
				}else {
					toast("请添加评语");
				}
			}
		});
		p_last.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (persons!=null) {
					if ((userIdLocation-1)>=0) {
						userIdLocation--;
						userId=persons.get(userIdLocation).id;
						realname=persons.get(userIdLocation).realname;
						showTitle(realname + "作业");
						requestData();
					}else {
						toast("已经是第一位同学的作业了");
					}
				}
			}
		});
		p_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (persons!=null) {
					if ((userIdLocation+1)<persons.size()) {
						userIdLocation++;
						userId=persons.get(userIdLocation).id;
						realname=persons.get(userIdLocation).realname;
						showTitle(realname + "作业");
						requestData();
					}else {
						toast("已经是最后一位同学的作业了");
					}
				}
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		requestData();
	}

	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hour", hour);
		map.put("part", part);
		doGet(Config1.getInstance().IP+"coursewareContents?", map,
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		studentHomeworkAdapter.destroy();
		super.onDestroy();
	}
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_one_student_homework_student;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			setResult(result);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (JCVideoPlayer.backPress()) {
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JCVideoPlayer.releaseAllVideos();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
