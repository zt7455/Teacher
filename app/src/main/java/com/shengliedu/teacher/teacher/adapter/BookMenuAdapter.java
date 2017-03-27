package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.Teachbook;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;

import java.util.List;

/**
 * 
 * 
 */
public class BookMenuAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Teachbook> list;
	private Context context;
	private View[] views;
	public BookMenuAdapter(Context context, List<Teachbook> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		views=new View[list.size()+1];
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
		final ViewHolder viewHolder;
		if (views[position] == null) {
			viewHolder = new ViewHolder();
			views[position] = layoutInflater.inflate(R.layout.book_menu_item,
					null);
			viewHolder.book_menu_icon = (ImageView) views[position]
					.findViewById(R.id.book_menu_icon);
			viewHolder.book_menu_name = (TextView) views[position]
					.findViewById(R.id.book_menu_name);
			views[position].setTag(viewHolder);
//			viewHolder.update(); 
		} else {
			viewHolder = (ViewHolder) views[position].getTag();
		}
		StringBuilder bookInfo=new StringBuilder();
		if (list.get(position).schoolstage!=0) {
			bookInfo.append(MyApplication.getSchoolstageNameForId(list.get(position).schoolstage));
		}else {
			bookInfo.append("无学段信息");
		}
		if (list.get(position).subject!=0) {
			bookInfo.append(MyApplication.getSubjectNameForId(list.get(position).subject)+"\n");
		}else {
			bookInfo.append("无学科信息"+"\n");
		}
		if (!TextUtils.isEmpty(list.get(position).name)) {
			bookInfo.append(list.get(position).name.split("-")[1]+"册\n");
		}else {
			bookInfo.append("无年纪册别信息"+"\n");
		}
		if (list.get(position).editionVersion!=0) {
			bookInfo.append(MyApplication.getEditionVersionNameForId(list.get(position).editionVersion));
		}else {
			bookInfo.append("无版本信息");
		}
		viewHolder.book_menu_name.setText(bookInfo.toString());
		if (!TextUtils.isEmpty(list.get(position).cover)) {
			final ImageLoader imageLoader=ImageLoader.getInstance();
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.mipmap.no_fm)
			.showImageOnFail(R.mipmap.no_fm)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.build();
			imageLoader.displayImage(Config1.MAIN_BOOK_IP+"/"+list.get(position).cover, viewHolder.book_menu_icon,options);
		}
		return views[position];
	}

	private static class ViewHolder {
		TextView book_menu_name;
		ImageView book_menu_icon;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		views=new View[list.size()+1];
	}
}
