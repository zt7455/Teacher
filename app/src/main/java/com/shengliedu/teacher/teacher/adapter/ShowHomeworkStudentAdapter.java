package com.shengliedu.teacher.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.PersonBean;

public class ShowHomeworkStudentAdapter extends BaseAdapter {
	private Context context;
	private List<IdName> persons;
	private LayoutInflater inflater;

	public ShowHomeworkStudentAdapter(Context context, List<IdName> persons) {
		this.context = context;
		this.persons = persons;
		this.inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return persons.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		IdName person = persons.get(position);
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.student_list_item, parent,false);
			viewholder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_lv_item_name2);
			viewholder.tv_pj = (TextView) convertView
					.findViewById(R.id.tv_lv_item_pj2);
			viewholder.tv_lv_item_qipao = (TextView) convertView
					.findViewById(R.id.qipao2);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		Log.v("TAG","imageCount="+person.imageCount);
		viewholder.tv_name.setText(person.realname);
		viewholder.tv_lv_item_qipao.setText(person.imageCount+"");
		viewholder.tv_pj.setText("去展评");
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_pj;
		TextView tv_lv_item_qipao;
	}

}
