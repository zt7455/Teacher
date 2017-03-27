package com.shengliedu.teacher.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

/**
 * 
 * 
 */
public class TeachLogTeacherAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ClassRoom> list;

	public TeachLogTeacherAdapter(Context context, List<ClassRoom> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
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

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater
					.inflate(R.layout.teach_task_grade_item, null);
			viewHolder.teach_task_grade_item_text = (TextView) convertView
					.findViewById(R.id.teach_task_grade_item_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.teach_task_grade_item_text.setText(MyApplication.getTeacherNameForId(list.get(position).id));
		return convertView;
	}

	static class ViewHolder {
		public TextView teach_task_grade_item_text;
	}
}
