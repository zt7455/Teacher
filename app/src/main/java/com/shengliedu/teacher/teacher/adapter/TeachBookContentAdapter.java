package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.TeachBookContent;

import java.util.List;

public class TeachBookContentAdapter extends BaseAdapter {


	private List<TeachBookContent> bookContent;
	private LayoutInflater layoutInflater;
	Context context;

	public TeachBookContentAdapter(Context context, List<TeachBookContent> bookContent) {
		super();
		this.bookContent = bookContent;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return bookContent.size();
	}

	@Override
	public Object getItem(int i) {
		return bookContent.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.book_content_child_item,
					viewGroup, false);
			viewHolder.book_item_text = (TextView) convertView
					.findViewById(R.id.book_item_text);
			viewHolder.book_item_page = (TextView) convertView
					.findViewById(R.id.book_item_page);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.book_item_text.setText(bookContent.get(i).name);
		if (bookContent.get(i).page!=0) {
			viewHolder.book_item_page.setText("第"+(bookContent.get(i).page+1)+"页");
		}else {
			viewHolder.book_item_page.setText("");
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView book_item_title;
		public TextView book_item_text;
		public TextView book_item_page;
	}

}
