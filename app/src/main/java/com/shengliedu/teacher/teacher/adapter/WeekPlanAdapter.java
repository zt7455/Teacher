package com.shengliedu.teacher.teacher.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ConsoleMenu;
import com.shengliedu.teacher.teacher.bean.WeekPlanList;

/**
 * 
 * 
 */
public class WeekPlanAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<WeekPlanList> list;

	public WeekPlanAdapter(Context context, List<WeekPlanList> list) {
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
			convertView = layoutInflater.inflate(R.layout.week_plan_item,
					null);
			viewHolder.week_plan_item_title = (TextView) convertView
					.findViewById(R.id.week_plan_item_title);
			viewHolder.week_plan_item_time = (TextView) convertView
					.findViewById(R.id.week_plan_item_time);
			viewHolder.week_plan_item_content = (TextView) convertView
					.findViewById(R.id.week_plan_item_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.week_plan_item_title.setText(list.get(position).sName);
		viewHolder.week_plan_item_time.setText(list.get(position).sTime+"~"+list.get(position).eTime);
		viewHolder.week_plan_item_content.setText(list.get(position).sContent);
		return convertView;
	}

	static class ViewHolder {
		public TextView week_plan_item_title;
		public TextView week_plan_item_time;
		public TextView week_plan_item_content;
	}
}
