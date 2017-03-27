package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.TeachLogList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 */
public class TeachLogAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TeachLogList> list;

	public TeachLogAdapter(Context context, List<TeachLogList> list) {
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
			convertView = layoutInflater.inflate(R.layout.teach_log_item, null);
			viewHolder.teach_log_title = (TextView) convertView
					.findViewById(R.id.teach_log_title);
			viewHolder.teach_log_item_content = (TextView) convertView
					.findViewById(R.id.teach_log_item_content);
			viewHolder.teach_log_item_pingjia = (TextView) convertView
					.findViewById(R.id.teach_log_item_pingjia);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
		viewHolder.teach_log_title.setText(simpleDateFormat.format(new Date(list.get(position).date*1000)));
		viewHolder.teach_log_item_content
				.setText(list.get(position).name+"");
		String biaoxian = "";
		switch (list.get(position).behavior) {
		case 1:
			biaoxian = "优";
			break;
		case 2:
			biaoxian = "良";
			break;
		case 3:
			biaoxian = "中";
			break;
		case 4:
			biaoxian = "差";
			break;

		default:
			break;
		}
		viewHolder.teach_log_item_pingjia.setText(biaoxian);
		return convertView;
	}

	static class ViewHolder {
		public TextView teach_log_title;
		public TextView teach_log_item_content;
		public TextView teach_log_item_pingjia;
	}
}
