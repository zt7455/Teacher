package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.PersonBean;

import java.util.List;

public class Sort2Adapter extends BaseAdapter {
	private Context context;
	private List<PersonBean> persons;
	private LayoutInflater inflater;

	public Sort2Adapter(Context context, List<PersonBean> persons) {
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
		int selection = person.firstpinyin.charAt(0);
		int positionForSelection = getPositionForSelection(selection);
		if (position == positionForSelection) {
			viewholder.tv_tag.setVisibility(View.VISIBLE);
			viewholder.tv_tag.setText(person.firstpinyin);
		} else {
			viewholder.tv_tag.setVisibility(View.GONE);
			

		}
		viewholder.tv_name.setText(person.realname);
		String pj="";
		switch (person.behavior) {
		case 0:
			pj="未评价";
			break;
		case 1:
			pj="A";
			break;
		case 2:
			pj="B";
			break;
		case 3:
			pj="C";
			break;
		case 4:
			pj="D";
			break;

		default:
			break;
		}
		viewholder.tv_pj.setText(pj);
		return convertView;
	}

	public int getPositionForSelection(int selection) {
		for (int i = 0; i < persons.size(); i++) {
			String Fpinyin = persons.get(i).firstpinyin;
			char first = Fpinyin.toUpperCase().charAt(0);
			if (first == selection) {
				return i;
			}
		}
		return -1;

	}

	class ViewHolder {
		TextView tv_tag;
		TextView tv_name;
		TextView tv_pj;
		TextView abc;
		ImageView isyue;
	}

}
