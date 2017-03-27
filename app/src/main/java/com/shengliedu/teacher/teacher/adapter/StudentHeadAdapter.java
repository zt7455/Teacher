package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.UserInfo;
import com.shengliedu.teacher.teacher.util.Config1;

import java.util.List;

/**
 * 
 * 
 */
public class StudentHeadAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<UserInfo> list;
	public StudentHeadAdapter(Context context, List<UserInfo> list,int classroomId) {
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
			convertView = layoutInflater.inflate(R.layout.student_head_item,
					null);
			viewHolder.console_menu_icon = (ImageView) convertView
					.findViewById(R.id.console_menu_icon);
			viewHolder.console_menu_name = (TextView) convertView
					.findViewById(R.id.console_menu_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.console_menu_icon
//				.setImageResource(list.get(position).image);
		String name=list.get(position).image;
		if (!TextUtils.isEmpty(name)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Config1.IP+name.replace("\\/","/"),viewHolder.console_menu_icon);
		}else {
			viewHolder.console_menu_icon.setImageResource(R.mipmap.default_icon);
		}
		viewHolder.console_menu_name.setText(list.get(position).realname);

		return convertView;
	}

	static class ViewHolder {
		TextView console_menu_name;
		ImageView console_menu_icon;
	}
}
