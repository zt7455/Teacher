package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.PersonBean;

import java.util.List;

public class StudentBehaviorSubmitAdapter extends BaseAdapter {
	private Context context;
	private List<PersonBean> persons;
	private LayoutInflater inflater;

	public StudentBehaviorSubmitAdapter(Context context, List<PersonBean> persons) {
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
		PersonBean person = persons.get(position);
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.person_list_submit_item, null);
			viewholder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_lv_item_name);
			viewholder.tv_pj = (TextView) convertView
					.findViewById(R.id.tv_lv_item_pj);
			viewholder.submitCount = (TextView) convertView
					.findViewById(R.id.abc);
			viewholder.isyue = (ImageView) convertView
					.findViewById(R.id.isyue);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.tv_name.setText(person.realname);
		viewholder.isyue.setAlpha(0.35f);
		switch (person.checkFinish) {
		case 1:
			viewholder.isyue.setVisibility(View.VISIBLE);
			break;
		case 0:
			viewholder.isyue.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		if (!TextUtils.isEmpty(person.submitTime)) {
			viewholder.tv_pj.setText("提交作业时间："+person.submitTime.substring(5, person.submitTime.length()-3));
		}else {
			viewholder.tv_pj.setText("未提交作业");
		}
		if (person.submitCount==0) {
			viewholder.submitCount.setBackgroundResource(R.mipmap.index);
		}else {
			viewholder.submitCount.setBackgroundResource(R.mipmap.index2);
		}
		viewholder.submitCount.setText(person.submitCount+"");
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_pj;
		TextView submitCount;
		ImageView isyue;
	}

}
