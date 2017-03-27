package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ImageHistoryInfo;

import java.util.List;

public class HistoryHAdapter extends BaseAdapter{
	private List<ImageHistoryInfo> imageInfos;
	private Context context;
	private LayoutInflater layoutInflater;
	public HistoryHAdapter(Context context, List<ImageHistoryInfo> imageInfos) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.imageInfos=imageInfos;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageInfos==null?0:imageInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return imageInfos.get(arg0);
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.homeworkshow_history_item,
					null);
			viewHolder.homework_show_item_image = (ImageView) convertView
					.findViewById(R.id.img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.index_item_back)
				.cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage("file://"+imageInfos.get(position).path,
				viewHolder.homework_show_item_image,options);
		return convertView;
	}
	static class ViewHolder {
		public ImageView homework_show_item_image;
	}
}
