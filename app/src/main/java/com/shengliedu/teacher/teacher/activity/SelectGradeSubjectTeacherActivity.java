package com.shengliedu.teacher.teacher.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachLogRightAdapter;
import com.shengliedu.teacher.teacher.adapter.TeachLogSelectGradeAdapter;
import com.shengliedu.teacher.teacher.bean.Grade_Subject_classroomType;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.Subject_classroomType;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

public class SelectGradeSubjectTeacherActivity extends BaseActivity {
	private ListView left_list;
	private ListView right_list;
	private List<Grade_Subject_classroomType> grade_Subject_classroomTypes;
	private List<Grade_Subject_classroomType> grade_Subject_classroomTypes2;

	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private TeachLogSelectGradeAdapter teachLogSelectGradeAdapter;
	
	private TeachLogRightAdapter teachLogRightAdapter;
	private List<Subject_classroomType> subjects = new ArrayList<Subject_classroomType>();
	private static String gradename="";
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		if (ConsoleActivity.roleJiaoshi) {
			setRightText("我的教学日志", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setResult(402);
					finish();
				}
			});
		}
		left_list = getView(R.id.left_List);
		right_list = getView(R.id.right_list);
		teachLogRightAdapter=new TeachLogRightAdapter(this, subjects,gradename);
		right_list.setAdapter(teachLogRightAdapter);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (ConsoleActivity.roleJiaowu) {
			grade_Subject_classroomTypes = MyApplication.grade_Subject_classroomTypes;
			if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes)) {
				List<Subject_classroomType> subjects1 = grade_Subject_classroomTypes
						.get(0).subject_classroomtype;
				if (!MyApplication.listIsEmpty(subjects1)) {
					gradename= grade_Subject_classroomTypes
							.get(0).name;
					Log.v("TAG", "gradename:"+gradename);
					subjects.clear();
					subjects.addAll(subjects1);
					teachLogRightAdapter.notifyDataSetChanged();
				}
					map.put(0, true);
					teachLogSelectGradeAdapter = new TeachLogSelectGradeAdapter(
							this, grade_Subject_classroomTypes, map);
					left_list.setAdapter(teachLogSelectGradeAdapter);
					left_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							map.clear();
							map.put(arg2, true);
							teachLogSelectGradeAdapter.notifyDataSetChanged();
							List<Subject_classroomType> subjects1 = grade_Subject_classroomTypes
									.get(arg2).subject_classroomtype;
							gradename= grade_Subject_classroomTypes
									.get(arg2).name;
							if (subjects1!=null) {
								subjects.clear();
								subjects.addAll(subjects1);
								teachLogRightAdapter.notifyDataSetChanged();
							}
						}
					});
			}
		} else if (ConsoleActivity.roleNianjizu) {
			List<IdName> grades=MyApplication.userInfo.leadGrades;
			if (!MyApplication.listIsEmpty(grades)) {
				for (int i = 0; i < grades.size(); i++) {
					grade_Subject_classroomTypes = MyApplication.grade_Subject_classroomTypes;
					if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes)) {
						grade_Subject_classroomTypes2=new ArrayList<Grade_Subject_classroomType>();
						for (int j = 0; j < grade_Subject_classroomTypes.size(); j++) {
							if (grades.get(i).id==grade_Subject_classroomTypes.get(j).id) {
								grade_Subject_classroomTypes2.add(grade_Subject_classroomTypes.get(i));
							}
						}
						if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes2)) {
							List<Subject_classroomType> subjects1 = grade_Subject_classroomTypes2
									.get(0).subject_classroomtype;
							if (!MyApplication.listIsEmpty(subjects1)) {
								gradename= grade_Subject_classroomTypes
										.get(0).name;
								subjects.clear();
								subjects.addAll(subjects1);
								teachLogRightAdapter.notifyDataSetChanged();
							}
								map.put(0, true);
								teachLogSelectGradeAdapter = new TeachLogSelectGradeAdapter(
										this, grade_Subject_classroomTypes2, map);
								left_list.setAdapter(teachLogSelectGradeAdapter);
								left_list.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										map.clear();
										map.put(arg2, true);
										teachLogSelectGradeAdapter.notifyDataSetChanged();
										List<Subject_classroomType> subjects1 = grade_Subject_classroomTypes2
												.get(arg2).subject_classroomtype;
										gradename= grade_Subject_classroomTypes
												.get(arg2).name;
										if (subjects1!=null) {
											subjects.clear();
											subjects.addAll(subjects1);
											teachLogRightAdapter.notifyDataSetChanged();
										}
									}
								});
						}
					}
				}
			}
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_select_grade_subject_teacher;
	}
}
