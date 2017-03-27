package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ImageInfo;
import com.shengliedu.teacher.teacher.util.Config1;

import java.util.List;

/**
 * 
 * 
 */
public class HomeworkShowListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ImageInfo> list;
	private Context context;

	public HomeworkShowListAdapter(Context context, List<ImageInfo> list) {
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
			convertView = layoutInflater.inflate(R.layout.homework_show_item,
					null);
			viewHolder.homework_show_item = (RelativeLayout) convertView
					.findViewById(R.id.homework_show_item);
			viewHolder.homework_show_item_lin = (LinearLayout) convertView
					.findViewById(R.id.homework_show_item_lin);
			viewHolder.homework_show_item_name = (TextView) convertView
					.findViewById(R.id.homework_show_item_name);
			viewHolder.homework_show_item_image = (ImageView) convertView
					.findViewById(R.id.homework_show_item_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		LayoutParams params = viewHolder.homework_show_item_image
				.getLayoutParams();
		params.width = width / 3;
		params.height = width / 3;
		viewHolder.homework_show_item_image.setLayoutParams(params);
		viewHolder.homework_show_item_lin.setVisibility(View.GONE);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.index_item_back)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		viewHolder.homework_show_item_image
				.setImageResource(R.mipmap.index_item_back);
		if (!TextUtils.isEmpty(list.get(position).answer)){
			JSONArray jsonArray= JSON.parseArray(list.get(position).answer);
			if (jsonArray!=null && jsonArray.size()>0){
				imageLoader.displayImage(Config1.IP + jsonArray.getJSONObject(0).getString("data"),
						viewHolder.homework_show_item_image, options);
			}
		}
//		viewHolder.homework_show_item_name.setText(list.get(position).realname);
		return convertView;
	}

	static class ViewHolder {
		public TextView homework_show_item_name;
		public LinearLayout homework_show_item_lin;
		public RelativeLayout homework_show_item;
		public ImageView homework_show_item_image;
	}
}
