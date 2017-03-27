package com.shengliedu.teacher.teacher.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.SelectGradeSubjectClassroomtypeTeacher2Activity;
import com.shengliedu.teacher.teacher.bean.ClassRoomType;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.view.NoScrollGridView;

/**
 * 
 * 
 */
public class WeekPlanRightAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ClassRoomType> list;
	private Activity context;
	public WeekPlanRightAdapter(Activity context, List<ClassRoomType> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater
					.inflate(R.layout.week_right_item, null);
			viewHolder.week_right_item_text = (TextView) convertView
					.findViewById(R.id.week_right_item_text);
			viewHolder.week_right_item_grid = (NoScrollGridView) convertView
					.findViewById(R.id.week_right_item_grid);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.week_right_item_text.setText(MyApplication.getClassroomTypeNameForId(list.get(position).id));
		WeekPlanTeacherAdapter adapter=new WeekPlanTeacherAdapter(context, list.get(position).teachers);
		viewHolder.week_right_item_grid.setAdapter(adapter);
		viewHolder.week_right_item_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("userId", list.get(position).teachers.get(arg2).id);
				intent.putExtra("gradeId", SelectGradeSubjectClassroomtypeTeacher2Activity.gradeId);
				intent.putExtra("subjectId", SelectGradeSubjectClassroomtypeTeacher2Activity.subjectId);
				intent.putExtra("classroomTypeId", list.get(position).id);
				context.setResult(301, intent);
				context.finish();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView week_right_item_text;
		public NoScrollGridView week_right_item_grid;
	}
}
