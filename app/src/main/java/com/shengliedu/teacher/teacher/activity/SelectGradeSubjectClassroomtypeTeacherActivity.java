package com.shengliedu.teacher.teacher.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachTaskSelectGradeAdapter;
import com.shengliedu.teacher.teacher.adapter.WeekPlanRightAdapter;
import com.shengliedu.teacher.teacher.adapter.WeekPlanSelectGradeAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.ClassRoomType;
import com.shengliedu.teacher.teacher.bean.Grade_Subject_classroomType;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.Subject_classroomType;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

public class SelectGradeSubjectClassroomtypeTeacherActivity extends BaseActivity {
	private ListView left_list;
	private ListView right_list;
	private List<Grade_Subject_classroomType> grade_Subject_classroomTypes;
	private List<Grade_Subject_classroomType> grade_Subject_classroomTypes2;
	private List<ClassRoom> leftlist = new ArrayList<ClassRoom>();

	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private WeekPlanSelectGradeAdapter weekPlanSelectGradeAdapter;
	
	private WeekPlanRightAdapter weekPlanRightAdapter;
	private List<ClassRoomType> rightlist = new ArrayList<ClassRoomType>();
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		if (ConsoleActivity.roleJiaoshi) {
			setRightText("我的周计划", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setResult(302);
					finish();
				}
			});
		}
		left_list = getView(R.id.left_List);
		right_list = getView(R.id.right_list);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (ConsoleActivity.roleJiaowu) {
			grade_Subject_classroomTypes = MyApplication.grade_Subject_classroomTypes;
			if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes)) {
				for (int i = 0; i < grade_Subject_classroomTypes.size(); i++) {
					List<Subject_classroomType> subject_classroomTypes = grade_Subject_classroomTypes
							.get(i).subject_classroomtype;
					for (int j = 0; j < subject_classroomTypes.size(); j++) {
						ClassRoom classRoom = new ClassRoom();
						classRoom.grade = grade_Subject_classroomTypes.get(i).id;
						classRoom.subject = grade_Subject_classroomTypes.get(i).subject_classroomtype
								.get(j).id;
						classRoom.classroomType = grade_Subject_classroomTypes
								.get(i).subject_classroomtype.get(j).classroomType;
						leftlist.add(classRoom);
					}
				}
				weekPlanRightAdapter=new WeekPlanRightAdapter(this, rightlist);
				right_list.setAdapter(weekPlanRightAdapter);
				if (!MyApplication.listIsEmpty(leftlist)) {
					map.put(0, true);
					weekPlanSelectGradeAdapter = new WeekPlanSelectGradeAdapter(
							this, leftlist, map);
					left_list.setAdapter(weekPlanSelectGradeAdapter);
					List<ClassRoomType> subjects1 = leftlist
							.get(0).classroomType;
					if (subjects1!=null) {
						rightlist.clear();
						rightlist.addAll(subjects1);
						weekPlanRightAdapter.notifyDataSetChanged();
					}
					left_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							map.clear();
							map.put(arg2, true);
							weekPlanSelectGradeAdapter.notifyDataSetChanged();
							List<ClassRoomType> subjects1 = leftlist
									.get(arg2).classroomType;
							if (subjects1!=null) {
								rightlist.clear();
								rightlist.addAll(subjects1);
								weekPlanRightAdapter.notifyDataSetChanged();
							}
						}
					});
				}
			}
		} else if (ConsoleActivity.roleNianjizu) {
			List<IdName> grades=MyApplication.userInfo.leadGrades;
			if (!MyApplication.listIsEmpty(grades)) {
				for (int m = 0; m < grades.size(); m++) {
					grade_Subject_classroomTypes = MyApplication.grade_Subject_classroomTypes;
					if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes)) {
						grade_Subject_classroomTypes2=new ArrayList<Grade_Subject_classroomType>();
						for (int j = 0; j < grade_Subject_classroomTypes.size(); j++) {
							if (grades.get(m).id==grade_Subject_classroomTypes.get(j).id) {
								grade_Subject_classroomTypes2.add(grade_Subject_classroomTypes.get(m));
							}
						}
						if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes2)) {
							for (int i = 0; i < grade_Subject_classroomTypes2.size(); i++) {
								List<Subject_classroomType> subject_classroomTypes = grade_Subject_classroomTypes2
										.get(i).subject_classroomtype;
								for (int j = 0; j < subject_classroomTypes.size(); j++) {
									ClassRoom classRoom = new ClassRoom();
									classRoom.grade = grade_Subject_classroomTypes2.get(i).id;
									classRoom.subject = grade_Subject_classroomTypes2.get(i).subject_classroomtype
											.get(j).id;
									classRoom.classroomType = grade_Subject_classroomTypes2
											.get(i).subject_classroomtype.get(j).classroomType;
									leftlist.add(classRoom);
								}
							}
							weekPlanRightAdapter=new WeekPlanRightAdapter(this, rightlist);
							right_list.setAdapter(weekPlanRightAdapter);
							if (!MyApplication.listIsEmpty(leftlist)) {
								map.put(0, true);
								weekPlanSelectGradeAdapter = new WeekPlanSelectGradeAdapter(
										this, leftlist, map);
								left_list.setAdapter(weekPlanSelectGradeAdapter);
								List<ClassRoomType> subjects1 = leftlist
										.get(0).classroomType;
								if (subjects1!=null) {
									rightlist.clear();
									rightlist.addAll(subjects1);
									weekPlanRightAdapter.notifyDataSetChanged();
								}
								left_list.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										map.clear();
										map.put(arg2, true);
										weekPlanSelectGradeAdapter.notifyDataSetChanged();
										List<ClassRoomType> subjects1 = leftlist
												.get(arg2).classroomType;
										if (subjects1!=null) {
											rightlist.clear();
											rightlist.addAll(subjects1);
											weekPlanRightAdapter.notifyDataSetChanged();
										}
									}
								});
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_select_gradesubject_classroomtype_teacher;
	}
}
