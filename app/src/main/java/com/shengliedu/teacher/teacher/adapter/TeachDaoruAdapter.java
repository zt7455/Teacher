package com.shengliedu.teacher.teacher.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.Beike;
import com.shengliedu.teacher.teacher.util.HtmlImage;

import java.util.List;

public class TeachDaoruAdapter extends BaseAdapter {

	private List<Beike> guide;
	private LayoutInflater layoutInflater;
	Activity context;

	public TeachDaoruAdapter(Activity context, List<Beike> guide) {
		super();
		this.guide = guide;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return guide.size();
	}

	@Override
	public Object getItem(int i) {
		return guide.get(i);
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
			convertView = layoutInflater.inflate(R.layout.teach_daoru_item,
					viewGroup, false);
			viewHolder.teach_daoru_item_text = (TextView) convertView
					.findViewById(R.id.teach_daoru_item_text);
			viewHolder.daoru_lin = (LinearLayout) convertView
					.findViewById(R.id.daoru_lin);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.teach_daoru_item_text.setText(Html.fromHtml(guide.get(groupPosition).content.get(childPosition).name));
		viewHolder.teach_daoru_item_text.setText(Html.fromHtml(HtmlImage
				.deleteSrc(guide.get(i).name)));
		String main = guide.get(i).name;
		List<String> imgList = HtmlImage.getImgSrc(main);
		ImageLoader utils = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.index_item_back)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		viewHolder.daoru_lin.removeAllViews();
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(context);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
//				imageView.setLayoutParams(new LayoutParams(
//						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
//						android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				utils.displayImage(url, imageView,options);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(context, url);
					}
				});
				viewHolder.daoru_lin.addView(imageView);
			}
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView teach_process_item_title;
		public TextView teach_daoru_item_text;
		public LinearLayout daoru_lin;
	}

}
