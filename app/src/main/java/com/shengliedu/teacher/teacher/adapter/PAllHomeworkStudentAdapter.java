package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

import java.util.List;

public class PAllHomeworkStudentAdapter extends BaseAdapter {
	private Context context;
	private List<Normal> persons;
	private LayoutInflater inflater;
	private int ctype;

	public PAllHomeworkStudentAdapter(Context context, List<Normal> persons,int ctype) {
		this.context = context;
		this.ctype = ctype;
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
		Normal person = persons.get(position);
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.person_list_item, null);
			viewholder.tv_tag = (TextView) convertView
					.findViewById(R.id.tv_lv_item_tag);
			viewholder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_lv_item_name);
			viewholder.tv_pj = (TextView) convertView
					.findViewById(R.id.tv_lv_item_pj);
			viewholder.abc = (TextView) convertView
					.findViewById(R.id.abc);
			viewholder.isyue = (ImageView) convertView
					.findViewById(R.id.isyue);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.abc.setVisibility(View.GONE);
		viewholder.isyue.setVisibility(View.GONE);
		viewholder.tv_tag.setVisibility(View.GONE);
		viewholder.tv_name.setText(person.realname);
		Log.v("TAG", "ctype:"+ctype);
		JSONArray jsonArray= JSON.parseArray(person.studentAnswer);
		if (ctype==2) {
			if (!MyApplication.listIsEmpty(jsonArray)) {
				viewholder.tv_pj.setVisibility(View.VISIBLE);
				viewholder.tv_pj.setText(jsonArray.getJSONObject(0).getString("data")+"");
			}else {
				viewholder.tv_pj.setText("未回答");
			}
		}else if (ctype==3) {
			viewholder.tv_pj.setVisibility(View.VISIBLE);
			String awswer="";
			switch (person.quality) {
			case 0:
				awswer="未评价";
				break;
			case 1:
				awswer="优";
				break;
			case 2:
				awswer="良";
				break;
			case 3:
				awswer="中";
				break;
			case 4:
				awswer="差";
				break;

			default:
				break;
			}
			viewholder.tv_pj.setText(awswer);
		}else {
			viewholder.tv_pj.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_tag;
		TextView tv_name;
		TextView tv_pj;
		TextView abc;
		ImageView isyue;
	}

}
