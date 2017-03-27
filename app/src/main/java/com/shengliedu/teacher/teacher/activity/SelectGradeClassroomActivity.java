package com.shengliedu.teacher.teacher.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.ClassroomLogSelectGradeAdapter;
import com.shengliedu.teacher.teacher.adapter.ClassroomSelectAdapter;
import com.shengliedu.teacher.teacher.adapter.MyClassAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;

public class SelectGradeClassroomActivity extends BaseActivity {
	private ListView grade_List;
	private GridView subject_Grid;

	private List<ClassRoom> classRooms;
	private List<ClassRoom> classRooms2;
	private ClassroomLogSelectGradeAdapter classroomLogSelectGradeAdapter;
	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

	private ClassroomSelectAdapter classroomSelectAdapter;
	private List<ClassRoom> classrooms = new ArrayList<ClassRoom>();
	private List<ClassRoom> leadClassrooms;
	private int classroomId;
	private String rt;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		map.put(0, true);
		rt="我的班级日志";
		String type=getIntent().getStringExtra("type");
		if ("leave".equals(type)) {
			rt="我的批请假";
		}
		if (ConsoleActivity.roleBanzhuren) {
			setRightText(rt, new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					leadClassrooms=MyApplication.userInfo.leadClassrooms;
					if (!MyApplication.listIsEmpty(leadClassrooms)) {
						if (leadClassrooms.size()==1) {
							classroomId = leadClassrooms.get(0).id;
							Intent intent = new Intent();
							intent.putExtra("classroomId", classroomId);
							intent.putExtra("classroomName", leadClassrooms.get(0).name);
							setResult(201, intent);
							finish();
						}else {
							showMenu();
						}
					}
				}
			});
		}
		grade_List = getView(R.id.grade_List);
		subject_Grid = getView(R.id.subject_Grid);
		classroomSelectAdapter = new ClassroomSelectAdapter(this,
				classrooms);
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
	
	private PopupWindow popupWindow;

	/**
	 * 弹出顶部menu
	 */

	private void showMenu() {
		// TODO Auto-generated method stub
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.grade_popmenu, null, false);
		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果
		// popupWindow.setAnimationStyle(R.style.Animatio	nFade);
		// 这里是位置显示方式,在屏幕的左侧
		// popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		popupWindow.showAsDropDown(findViewById(R.id.ctitle), screenwidth, 0);
		ListView list=(ListView) popupWindow_view.findViewById(R.id.my_list);
		MyClassAdapter adapter=new MyClassAdapter(SelectGradeClassroomActivity.this, leadClassrooms);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				classroomId=leadClassrooms.get(arg2).id;
				Intent intent = new Intent();
				intent.putExtra("classroomId", classroomId);
				intent.putExtra("classroomName", leadClassrooms.get(arg2).name);
				setResult(201, intent);
				finish();
			}
		});
	}
	
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (ConsoleActivity.roleJiaowu) {
			classRooms = MyApplication.classRooms;
			System.out.println("："+(!MyApplication.listIsEmpty(classRooms)));
			if (!MyApplication.listIsEmpty(classRooms)) {
				for (ClassRoom s:classRooms) {
					System.out.println("："+s.toString());
				}
				List<ClassRoom> subjects1 = classRooms
						.get(0).classrooms;
				if (subjects1!=null) {
					classrooms.clear();
					classrooms.addAll(subjects1);
					classroomSelectAdapter.notifyDataSetChanged();
				}
				classroomLogSelectGradeAdapter = new ClassroomLogSelectGradeAdapter(this,
						classRooms, map);
				grade_List.setAdapter(classroomLogSelectGradeAdapter);
				grade_List.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						map.clear();
						map.put(arg2, true);
						classroomLogSelectGradeAdapter.notifyDataSetChanged();
						List<ClassRoom> subjects1 = classRooms
								.get(arg2).classrooms;
						if (subjects1!=null) {
							classrooms.clear();
							classrooms.addAll(subjects1);
							classroomSelectAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		}else if (ConsoleActivity.roleNianjizu){
			List<IdName> grades=MyApplication.userInfo.leadGrades;
			if (!MyApplication.listIsEmpty(grades)) {
				for (int i = 0; i < grades.size(); i++) {
					classRooms = MyApplication.classRooms;
					if (!MyApplication.listIsEmpty(classRooms2)) {
						classRooms2=new ArrayList<ClassRoom>();
						for (int j = 0; j < classRooms2.size(); j++) {
							if (grades.get(i).id==classRooms2.get(j).id) {
								classRooms2.add(classRooms.get(i));
							}
						}
						if (!MyApplication.listIsEmpty(classRooms2)) {
							List<ClassRoom> subjects1 = classRooms2
									.get(0).classrooms;
							if (subjects1!=null) {
								classrooms.clear();
								classrooms.addAll(subjects1);
								classroomSelectAdapter.notifyDataSetChanged();
							}
							map.put(0, true);
							classroomLogSelectGradeAdapter = new ClassroomLogSelectGradeAdapter(this,
									classRooms2, map);
							grade_List.setAdapter(classroomLogSelectGradeAdapter);
							grade_List.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0, View arg1,
										int arg2, long arg3) {
									// TODO Auto-generated method stub
									map.clear();
									map.put(arg2, true);
									classroomSelectAdapter.notifyDataSetChanged();
									List<ClassRoom> subjects1 = classRooms2
											.get(arg2).classrooms;
									if (subjects1!=null) {
										classrooms.clear();
										classrooms.addAll(subjects1);
										classroomLogSelectGradeAdapter.notifyDataSetChanged();
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
		return R.layout.activity_select_grade_classroom;
	}

}
