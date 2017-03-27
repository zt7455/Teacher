package com.shengliedu.teacher.teacher.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.HomeworkShowActivity;
import com.shengliedu.teacher.teacher.activity.HomeworkShowStudentPicActivity;
import com.shengliedu.teacher.teacher.bean.HomeworkshowStudent;

import java.io.Serializable;
import java.util.List;

import static com.shengliedu.teacher.teacher.R.id.qipao2;

public class CurrentHAdapter extends BaseAdapter {
	private List<HomeworkshowStudent> persons;
	private HomeworkShowActivity context;
	private LayoutInflater layoutInflater;

	public CurrentHAdapter(HomeworkShowActivity context, List<HomeworkshowStudent> persons) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.persons = persons;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return persons == null ? 0 : persons.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return persons.size();
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		final HomeworkshowStudent person = persons.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.student_list_item,
					null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_lv_item_name2);
			viewHolder.tv_pj = (TextView) convertView
					.findViewById(R.id.tv_lv_item_pj2);
			viewHolder.tv_lv_item_qipao = (TextView) convertView
					.findViewById(qipao2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(person.realname);
		viewHolder.tv_lv_item_qipao.setText(person.imageCount + "");
		if (person.imageCount != 0) {
			viewHolder.tv_pj.setText("浏览");
			viewHolder.tv_lv_item_qipao.setBackgroundResource(R.mipmap.index2);
			viewHolder.tv_pj.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,
							HomeworkShowStudentPicActivity.class);
					intent.putExtra("pics", (Serializable) person.images);
					intent.putExtra("t",person.realname );
					context.startActivityForResult(intent, 500);
				}
			});
		} else {
			viewHolder.tv_lv_item_qipao.setBackgroundResource(R.mipmap.index);
			viewHolder.tv_pj.setText("");
			viewHolder.tv_pj.setOnClickListener(null);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView tv_name;
		TextView tv_pj;
		TextView tv_lv_item_qipao;
	}
}
