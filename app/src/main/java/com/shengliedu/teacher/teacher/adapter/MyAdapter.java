package com.shengliedu.teacher.teacher.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.HomeworkShowActivity;
import com.shengliedu.teacher.teacher.bean.Homeworkshow;
import com.shengliedu.teacher.teacher.view.NoScrollGridView;
import com.shengliedu.teacher.teacher.view.NoScrollListview;
import com.shengliedu.teacher.teacher.view.PhotoView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends BaseAdapter {
	private List<Homeworkshow> beans;
	private HomeworkShowActivity context;
	private LayoutInflater inflater;

	public MyAdapter(List<Homeworkshow> beans, HomeworkShowActivity context) {
		super();
		this.beans = beans;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return beans.get(position).type;
	}

	public class HistoryHolder {
		private TextView textView;
		private NoScrollGridView resultListView;

		// public void setData(HomeworkshowHistory data){
		// textView.setText(data.describe);
		// HHistoryAdapter hHistoryAdapter=new HHistoryAdapter(context,
		// data.imageHistoryInfos,
		// data.imageHistoryInfos.size()%3==0?data.imageHistoryInfos.size()/3:data.imageHistoryInfos.size()/3+1,
		// 3);
		// resultListView.setAdapter(hHistoryAdapter);
		// }
	}

	public class CurrentHolder {
		private NoScrollListview resultListView;

		// public void setData(List<ImageInfo> imageInfos){
		// HCurrentAdapter hCurrentAdapter=new HCurrentAdapter(context,
		// imageInfos,
		// imageInfos.size()%3==0?imageInfos.size()/3:imageInfos.size()/3+1, 3);
		// resultListView.setAdapter(hCurrentAdapter);
		// }
	}

	public class HintHolder {

	}

	public class TitleHolder {

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans == null ? 0 : beans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	HashMap<Integer, Object> map = new HashMap<Integer, Object>();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HintHolder holder1 = null;
		CurrentHolder holder2 = null;
		HistoryHolder holder3 = null;
		TitleHolder holder4 = null;
		int type = getItemViewType(position);
		Log.v("TAG", "type=" + type);
		if (map.get(1) == null || map.get(2) == null || map.get(3) == null
				|| map.get(4) == null) {
			inflater = LayoutInflater.from(context);
			// 按当前所需的样式，确定new的布局
			switch (type) {
			case 1:
				convertView = inflater.inflate(
						R.layout.homeworkshow_recycle_hint_item, parent, false);
				holder1 = new HintHolder();
				map.put(1, convertView);
				convertView.setTag(R.id.tag_1, map);
				break;
			case 2:
				convertView = inflater.inflate(
						R.layout.homeworkshow_recycle_current_item, parent,
						false);
				holder2 = new CurrentHolder();
				holder2.resultListView = (NoScrollListview) convertView
						.findViewById(R.id.resultListView_C);
				map.put(2, convertView);
				convertView.setTag(R.id.tag_2, map);
				break;
			case 3:
				convertView = inflater.inflate(
						R.layout.homeworkshow_recycle_history_item, parent,
						false);
				holder3 = new HistoryHolder();
				holder3.textView = (TextView) convertView
						.findViewById(R.id.title);
				holder3.resultListView = (NoScrollGridView) convertView
						.findViewById(R.id.resultListView_H);
				map.put(3, convertView);
				convertView.setTag(R.id.tag_3, map);
				break;
			case 4:
				convertView = inflater
						.inflate(R.layout.homeworkshow_recycle_title_item,
								parent, false);
				holder4 = new TitleHolder();
				map.put(4, convertView);
				convertView.setTag(R.id.tag_4, map);
				break;
			default:
				break;
			}

		} else {
			convertView = (View) map.get(type - 1);
			switch (type) {
			case 1:
				holder1 = (HintHolder) convertView.getTag(R.id.tag_1);
				break;
			case 2:
				holder2 = (CurrentHolder) convertView.getTag(R.id.tag_2);
				break;
			case 3:
				holder3 = (HistoryHolder) convertView.getTag(R.id.tag_3);
			case 4:
				holder4 = (TitleHolder) convertView.getTag(R.id.tag_4);
				break;
			}
		}
		// 设置资源
		switch (type) {
		case 1:
			break;
		case 2:
			Log.v("TAG", "studentssize="+beans.get(position).students.size());
			CurrentHAdapter currentHAdapter = new CurrentHAdapter(context,
					beans.get(position).students);
			holder2.resultListView.setAdapter(currentHAdapter);
			holder2.resultListView
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							if (beans.get(position) != null) {
								context.userId=beans.get(position).students.get(arg2).id;
								context.activityId=beans.get(position).activity;
								photo();
							}

						}
					});
			
			break;
		case 3:
			holder3.textView
					.setText(beans.get(position).homeworkshowHistory.describe);
			HistoryHAdapter hHistoryAdapter = new HistoryHAdapter(context,
					beans.get(position).homeworkshowHistory.imageHistoryInfos);
			holder3.resultListView.setAdapter(hHistoryAdapter);
			holder3.resultListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					showDialog(
							context,"file:/"+beans.get(position).homeworkshowHistory.imageHistoryInfos.get(arg2).path);
				}
			});
			break;
		case 4:
			break;
		}
		return convertView;
	}

	// ----------查看照片
	private Dialog d;
	private PhotoView iv;
	private Button btn_close_photo;

	private void showDialog(Activity context, final String path1) {
		// TODO Auto-generated method stub
		d = null;
		if (null == d) {
			View v = LayoutInflater.from(context).inflate(R.layout.see_photo,
					null);
			d = new Dialog(context, R.style.dialogss);
			d.addContentView(v, new LayoutParams(context.getWindowManager()
					.getDefaultDisplay().getWidth(), context.getWindowManager()
					.getDefaultDisplay().getHeight()));
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
//		BitmapUtils utils = new BitmapUtils(context);
//		utils.display(iv, path1);
		ImageLoader imageLoader=ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.mipmap.no_fm)
		.showImageOnFail(R.mipmap.no_fm)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.build();
		imageLoader.displayImage(path1,iv,options);
		d.show();
	}
	
	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = Environment
					.getExternalStorageDirectory().getPath() + "/zzkt/";
			File file = null;
			Log.e("wwwwwwwwwwww","sdcardState::::"+sdcardState);
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				String sdcardPathDirhomework = Environment
						.getExternalStorageDirectory().getPath() + "/zzkt/cache/";
				File fileDirHomework = new File(sdcardPathDirhomework);
				if (!fileDirHomework.exists()) {
					fileDirHomework.mkdirs();
				}
				
				// 是否有headImg文件
				file = new File(sdcardPathDirhomework + System.currentTimeMillis()
						+ ".JPEG");
			}
			if (file != null) {
				context.photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, context.photoUri);
				context.startActivityForResult(openCameraIntent, HomeworkShowActivity.TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
