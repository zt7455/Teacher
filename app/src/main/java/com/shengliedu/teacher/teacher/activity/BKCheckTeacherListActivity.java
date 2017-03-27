package com.shengliedu.teacher.teacher.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.BKJCListLAdapter;
import com.shengliedu.teacher.teacher.adapter.BKJCListRAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.TeacherAssign;
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

import static com.shengliedu.teacher.teacher.R.id.right_list;

public class BKCheckTeacherListActivity extends BaseActivity {
	private ListView left_listView;
	private ExpandableListView right_listView;
	private List<ClassRoom> grade_subject_teachers = new ArrayList<ClassRoom>();
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private BKJCListLAdapter bkjcListLAdapter;
	
	private BKJCListRAdapter bkjcListRAdapter;
	private List<ClassRoom> rightlist = new ArrayList<ClassRoom>();

	private int gradeId;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		left_listView = getView(R.id.left_List);
		right_listView = getView(right_list);
		right_listView
				.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						right_listView.expandGroup(arg0, false);
					}
				});
		bkjcListLAdapter=new BKJCListLAdapter(this,grade_subject_teachers,map);
		left_listView.setAdapter(bkjcListLAdapter);
		bkjcListRAdapter=new BKJCListRAdapter(this,rightlist);
		right_listView.setAdapter(bkjcListRAdapter);
		left_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				// TODO Auto-generated method stub
				map.clear();
				map.put(arg2, true);
				gradeId=grade_subject_teachers.get(arg2).id;
				bkjcListLAdapter.notifyDataSetChanged();
				List<ClassRoom> subjects1 = grade_subject_teachers
						.get(arg2).subjects;
				rightlist.clear();
				if (subjects1!=null) {
					if (ConsoleActivity.roleJiaowu) {
						for (ClassRoom subject:subjects1){
							if (subject.teachers!=null && subject.teachers.size()>0){
								rightlist.add(subject);
							}
						}
					}else if (ConsoleActivity.roleJiaoyan){
						if (!MyApplication.listIsEmpty(MyApplication.userInfo.gradeSubjectResearchers)) {
							for (TeacherAssign teacherAssign:MyApplication.userInfo.gradeSubjectResearchers) {
								for (ClassRoom subject : subjects1) {
									if (subject.teachers != null && subject.teachers.size() > 0 && teacherAssign.subject_id==subject.id) {
										rightlist.add(subject);
									}
								}
							}
						}
					}
				}
				bkjcListRAdapter.setGradeId(gradeId);
				bkjcListRAdapter.notifyDataSetChanged();
				if (rightlist != null && rightlist.size() > 0) {
					for (int i = 0; i < rightlist.size(); i++) {
						right_listView.expandGroup(i);
					}
				}
			}
		});
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<ClassRoom> grade_subject_teachersTemp= JSON.parseArray((String) msg.obj,ClassRoom.class);
				grade_subject_teachers.clear();
				if (grade_subject_teachersTemp!=null && grade_subject_teachersTemp.size()>0){
					if (ConsoleActivity.roleJiaowu) {
						grade_subject_teachers.addAll(grade_subject_teachersTemp);
					}else if (ConsoleActivity.roleJiaoyan){
							if (!MyApplication.listIsEmpty(MyApplication.userInfo.gradeSubjectResearchers)){
//								List<ClassRoom> grade_subject_teachersTemp2=new ArrayList<>();
								for (TeacherAssign teacherAssign:MyApplication.userInfo.gradeSubjectResearchers){
									 for (ClassRoom grade_subject_teacher:grade_subject_teachersTemp){
										 if (teacherAssign.grade_id==grade_subject_teacher.id){
											 grade_subject_teachers.add(grade_subject_teacher);
										 }
									 }
								}
//								for (TeacherAssign teacherAssign:MyApplication.userInfo.gradeSubjectResearchers){
//									for (int i=0;i<grade_subject_teachersTemp2.size();i++){
//										for (int j=0;j<grade_subject_teachersTemp2.size();j++){
//												if (teacherAssign.grade_id==grade_subject_teachersTemp2.get(i).id){
//													if (!MyApplication.listIsEmpty(grade_subject_teachersTemp2.get(i).subjects)&&teacherAssign.subject_id==grade_subject_teachersTemp2.get(i).subjects.get(j).id){
//
//													}
//												}
//										}
//									}
//								}
							}
					}
					map.put(0,true);
					gradeId=grade_subject_teachers.get(0).id;
					List<ClassRoom> subjects1 = grade_subject_teachers
							.get(0).subjects;
					rightlist.clear();
					if (subjects1!=null) {
						if (ConsoleActivity.roleJiaowu) {
							for (ClassRoom subject : subjects1) {
								if (subject.teachers != null && subject.teachers.size() > 0) {
									rightlist.add(subject);
								}
							}
						}else if (ConsoleActivity.roleJiaoyan){
							if (!MyApplication.listIsEmpty(MyApplication.userInfo.gradeSubjectResearchers)) {
							for (TeacherAssign teacherAssign:MyApplication.userInfo.gradeSubjectResearchers) {
								for (ClassRoom subject : subjects1) {
									if (subject.teachers != null && subject.teachers.size() > 0 && teacherAssign.subject_id==subject.id) {
										rightlist.add(subject);
									}
								}
							}
						}
						}
					}
					bkjcListRAdapter.setGradeId(gradeId);
					bkjcListRAdapter.notifyDataSetChanged();
					if (rightlist != null && rightlist.size() > 0) {
						for (int i = 0; i < rightlist.size(); i++) {
							right_listView.expandGroup(i);
						}
					}
				}
				bkjcListLAdapter.notifyDataSetChanged();
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
//		http://192.168.1.207/user?checkTeacherTree=1&semester=40000&region=350102&schoolstage=1&school=67
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("checkTeacherTree", 1);
		map.put("semester", MyApplication.userInfo.currentSemester.id);
		map.put("region", MyApplication.beanSchoolInfo.region);
		map.put("schoolstage", MyApplication.beanSchoolInfo.schoolstage);
		map.put("school", MyApplication.beanSchoolInfo.id);
		doGet(Config1.getInstance().IP+"user?", map,
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

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_bkjc_list;
	}
}
