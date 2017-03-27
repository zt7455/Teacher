package com.shengliedu.teacher.teacher.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout.LayoutParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.HomeworkShowListAdapter;
import com.shengliedu.teacher.teacher.bean.ImageInfo;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.view.CustomDialog;
import com.shengliedu.teacher.teacher.view.PhotoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkShowStudentPicActivity extends BaseActivity {
	private GridView homework_show_grid;
	private List<ImageInfo> imageInfolist;
	private HomeworkShowListAdapter homeworkShowListAdapter;
	private int result;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		imageInfolist=(List<ImageInfo>) getIntent().getSerializableExtra("pics");
		if (MyApplication.listIsEmpty(imageInfolist)) {
			imageInfolist= new ArrayList<ImageInfo>();
		}
		setBackSetResult(result);
		showTitle(getIntent().getStringExtra("t"));
		homework_show_grid = getView(R.id.homework_show_grid);
		homeworkShowListAdapter = new HomeworkShowListAdapter(this,
				imageInfolist);
		homework_show_grid.setAdapter(homeworkShowListAdapter);
		homework_show_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				JSONArray jsonArray= JSON.parseArray(imageInfolist.get(arg2).answer);
						showDialog(HomeworkShowStudentPicActivity.this,Config1.IP+jsonArray.getJSONObject(0).getString("data"));
			}
		});
		homework_show_grid
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						CustomDialog.Builder customDialog = new CustomDialog.Builder(
								context);
						customDialog.setMessage("亲,是否删除该展示图片?");
						customDialog.setTitle("提示");
						customDialog.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											int which) {
										// 设置你的操作事项
										dialog.dismiss();
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("id",imageInfolist.get(arg2).id);
										doDelete(Config1.getInstance()
														.IP+"activityStudent/"+imageInfolist.get(arg2).id,
												map,
												new ResultCallback() {
													@Override
													public void onFailure(Call call, IOException e) {
handlerReq.sendEmptyMessage(2);
													}

													@Override
													public void onResponse(Call call, Response response,String json)  {
														Message message=Message.obtain();
														message.what=1;
														message.obj=arg2;
														handlerReq.sendMessage(message);
													}
												});
									}
								});
						customDialog.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						customDialog.create().show();
						return true;
					}
				});
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				toast("删除成功");
				int a= (int) msg.obj;
				imageInfolist.remove(a);
				homeworkShowListAdapter.notifyDataSetChanged();
				HomeworkShowStudentPicActivity.this.result=201;
				setResult(201);
				if (imageInfolist.size()==0) {
					finish();
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homework_show_stupic;
	}

	//----------查看照片
	private Dialog d;
//	private ZoomImageView iv;
	private PhotoView iv;
	private Button btn_close_photo;
	private void showDialog(Activity context,final String path1) {
		// TODO Auto-generated method stub
		d = null;
		if (null == d) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.see_photo, null);
			d = new Dialog(this, R.style.dialogss);
			d.addContentView(v, new LayoutParams( context
					.getWindowManager().getDefaultDisplay().getWidth(),
					this.getWindowManager().getDefaultDisplay()
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			setResult(result);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
