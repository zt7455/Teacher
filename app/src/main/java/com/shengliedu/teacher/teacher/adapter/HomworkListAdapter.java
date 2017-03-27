package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.BeanHomework2;

import java.util.List;

/**
 * 
 * 
 */
public class HomworkListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<BeanHomework2> list;

	public HomworkListAdapter(Context context, List<BeanHomework2> list) {
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
			convertView = layoutInflater.inflate(R.layout.homework_list_item,
					null);
			viewHolder.homework_finish_item_title = (TextView) convertView
					.findViewById(R.id.homework_finish_item_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject= JSON.parseObject(list.get(position).teaching);
		String type="其他";
		if (list.get(position).content_type==1){
			if (list.get(position).submitType==1){
				type="文本提交";
			}else if (list.get(position).submitType==2){
				type="图片提交";
			}else if (list.get(position).submitType==3){
				type="音频提交";
			}else if (list.get(position).submitType==4){
				type="视频提交";
			}else {
				type="素材题";
			}
		}else if (list.get(position).content_type==2){
			int subType=0;
			if (jsonObject.containsKey("subType")) {
				subType = jsonObject.getInteger("subType");
			}
			if (subType==1){
				type="单选题";
			}else if (subType==2){
				type="多选题";
			}else if (subType==3){
				type="判断题";
			}else {
				type="习题";
			}
		}
		viewHolder.homework_finish_item_title.setText("[" + type + "] "
				+ Html.fromHtml(jsonObject.getString("name")));
		return convertView;
	}

	static class ViewHolder {
		TextView homework_finish_item_title;
	}
}
