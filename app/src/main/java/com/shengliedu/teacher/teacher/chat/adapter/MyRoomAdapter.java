/**
 * 
 */
package com.shengliedu.teacher.teacher.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.model.Room;

/**
 * @author MZH
 *
 */
public class MyRoomAdapter extends ArrayAdapter<Room> {
	Context context;
	
	public MyRoomAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_room, null);
		}
		final Room item = getItem(position);
		TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
		nameView.setText(item.name);
		return convertView;
	}
}
