package com.shengliedu.teacher.teacher.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.RoleInfo;

/**
 * 
 * 
 */
@SuppressLint("UseSparseArrays")
public class ChildListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<IdName> list;

	public ChildListAdapter(Context context, List<IdName> list) {
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

	@SuppressLint("ResourceAsColor")
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate

			(R.layout.child_item, null);
			viewHolder.child = (TextView) convertView.findViewById

			(R.id.child);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.child.setText(list.get(position).name);

		return convertView;
	}

	public static class ViewHolder {
		public TextView child;

	}
}
