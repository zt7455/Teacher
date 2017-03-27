package com.shengliedu.teacher.teacher.adapter;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.Normal;

import java.util.List;

/**
 * 
 * 
 */
public class HomeworkFinishAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Normal> list;
	private Activity context;

	public HomeworkFinishAdapter(Activity context, List<Normal> list) {
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
			convertView = layoutInflater.inflate(R.layout.homework_finish_item,
					null);
			viewHolder.homework_finish_item_title = (TextView) convertView
					.findViewById(R.id.homework_finish_item_title);
			viewHolder.homework_finish_item_finish = (TextView) convertView
					.findViewById(R.id.homework_finish_item_finish);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String xuanx = "";
		String content = "";
//		if (list.get(position).isFinish == 1) {
//			content = "全部答完";
//		} else {
//			content = list.get(position).lCount + "人未答";
//		}
		JSONObject object= JSON.parseObject(list.get(position).teaching);
		if (!TextUtils
				.isEmpty(object.getString("name")) ){
			xuanx = object.getString("name");
		}
		if (list.get(position).content_type == 2) {
			content = list.get(position).submitCount + "已答  " + list.get(position).rCount + "人答对    "
					+ list.get(position).wCount + "人答错";
		}else if (list.get(position).content_type == 1) {
			content = list.get(position).submitCount + "已答  ";
			if ( list.get(position).submitType==5){
				content = "本作业无需提交";
			}
		}
		viewHolder.homework_finish_item_title.setText((position + 1) + "."
				+ Html.fromHtml(xuanx));
		viewHolder.homework_finish_item_finish.setText(content);
		return convertView;
	}

	static class ViewHolder {
		public TextView homework_finish_item_title;
		public TextView homework_finish_item_finish;
		public TextView correct_homework_item_yipigai;
		public TextView correct_homework_item_finish;
		public TextView correct_homework_item_pigai;
		public TextView correct_homework_item_look;
	}
}
