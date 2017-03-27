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

/**
 * 
 * 
 */
public class ConsoleMenuAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ConsoleMenu> list;

	public ConsoleMenuAdapter(Context context, List<ConsoleMenu> list) {
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
			convertView = layoutInflater.inflate(R.layout.console_menu_item,
					null);
			viewHolder.console_menu_icon = (ImageView) convertView
					.findViewById(R.id.console_menu_icon);
			viewHolder.console_menu_name = (TextView) convertView
					.findViewById(R.id.console_menu_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.console_menu_icon
				.setImageResource(list.get(position).menuImag);
		viewHolder.console_menu_name.setText(list.get(position).menuName);

		return convertView;
	}

	static class ViewHolder {
		TextView console_menu_name;
		ImageView console_menu_icon;
	}
}
