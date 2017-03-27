package com.shengliedu.teacher.teacher.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.BKCheckActivity;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.view.NoScrollGridView;

import java.util.List;

public class BKJCListRAdapter extends BaseExpandableListAdapter {

	private List<ClassRoom> subjects;
	private LayoutInflater layoutInflater;
	BaseActivity activity;
	private int gradeId;
	public BKJCListRAdapter(BaseActivity context, List<ClassRoom> subjects) {
		super();
		this.subjects = subjects;
		this.activity = context;
		layoutInflater = LayoutInflater.from(context);
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return (subjects.get(arg0).teachers==null)?null:subjects.get(arg0).teachers.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.bkjc_r_two_item, parent, false);
			viewHolder.bkjc_two_list = (NoScrollGridView) convertView
					.findViewById(R.id.bkjc_two_list);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final List<IdName> teachers=subjects.get(groupPosition).teachers;
		final int subject=subjects.get(groupPosition).id;
		if (teachers!=null && teachers.size()>0){
			final BKJCRListAdapter adapter=new BKJCRListAdapter(activity, teachers);
		    viewHolder.bkjc_two_list.setAdapter(adapter);
			viewHolder.bkjc_two_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//					activity.toast("grade:"+gradeId+",subject:"+subject+",teacher:"+teachers.get(i).id);
					Log.v("BBBKKK","grade:"+gradeId+",subject:"+subject+",teacher:"+teachers.get(i).id);
					Intent intent=new Intent(activity, BKCheckActivity.class);
					intent.putExtra("gradeId",gradeId);
					intent.putExtra("subjectId",subject);
					intent.putExtra("userId",teachers.get(i).id);
					activity.startActivity(intent);
				}
			});
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return (subjects.get(groupPosition).teachers==null || subjects.get(groupPosition).teachers.size()==0)?0:1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return subjects.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return (subjects==null)?0:subjects.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.bkjc_r_one__item, parent, false);
			viewHolder.bkjc_item_title = (TextView) convertView
					.findViewById(R.id.bkjc_item_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (subjects.get(groupPosition).teachers!=null && subjects.get(groupPosition).teachers.size()>0){
			viewHolder.bkjc_item_title.setText("▎"+subjects.get(groupPosition).name);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	static class ViewHolder {
		public TextView bkjc_item_title;
		// public TextView homework_finish_item_finish;
		public NoScrollGridView bkjc_two_list;
	}
}
