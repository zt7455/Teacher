package com.shengliedu.teacher.teacher.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachTaskSelectGradeAdapter;
import com.shengliedu.teacher.teacher.adapter.TeachTaskSelectSubjectAdapter;
import com.shengliedu.teacher.teacher.bean.Grade_Subject_classroomType;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.Subject_classroomType;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;

public class SelectGradeSubjectActivity extends BaseActivity {
	private ListView grade_List;
	private GridView subject_Grid;
	private int selectGrade;

	private List<Grade_Subject_classroomType> grade_Subject_classroomTypes;
	private List<Grade_Subject_classroomType> grade_Subject_classroomTypes2;
	private TeachTaskSelectGradeAdapter taskSelectGradeAdapter;
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

	private TeachTaskSelectSubjectAdapter taskSelectSubjectAdapter;
	private List<Subject_classroomType> subjects = new ArrayList<Subject_classroomType>();

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		selectGrade = getIntent().getIntExtra("selectGrade", 0);
		map.put(selectGrade, true);
		if (ConsoleActivity.roleJiaoshi) {
			setRightText("我的教学任务", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					setResult(201, intent);
					finish();
				}
			});
		}
		grade_List = getView(R.id.grade_List);
		subject_Grid = getView(R.id.subject_Grid);
		taskSelectSubjectAdapter = new TeachTaskSelectSubjectAdapter(this,
				subjects);
		subject_Grid.setAdapter(taskSelectSubjectAdapter);
		subject_Grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("selectSubject", arg2);
				setResult(200, intent);
				finish();
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (ConsoleActivity.roleJiaowu) {
			grade_Subject_classroomTypes = MyApplication.grade_Subject_classroomTypes;
			if (!MyApplication.listIsEmpty(grade_Subject_classroomTypes)) {
				List<Subject_classroomType> subjects1 = grade_Subject_classroomTypes
						.get(selectGrade).subject_classroomtype;
				if (!MyApplication.listIsEmpty(subjects1)) {
					subjects.clear();
					subjects.addAll(subjects1);
					taskSelectSubjectAdapter.notifyDataSetChanged();
				}
				taskSelectGradeAdapter = new TeachTaskSelectGradeAdapter(this,
						grade_Subject_classroomTypes, map);
				grade_List.setAdapter(taskSelectGradeAdapter);
				grade_List.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						map.clear();
						map.put(arg2, true);
						taskSelectGradeAdapter.notifyDataSetChanged();
						List<Subject_classroomType> subjects1 = grade_Subject_classroomTypes
								.get(arg2).subject_classroomtype;
						if (!MyApplication.listIsEmpty(subjects1)) {
							subjects.clear();
							subjects.addAll(subjects1);
							taskSelectSubjectAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		}else if (ConsoleActivity.roleBanzhuren){
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
								subjects.clear();
								subjects.addAll(subjects1);
								taskSelectSubjectAdapter.notifyDataSetChanged();
							}
							map.put(0, true);
							taskSelectGradeAdapter = new TeachTaskSelectGradeAdapter(this,
									grade_Subject_classroomTypes2, map);
							grade_List.setAdapter(taskSelectGradeAdapter);
							grade_List.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0, View arg1,
										int arg2, long arg3) {
									// TODO Auto-generated method stub
									map.clear();
									map.put(arg2, true);
									taskSelectGradeAdapter.notifyDataSetChanged();
									List<Subject_classroomType> subjects1 = grade_Subject_classroomTypes2
											.get(arg2).subject_classroomtype;
									if (!MyApplication.listIsEmpty(subjects1)) {
										subjects.clear();
										subjects.addAll(subjects1);
										taskSelectSubjectAdapter.notifyDataSetChanged();
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
		return R.layout.activity_select_gradesubject;
	}

}
