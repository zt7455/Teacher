package com.shengliedu.teacher.teacher.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.ConsoleMenu;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.Subject_classroomType;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.view.NoScrollGridView;

/**
 * 
 * 
 */
public class TeachLogRightAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Subject_classroomType> list;
	private Activity context;
	private String name;
	public TeachLogRightAdapter(Activity context, List<Subject_classroomType> list,String name) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.name = name;
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
		Log.v("TAG", "name:"+name);
		viewHolder.week_right_item_text.setText(list.get(position).name);
		final List<ClassRoom> idNames=new ArrayList<ClassRoom>();
		if (!MyApplication.listIsEmpty(list.get(position).classroomType)) {
			for (int i = 0; i < list.get(position).classroomType.size(); i++) {
				List<IdName> teachers=list.get(position).classroomType.get(i).teachers;
				if (!MyApplication.listIsEmpty(teachers)) {
					for (int j = 0; j < teachers.size(); j++) {
						ClassRoom classRoom=new ClassRoom();
						classRoom.id=list.get(position).classroomType.get(i).teachers.get(j).id;
						classRoom.name=list.get(position).classroomType.get(i).teachers.get(j).name;
						idNames.add(classRoom);
					}
				}
			}
			Set<ClassRoom> set=new HashSet<ClassRoom>(idNames);
			idNames.clear();
			idNames.addAll(set);
		}
		TeachLogTeacherAdapter adapter=new TeachLogTeacherAdapter(context, idNames);
		viewHolder.week_right_item_grid.setAdapter(adapter);
		viewHolder.week_right_item_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("userName",list.get(position).name+"-"+name+"\n"+idNames.get(arg2).name);
				intent.putExtra("userId",idNames.get(arg2).id);
				context.setResult(401, intent);
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
