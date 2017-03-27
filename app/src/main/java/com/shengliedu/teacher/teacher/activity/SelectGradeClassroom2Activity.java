package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.ClassroomSelectAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class SelectGradeClassroom2Activity extends BaseActivity {
	private GridView subject_Grid;
	private ClassroomSelectAdapter classroomSelectAdapter;
	private List<ClassRoom> classrooms = new ArrayList<ClassRoom>();

	@Override
	public void initViews() {
		// getClassRoomNameForclassroomid
		// TODO Auto-generated method stub
		setBack();
		subject_Grid = getView(R.id.subject_Grid);
		classroomSelectAdapter = new ClassroomSelectAdapter(this, classrooms);
		subject_Grid.setAdapter(classroomSelectAdapter);
		subject_Grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("classroomId", classrooms.get(arg2).id);
				intent.putExtra("classroomName", classrooms.get(arg2).name);
				setResult(201, intent);
				finish();
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (ConsoleActivity.roleJiaowu) {
			showTitle("选择班级");
			classrooms.clear();
			List<ClassRoom> classroomDic=MyApplication.classroomDic;
			if (!MyApplication.listIsEmpty(classroomDic)) {
				classrooms.addAll(classroomDic);
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					classroomSelectAdapter.notifyDataSetChanged();
				}
			});
		} else if (ConsoleActivity.roleNianjizu) {
			showTitle("选择本年级班级");
			List<IdName> list=MyApplication.userInfo.leadGrades;
			List<ClassRoom> classroomDic=MyApplication.classroomDic;
			classrooms.clear();
			if (!MyApplication.listIsEmpty(list)&&!MyApplication.listIsEmpty(classroomDic)) {
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < classroomDic.size(); j++) {
						if (list.get(i).id==classroomDic.get(j).grade) {
							classrooms.add(classroomDic.get(j));
						}
					}
				}
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					classroomSelectAdapter.notifyDataSetChanged();
				}
			});
		} else if (ConsoleActivity.roleBanzhuren) {
			showTitle("选择我的班级");
			List<ClassRoom>  list=MyApplication.userInfo.leadClassrooms;
			classrooms.clear();
			if (!MyApplication.listIsEmpty(list)) {
				classrooms.addAll(list);
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					classroomSelectAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_select_grade_classroom2;
	}

}
