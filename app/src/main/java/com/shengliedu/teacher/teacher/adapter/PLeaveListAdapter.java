package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

import java.util.List;

/**
 * 
 * 
 */
public class PLeaveListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Normal> list;
	private Context context;

	public PLeaveListAdapter(Context context, List<Normal> list) {
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

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.p_leave_item, null);
			viewHolder.p_leave_item_info = (TextView) convertView
					.findViewById(R.id.p_leave_item_info);
			viewHolder.p_leave_item_state = (ImageView) convertView
					.findViewById(R.id.p_leave_item_state);
			viewHolder.p_leave_item_rel = (RelativeLayout) convertView
					.findViewById(R.id.p_leave_item_rel);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.p_leave_item_info.setText(MyApplication.getClassRoomNameForclassroomid(list.get(position).classroom)+"-"+list.get(position).studentName);
		switch (list.get(position).approve) {
		case 1:
			viewHolder.p_leave_item_rel.setBackgroundResource(R.color.blue1);
			viewHolder.p_leave_item_state.setImageResource(R.mipmap.p_leave_ok);
			break;
		case 2:
			viewHolder.p_leave_item_rel.setBackgroundResource(R.color.red2);
			viewHolder.p_leave_item_state.setImageResource(R.mipmap.p_leave_no);
			break;
		case 0:
			viewHolder.p_leave_item_rel.setBackgroundResource(R.color.seagreen);
			viewHolder.p_leave_item_state.setImageBitmap(null);
			break;

		default:
			break;
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView p_leave_item_info;
		public RelativeLayout p_leave_item_rel;
		public ImageView p_leave_item_state;
	}
}
