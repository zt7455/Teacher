package com.shengliedu.teacher.teacher.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.AllStudentHomeworkActivity;
import com.shengliedu.teacher.teacher.activity.HomeworkFinishActivity;
import com.shengliedu.teacher.teacher.activity.PHomeworkStudentListActivity;
import com.shengliedu.teacher.teacher.bean.CorrectHomeList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 */
public class CorrectHomeAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<CorrectHomeList> list;
	private Activity context;

	public CorrectHomeAdapter(Activity context, List<CorrectHomeList> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
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
			convertView = layoutInflater.inflate(R.layout.correct_homework_item, null);
			viewHolder.correct_homework_title = (TextView) convertView
					.findViewById(R.id.correct_homework_title);
			viewHolder.correct_homework_item_content = (TextView) convertView
					.findViewById(R.id.correct_homework_item_content);
//			viewHolder.correct_homework_item_yipigai = (TextView) convertView
//					.findViewById(R.id.correct_homework_item_yipigai);
			viewHolder.correct_homework_item_pigai = (TextView) convertView
					.findViewById(R.id.correct_homework_item_pigai);
			viewHolder.correct_homework_item_look = (TextView) convertView
					.findViewById(R.id.correct_homework_item_look);
			viewHolder.correct_homework_item_browse = (TextView) convertView
					.findViewById(R.id.correct_homework_item_browse);
			viewHolder.correct_homework_item_pigai2 = (TextView) convertView
					.findViewById(R.id.correct_homework_item_pigai2);
			viewHolder.correct_homework_item_look2 = (TextView) convertView
					.findViewById(R.id.correct_homework_item_look2);
			viewHolder.correct_homework_item_browse2 = (TextView) convertView
					.findViewById(R.id.correct_homework_item_browse2);
			viewHolder.keqian_rel = (RelativeLayout) convertView
					.findViewById(R.id.keqian_rel);
			viewHolder.kehou_rel = (RelativeLayout) convertView
					.findViewById(R.id.kehou_rel);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
		viewHolder.correct_homework_title.setText(simpleDateFormat.format(new Date(list.get(position).date*1000)));
		viewHolder.correct_homework_item_content
				.setText(list.get(position).name);
//		viewHolder.correct_homework_item_yipigai.setText(list.get(position).checkHomework==0?"未完成批改":"已完成批改");
//		hasPreHw表示是否含有课前作业,1表示有，0表示没有
//
//		hasNextHw表示是否含有课后作业,1表示有，0表示没有
		if (list.get(position).hasNextHw==1) {
			viewHolder.kehou_rel.setVisibility(View.VISIBLE);
		}else {
			viewHolder.kehou_rel.setVisibility(View.GONE);
		}
		if (list.get(position).hasPreHw==1) {
			viewHolder.keqian_rel.setVisibility(View.VISIBLE);
		}else {
			viewHolder.keqian_rel.setVisibility(View.GONE);
		}
		viewHolder.correct_homework_item_pigai.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//批改作业
				Intent intent=new Intent(context,PHomeworkStudentListActivity.class);
				intent.putExtra("activityId", list.get(position).id);
				intent.putExtra("part", 1);
				intent.putExtra("hour", list.get(position).hour);
				intent.putExtra("classroom", list.get(position).classroom);
				intent.putExtra("classroom_name", list.get(position).classroom_name);
				context.startActivity(intent);
			}
		});
		viewHolder.correct_homework_item_look.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//查看作业
				Intent intent=new Intent(context,AllStudentHomeworkActivity.class);
				intent.putExtra("activityId", list.get(position).id);
				intent.putExtra("part", 1);
				intent.putExtra("hour", list.get(position).hour);
				intent.putExtra("classroom_name", list.get(position).classroom_name);
				context.startActivity(intent);
			}
		});
		viewHolder.correct_homework_item_browse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//作业总览
				Intent intent=new Intent(context,HomeworkFinishActivity.class);
				intent.putExtra("part", 1);
				intent.putExtra("hour", list.get(position).hour);
				intent.putExtra("activityId", list.get(position).id);
				context.startActivity(intent);
			}
		});
		viewHolder.correct_homework_item_pigai2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,PHomeworkStudentListActivity.class);
				intent.putExtra("activityId", list.get(position).id);
				intent.putExtra("part", 3);
				intent.putExtra("hour", list.get(position).hour);
				intent.putExtra("classroom", list.get(position).classroom);
				intent.putExtra("classroom_name", list.get(position).classroom_name);
				context.startActivity(intent);
			}
		});
		viewHolder.correct_homework_item_look2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,AllStudentHomeworkActivity.class);
				intent.putExtra("activityId", list.get(position).id);
				intent.putExtra("part", 3);
				intent.putExtra("hour", list.get(position).hour);
				intent.putExtra("classroom_name", list.get(position).classroom_name);
				context.startActivity(intent);
			}
		});
		viewHolder.correct_homework_item_browse2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,HomeworkFinishActivity.class);
				intent.putExtra("part", 3);
				intent.putExtra("hour", list.get(position).hour);
				intent.putExtra("activityId", list.get(position).id);
				context.startActivity(intent);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView correct_homework_title;
		public TextView correct_homework_item_content;
//		public TextView correct_homework_item_yipigai;
		public TextView correct_homework_item_pigai;
		public TextView correct_homework_item_look;
		public TextView correct_homework_item_browse;
		public TextView correct_homework_item_pigai2;
		public TextView correct_homework_item_look2;
		public TextView correct_homework_item_browse2;
		public RelativeLayout kehou_rel;
		public RelativeLayout keqian_rel;
	}
}
