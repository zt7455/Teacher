package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachTaskAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoomType;
import com.shengliedu.teacher.teacher.bean.Grade_Subject_classroomType;
import com.shengliedu.teacher.teacher.bean.TeachTask;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class TeachTaskActivity extends BaseActivity implements OnClickListener {
	private TextView title1;
	private ImageView search_icon;
	private LinearLayout nianjikemu;
	private RadioGroup rg;
	private HorizontalScrollView head;
	public static List<Grade_Subject_classroomType> grade_Subject_classroomTypes;

	public static int selectGrade;
	public static int selectSubject;
	public static int selectClassRoomType;
	private List<ClassRoomType> classRoomTypes;

	private ListView teachtask_list;
	private TeachTaskAdapter taskAdapter;
	private List<TeachTask> teachtasklist = new ArrayList<TeachTask>();

	public static int type;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("教学任务");
		title1 = getView(R.id.title1);
		search_icon = getView(R.id.search_icon);
		nianjikemu = getView(R.id.nianjikemu);
		head = getView(R.id.head);
		rg = getView(R.id.bx_rg);
		teachtask_list = getView(R.id.teachtask_list);
		taskAdapter = new TeachTaskAdapter(this, teachtasklist);
		teachtask_list.setAdapter(taskAdapter);
		title1.setOnClickListener(this);
		search_icon.setOnClickListener(this);
		if (!ConsoleActivity.roleJiaowu && !ConsoleActivity.roleNianjizu) {
			// 仅仅只有教师的情况
			nianjikemu.setVisibility(View.GONE);
			head.setVisibility(View.GONE);
		} else {
			nianjikemu.setVisibility(View.VISIBLE);
			head.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Log.v("TAG", "a="+(head.getVisibility()==View.VISIBLE));
		if (head.getVisibility()==View.VISIBLE) {
			grade_Subject_classroomTypes = MyApplication.grade_Subject_classroomTypes;
			// 遍历取第一条作为默认数据，如果没有第一条，继续找下一条
			if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes)) {
				one: for (int i = 0; i < grade_Subject_classroomTypes.size(); i++) {
					if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes
							.get(i).subject_classroomtype)) {
						for (int j = 0; j < grade_Subject_classroomTypes.get(i).subject_classroomtype
								.size(); j++) {
							if (!MyApplication
									.listIsEmpty(grade_Subject_classroomTypes
											.get(i).subject_classroomtype
											.get(j).classroomType)) {
								selectClassRoomType = 0;
								selectSubject = j;
								selectGrade = i;
								title1.setText(grade_Subject_classroomTypes
										.get(i).subject_classroomtype.get(j).name
										+ "-"
										+ grade_Subject_classroomTypes.get(i).name);
								classRoomTypes = grade_Subject_classroomTypes
										.get(i).subject_classroomtype.get(j).classroomType;
								break one;
							}
						}
					}
				}
				System.out.println(selectGrade + "," + selectSubject + ","
						+ selectClassRoomType);
			}
			if (!MyApplication.listIsEmpty(classRoomTypes)) {// 遍历完，判断默认数据是否
				for (int i = 0; i < classRoomTypes.size(); i++) {
					RadioButton button = (RadioButton) LayoutInflater
							.from(this).inflate(R.layout.radio_without_button,
									null);
					button.setText(classRoomTypes.get(i).name);
					// button.setTag(i);
					button.setId(selectSubject * 100 + i);
					// button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 20,
					// 0);
					rg.addView(button);
				}
				rg.check(selectSubject * 100 + 0);
				rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						selectClassRoomType = arg1 - selectSubject * 100;
						type = 1;
						requestData(1);
					}
				});
				type = 1;
				requestData(1);
			} else {
				head.setVisibility(View.GONE);
				nianjikemu.setVisibility(View.GONE);
				teachtask_list.setVisibility(View.GONE);
				toast("无教学分工");
			}
		} else {
			type = 2;
			requestData(2);
		}
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject object=JSON.parseObject((String) msg.obj);
				List<TeachTask> teachtasklist1 = new ArrayList<TeachTask>();
				teachtasklist1 = JSON.parseArray(object.getString("data"),
						TeachTask.class);
//				if (!MyApplication.listIsEmpty(teachtasklist1)) {
				teachtasklist.clear();
				teachtasklist.addAll(teachtasklist1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						taskAdapter.notifyDataSetChanged();
					}
				});
			}else if (msg.what==2){

			}
		}
	};
	private void requestData(int type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		if (type == 1) {// 教务年级组长
			map.put("gradeId", grade_Subject_classroomTypes.get(selectGrade).id);
			map.put("subjectId",
					grade_Subject_classroomTypes.get(selectGrade).subject_classroomtype
							.get(selectSubject).id);
			map.put("classroomTypeId",
					grade_Subject_classroomTypes.get(selectGrade).subject_classroomtype
							.get(selectSubject).classroomType
							.get(selectClassRoomType).id);
			map.put("userId", MyApplication.userInfo.id);
		} else if (type == 2) {// 教师
			map.put("userId", MyApplication.userInfo.id);
		}
		doGet(Config1.getInstance().TEACH_TASK(), map, new ResultCallback() {
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
//				}
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_teachtask;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title1:
		case R.id.search_icon:
			// 教务教研
				Intent intent = new Intent(TeachTaskActivity.this,
						SelectGradeSubjectActivity.class);
				if (ConsoleActivity.roleJiaowu) {// 有教务，走教务
					intent.putExtra("selectGrade", selectGrade);
				} else {// 没有教务，走年纪组长
				}
				startActivityForResult(intent, 100);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 100 && arg1 == 200) {
			title1.setVisibility(View.VISIBLE);
			head.setVisibility(View.VISIBLE);
			List<ClassRoomType> classRoomTypes1 = grade_Subject_classroomTypes
					.get(selectGrade).subject_classroomtype.get(arg2
					.getIntExtra("selectSubject", -1)).classroomType;
			if (!MyApplication.listIsEmpty(classRoomTypes1)) {
				if (type == 2
						|| (type == 1 && selectSubject != arg2.getIntExtra(
								"selectSubject", -1))) {
					Log.v("TAG", "1selectSubject=" + selectSubject + ",arg2="
							+ arg2.getIntExtra("selectSubject", -1));
					selectSubject = arg2.getIntExtra("selectSubject",
							selectSubject);
					title1.setText(grade_Subject_classroomTypes
							.get(selectGrade).subject_classroomtype
							.get(selectSubject).name
							+ "-"
							+ grade_Subject_classroomTypes.get(selectGrade).name);
					rg.removeAllViews();
					teachtasklist.clear();
					taskAdapter.notifyDataSetChanged();
					Log.v("TAG", "move over");
					for (int i = 0; i < classRoomTypes.size(); i++) {
						RadioButton button = (RadioButton) LayoutInflater.from(
								this).inflate(R.layout.radio_without_button,
								null);
						button.setText(classRoomTypes.get(i).name);
						// button.setTag(i);
						button.setId(selectSubject * 100 + i);
						// button.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						// 20,
						// 0);
						rg.addView(button);
					}
					rg.check(selectSubject * 100 + 0);
					rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup arg0, int arg1) {
							// TODO Auto-generated method stub
							selectClassRoomType = arg1 - selectSubject * 100;
							requestData(1);
						}
					});
					type = 1;
					requestData(1);
				}
			} else {
				type = 1;
				rg.removeAllViewsInLayout();
				toast(grade_Subject_classroomTypes.get(selectGrade).subject_classroomtype
						.get(arg2.getIntExtra("selectSubject", -1)).name
						+ "-"
						+ grade_Subject_classroomTypes.get(selectGrade).name
						+ "无教学分工！");
			}
			Log.v("TAG", "data=" + selectSubject);
		} else if (arg0 == 100 && arg1 == 201) {
			type = 2;
			title1.setVisibility(View.GONE);
			head.setVisibility(View.GONE);
			requestData(2);
		}
	}
}
