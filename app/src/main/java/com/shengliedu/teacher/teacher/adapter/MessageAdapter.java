package com.shengliedu.teacher.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.MessageList;

/**
 * 
 * 
 */
public class MessageAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<MessageList> list;

	public MessageAdapter(Context context, List<MessageList> list) {
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
			convertView = layoutInflater.inflate(R.layout.message_item, null);
			viewHolder.message_text = (TextView) convertView
					.findViewById(R.id.message_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.message_text.setText(list.get(position).typeName+"："+list.get(position).title);
		return convertView;
	}

	static class ViewHolder {
		public TextView message_text;
	}
}
