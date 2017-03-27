package com.shengliedu.teacher.teacher.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.ImagePGActivity;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.view.PhotoView;

import java.util.List;

/**
 * 
 * 
 */
public class HomeworkShowImageAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Normal> list;
	private Activity context;
	private int id;

	public HomeworkShowImageAdapter(Activity context, List<Normal> list,int id) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.id = id;
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.homework_show_image_item,
					null);
			viewHolder.homework_show_item_image = (ImageView) convertView
					.findViewById(R.id.homework_show_item_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(list.get(position).data)) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.mipmap.index_item_back)
					.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			imageLoader.displayImage(Config1.IP+list.get(position).data,viewHolder.homework_show_item_image,options);
			viewHolder.homework_show_item_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					showDialog(Config1.IP+list.get(position).data);
					Intent intent=new Intent(context, ImagePGActivity.class);
					intent.putExtra("link", Config1.getInstance().IP+"appparent.html#/homeworkImage/"+id+"/"+list.get(position).data.substring(list.get(position).data.lastIndexOf("/")+1));
					context.startActivity(intent);
				}
			});
		}
		return convertView;
	}

	static class ViewHolder {
		public ImageView homework_show_item_image;
	}
	
	
	//----------查看照片
		private Dialog d;
		private PhotoView iv;
		private Button btn_close_photo;
		private void showDialog(final String path1) {
			// TODO Auto-generated method stub
			d = null;
			if (null == d) {
				View v = LayoutInflater.from(context).inflate(
						R.layout.see_photo, null);
				d = new Dialog(context, R.style.dialogss);
				d.addContentView(v, new LayoutParams(context
						.getWindowManager().getDefaultDisplay().getWidth(),
						context.getWindowManager().getDefaultDisplay()
								.getHeight()));
				iv = (PhotoView) v.findViewById(R.id.see_photoview);
				iv.enable();
				btn_close_photo = (Button) v.findViewById(R.id.btn_close_photo);
				btn_close_photo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						d.dismiss();
					}
				});
			}
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(path1,iv);
			d.show();
		}
}
