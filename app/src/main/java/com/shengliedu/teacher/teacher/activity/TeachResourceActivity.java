package com.shengliedu.teacher.teacher.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachResourceSelectGradeAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.fragment.TeachBookFragment;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class TeachResourceActivity extends BaseActivity {
	private ListView resource_left_List;
	public static List<ClassRoom> grade_Subject_editionVersion = new ArrayList<ClassRoom>();
	public static List<ClassRoom> Subject_editionVersion = new ArrayList<ClassRoom>();
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private TeachResourceSelectGradeAdapter adapter;
	private int gradeId, subjectId, editionVersion;
	private AlertDialog dialog;
	private DrawerLayout drawerLayout;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("教学资源");
		resource_left_List = getView(R.id.resource_left_List);
		drawerLayout = getView(R.id.drawerLayout);
		drawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerOpened(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		adapter = new TeachResourceSelectGradeAdapter(this,
				Subject_editionVersion, map);
		resource_left_List.setAdapter(adapter);
		resource_left_List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (!MyApplication.listIsEmpty(Subject_editionVersion)) {
					map.clear();
					map.put(arg2, true);
					adapter.notifyDataSetChanged();
					subjectId = Subject_editionVersion.get(arg2).id;
					editionVersion = Subject_editionVersion.get(arg2).editionVersion;
					drawerLayout.closeDrawer(Gravity.LEFT);
					switchFragment();
				}
			}
		});
	}
	private int which;
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				grade_Subject_editionVersion= JSON.parseArray((String) msg.obj,ClassRoom.class);
				if (grade_Subject_editionVersion!=null){
					if (!MyApplication.listIsEmpty(grade_Subject_editionVersion)) {
						int location=0;
						gradeId = grade_Subject_editionVersion.get(0).id;
						int subject = 0;
						if (ConsoleActivity.roleJiaoshi && !MyApplication.listIsEmpty(MyApplication.teacherAssigns)) {
							for (int i = 0; i < MyApplication.teacherAssigns.size(); i++) {
								if (MyApplication.userInfo.id==MyApplication.teacherAssigns.get(i).user_id) {
									gradeId=MyApplication.teacherAssigns.get(i).grade_id;
									subject=MyApplication.teacherAssigns.get(i).subject_id;
								}
							}
						}
						for (int i = 0; i < grade_Subject_editionVersion
								.size(); i++) {
							if (gradeId==grade_Subject_editionVersion
									.get(i).id) {
								location = i;
							}
						}
						setRightText(MyApplication.getGradeNameForId(gradeId),
								new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										Builder builder3 = new Builder(
												TeachResourceActivity.this);
										builder3.setTitle("班级");
										// 给对话框设定单选列表项
										int size = grade_Subject_editionVersion.size();
										String[] grades = new String[size];
										for (int i = 0; i < grade_Subject_editionVersion
												.size(); i++) {
											if (gradeId==grade_Subject_editionVersion
													.get(i).id) {
												which = i;
											}
											grades[i] = MyApplication
													.getGradeNameForId(grade_Subject_editionVersion
															.get(i).id);
										}
										builder3.setSingleChoiceItems(grades, which,
												new AlertDialog.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														TeachResourceActivity.this.which=which;
														gradeId = grade_Subject_editionVersion
																.get(which).id;
														setRightText(MyApplication
																.getGradeNameForId(gradeId));
														Subject_editionVersion.clear();
														map.clear();
														if (!MyApplication
																.listIsEmpty(grade_Subject_editionVersion
																		.get(which).subjects)) {
															Subject_editionVersion
																	.addAll(grade_Subject_editionVersion
																			.get(which).subjects);
															subjectId = Subject_editionVersion
																	.get(0).id;
															editionVersion = Subject_editionVersion
																	.get(0).editionVersion;
															map.put(0, true);
															adapter.notifyDataSetChanged();
														} else {
															adapter.notifyDataSetChanged();
														}
														switchFragment();
														dialog.dismiss();
													}
												});
										dialog = builder3.show();
										dialog.show();
									}
								});
						Subject_editionVersion.clear();
						Subject_editionVersion
								.addAll(grade_Subject_editionVersion.get(location).subjects);
						if (!MyApplication.listIsEmpty(Subject_editionVersion)) {
							int subloca=0;
							for (int i = 0; i < Subject_editionVersion
									.size(); i++){
								if (subject!=0 && subject==Subject_editionVersion.get(i).id){
									subloca=i;
								}
							}
							subjectId = Subject_editionVersion.get(subloca).id;
							editionVersion = Subject_editionVersion.get(subloca).editionVersion;
							map.clear();
							map.put(subloca, true);
							adapter.notifyDataSetChanged();
						}
					}
					switchFragment();
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		// editionVersionId=82&gradeId=1&limit=30&offset=0&subjectId=3&typeId=1
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("setUpGradeSubject", 1);
		map.put("schoolstage", MyApplication.beanSchoolInfo.schoolstage);
		map.put("region", MyApplication.beanSchoolInfo.region);
		doGet(Config1.getInstance().MAIN_BOOK_IP+"/textbook?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response, String json)  {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}

	private void switchFragment() {
		// TODO Auto-generated method stub
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.resource_fragment_content,
						new TeachBookFragment(this, gradeId, subjectId,
								editionVersion)).commit();
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_teach_resource;
	}

}
