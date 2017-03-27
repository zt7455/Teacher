package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
	private List<String> imageUrls; 
	private Context context;
	private LayoutInflater layoutInflater;

	public ImageAdapter(List<String> imageUrls, Context context) {
		this.imageUrls = imageUrls;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return imageUrls.size();
	}

	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate

			(R.layout.textbook_item, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			viewHolder.page = (TextView) convertView
					.findViewById(R.id.page);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.v("TAG", imageUrls.get(position));
		viewHolder.page.setText((position+1)+"/"+imageUrls.size());
//		BitmapUtils utils=new BitmapUtils(context, Environment.getExternalStorageDirectory()
//			+ "/zzkt/");
//		utils.display(viewHolder.imageView, imageUrls.get(position));
		ImageLoader imageLoader=ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.mipmap.no_fm)
		.showImageOnFail(R.mipmap.no_fm)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.build();
		imageLoader.displayImage(imageUrls.get(position), viewHolder.imageView,options);
//		new CacheImageAsyncTask(viewHolder.imageView, context).execute(imageUrls.get(position));
		return convertView;
	}
	class ViewHolder{
		ImageView imageView;
		TextView page;
	}
}
