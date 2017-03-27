package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ImageInfo;
import com.shengliedu.teacher.teacher.util.Config1;

import java.util.List;

public class HCurrentAdapter extends IBaseAdapter{
	private List<ImageInfo> imageInfos;
	private Context context;
	public HCurrentAdapter(int row, int column) {
		super(row, column);
		// TODO Auto-generated constructor stub
	}
	public HCurrentAdapter(Context context, List<ImageInfo> imageInfos, int row, int column) {
		super(row, column);
		// TODO Auto-generated constructor stub
		this.imageInfos=imageInfos;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageInfos==null?0:imageInfos.size();
	}

	@Override
	public View getView(ViewGroup parent, int posterPosition) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.homework_show_item, parent, false);
		LinearLayout homework_show_item_lin = (LinearLayout) view
				.findViewById(R.id.homework_show_item_lin);
		ImageView homework_show_item_image=(ImageView) view.findViewById(R.id.homework_show_item_image);
		TextView homework_show_item_name=(TextView) view.findViewById(R.id.homework_show_item_name);
		if (posterPosition==0){
			homework_show_item_image.setImageResource(imageInfos
					.get(posterPosition).id);
			homework_show_item_lin.setVisibility(View.GONE);
		}else {
			homework_show_item_lin.setVisibility(View.VISIBLE);
			ImageLoader imageLoader = ImageLoader.getInstance();
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.mipmap.index_item_back)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			homework_show_item_image
					.setImageResource(R.mipmap.index_item_back);
			imageLoader.displayImage(Config1.IP + imageInfos.get(posterPosition).file,
					homework_show_item_image,options);
			homework_show_item_name
					.setText(imageInfos.get(posterPosition).realname);
		}
		return view;
	}

}
