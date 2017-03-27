package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.IdName;

import java.util.List;

/**
 * 
 * 
 */
public class MyZoneAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<IdName> list;

	public MyZoneAdapter(Context context, List<IdName> list) {
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
					.inflate(R.layout.myz_zone_item, null);
			viewHolder.my_zone_image = (ImageView) convertView
					.findViewById(R.id.my_zone_image);
			viewHolder.my_zone_name = (TextView) convertView
					.findViewById(R.id.my_zone_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).type==1) {
			if (list.get(position).location.toLowerCase().endsWith(".doc")|| list.get(position).location.toLowerCase().endsWith(".docx")) {
				viewHolder.my_zone_image.setImageResource(R.mipmap.word_icon);
			}else if (list.get(position).location.toLowerCase().endsWith(".ppt")|| list.get(position).location.toLowerCase().endsWith(".pptx")) {
				viewHolder.my_zone_image.setImageResource(R.mipmap.ppt_icon);
			}else if (list.get(position).location.toLowerCase().endsWith(".pdf")) {
				viewHolder.my_zone_image.setImageResource(R.mipmap.pdf_icon);
			}else {
				viewHolder.my_zone_image.setImageResource(R.mipmap.text_icon);
			}
		}else if (list.get(position).type==2) {
			viewHolder.my_zone_image.setImageResource(R.mipmap.photo_icon);
		}else if (list.get(position).type==3) {
			viewHolder.my_zone_image.setImageResource(R.mipmap.music_icon);
		}else if (list.get(position).type==4) {
			viewHolder.my_zone_image.setImageResource(R.mipmap.video_icon);
		}else if (list.get(position).type==5) {
			viewHolder.my_zone_image.setImageResource(R.mipmap.other_icon);
		}
		viewHolder.my_zone_name.setText(list.get(position).name);
		return convertView;
	}

	static class ViewHolder {
		public ImageView my_zone_image;
		public TextView my_zone_name;
	}

}
