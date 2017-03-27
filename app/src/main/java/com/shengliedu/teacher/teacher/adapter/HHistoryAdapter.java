package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ImageHistoryInfo;

import java.util.List;

public class HHistoryAdapter extends IBaseAdapter{
	private List<ImageHistoryInfo> imageInfos;
	private Context context;
	public HHistoryAdapter(int row, int column) {
		super(row, column);
		// TODO Auto-generated constructor stub
	}
	public HHistoryAdapter(Context context, List<ImageHistoryInfo> imageInfos, int row, int column) {
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
		View view=inflater.inflate(R.layout.homeworkshow_history_item, parent, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.img);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.index_item_back)
				.cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage("file://"+imageInfos.get(posterPosition).path,
				imageView,options);
		return view;
	}

}
