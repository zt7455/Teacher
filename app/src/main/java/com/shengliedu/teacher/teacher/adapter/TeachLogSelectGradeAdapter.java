package com.shengliedu.teacher.teacher.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.ClassRoomType;
import com.shengliedu.teacher.teacher.bean.Grade_Subject_classroomType;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

/**
 * 
 * 
 */
public class TeachLogSelectGradeAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Grade_Subject_classroomType> list;
	private Context context;
	private Map<Integer, Boolean> map;
	public TeachLogSelectGradeAdapter(Context context, List<Grade_Subject_classroomType> list,Map<Integer, Boolean> map) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.map = map;
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
		viewHolder.teach_task_grade_item_text.setText(MyApplication.getGradeNameForId(list.get(position).id));
		if (map.get(position) != null) {
			convertView.setBackgroundResource(R.color.text_dark2);
			viewHolder.teach_task_grade_item_text.setTextColor(context.getResources().getColor(R.color.white));
		} else {
			convertView.setBackgroundResource(R.color.teachtaskbg);
			viewHolder.teach_task_grade_item_text.setTextColor(context.getResources().getColor(R.color.text_dark2));
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView teach_task_grade_item_text;
	}
}
