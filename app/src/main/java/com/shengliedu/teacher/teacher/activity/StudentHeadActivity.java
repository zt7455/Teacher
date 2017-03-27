package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.StudentHeadAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.UserInfo;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.FileUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.UploadUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class StudentHeadActivity extends BaseActivity implements OnClickListener {
	private GridView grid_head;
	private List<ClassRoom> leadClassrooms;
	private int classroomId;
	private List<UserInfo> userInfos=new ArrayList<UserInfo>();
	private StudentHeadAdapter adapter;
	
	private PopupWindow pop;
	private int classLocation;
	private int cid;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		leadClassrooms=	MyApplication.userInfo.leadClassrooms;
		setBack();
		showTitle("学生头像管理");
		grid_head=getView(R.id.grid_head);
		if (!MyApplication.listIsEmpty(leadClassrooms)) {
			classLocation=0;
			classroomId=leadClassrooms.get(classLocation).id;
			adapter=new StudentHeadAdapter(this, userInfos,classroomId);
			grid_head.setAdapter(adapter);
			grid_head.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					cid=userInfos.get(arg2).id;
					showPop();
				}
			});
		}else {
			toast("没有班级的班主任是班主任吗？");
		}
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject object=JSON.parseObject((String) msg.obj);
				List<UserInfo> userInfos1=JSON.parseArray(object.getString("students"), UserInfo.class);
				userInfos.clear();
				if (!MyApplication.listIsEmpty(userInfos1)) {
					userInfos.addAll(userInfos1);
				}
				adapter.notifyDataSetChanged();
			}else if (msg.what==2){

			}else if (msg.what==3){
				getDatas();
			}else if (msg.what==4){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (classroomId!=0) {
//			http://dc.yunclass.com:90/classroom/29?withStudents=1
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("withStudents", 1);
 		doGet(Config1.getInstance().IP+"classroom/"+classroomId+"?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json)  {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_student_head;
	}
	
	private void showPop() {
		// TODO Auto-generated method stub
		if (pop == null) {
			View v = LayoutInflater.from(this).inflate(R.layout.select_picture,
					null);

			pop = new PopupWindow(this);
			pop.setContentView(v);
			pop.setWidth(LayoutParams.FILL_PARENT);
			pop.setHeight(LayoutParams.WRAP_CONTENT);
			pop.setOutsideTouchable(true);
			pop.setFocusable(true);
			pop.setAnimationStyle(R.style.PopupAnimation);
			pop.setBackgroundDrawable(new ColorDrawable(0));
			Button take_photo = (Button) v.findViewById(R.id.take_photo);
			take_photo.setOnClickListener(this);
			Button xiangce = (Button) v.findViewById(R.id.xiangce);
			xiangce.setOnClickListener(this);
			Button quxiao = (Button) v.findViewById(R.id.quxiao);
			quxiao.setOnClickListener(this);
		}
		pop.showAtLocation(findViewById(R.id.person_center), Gravity.BOTTOM, 0,
				0);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.take_photo:// 拍照
			if (pop != null) {
				pop.dismiss();
			}
			photo();
			break;
		case R.id.xiangce:// 相册
			if (pop != null) {
				pop.dismiss();
			}
			// 从相册中选择
			Intent intent = new Intent(
					// 相册
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, RESULT_LOAD_IMAGE);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			startPhotoZoom(photoUri);
			break;
		case RESULT_LOAD_IMAGE:
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;
		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK && null != data) {// 裁剪返回
				final String url = data.getStringExtra("fileurl");
				toast(path + "");
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						File file = new File(path);
						handler.sendEmptyMessage(2);
						UploadUtil u = new UploadUtil();
						String a = u.uploadHead(file, Config1.getInstance()
								.FILEUPLOAD());
						handler.sendEmptyMessage(3);
						if (!TextUtils.isEmpty(a)) {
							Message message = Message.obtain();
							message.obj = a;
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}.start();
			}
			break;
		}
	}
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String a = (String) msg.obj;
				Map<String,Object> map=new HashMap<>();
				map.put("userId", cid+ "");
				map.put("infoKey", "image");
				map.put("keyValue",
						a.substring(a.indexOf("link") + 7, a.indexOf(",") - 1));
				doPost(Config1.getInstance().UPDATEUSERINFO(), map,
						new ResultCallback() {
							@Override
							public void onFailure(Call call, IOException e) {
								handlerReq.sendEmptyMessage(4);
							}

							@Override
							public void onResponse(Call call, Response response,String json)  {
								Message message=Message.obtain();
								message.what=3;
								message.obj=json;
								handlerReq.sendMessage(message);
							}
						});
			} else if (msg.what == 2) {
				showRoundProcessDialog(StudentHeadActivity.this);
			} else if (msg.what == 3) {
				showRoundProcessDialogCancel();
			}
		}
	};
	// ------------------------------------------------------
		private static final int TAKE_PICTURE = 0;
		private static final int RESULT_LOAD_IMAGE = 1;
		private static final int CUT_PHOTO_REQUEST_CODE = 2;
		private String path = "";
		private Uri photoUri;

		public void photo() {
			try {
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				String sdcardState = Environment.getExternalStorageState();
				String sdcardPathDir = Environment
						.getExternalStorageDirectory().getPath() + "/zzkt/";
				File file = null;
				if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
					// 有sd卡，是否有myImage文件夹
					File fileDir = new File(sdcardPathDir);
					if (!fileDir.exists()) {
						fileDir.mkdirs();
					}
					// 是否有headImg文件
					file = new File(sdcardPathDir + System.currentTimeMillis()
							+ ".JPEG");
				}
				if (file != null) {
					path = file.getPath();
					photoUri = Uri.fromFile(file);
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
					startActivityForResult(openCameraIntent, TAKE_PICTURE);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void startPhotoZoom(Uri uri) {
			try {
				// 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyyMMddhhmmss");
				String address = sDateFormat.format(new java.util.Date());
				if (!FileUtils.isFileExist("")) {
					FileUtils.createSDDir("");
				}
				Uri imageUri = Uri.parse("file:///sdcard/formats/" + address
						+ ".JPEG");
				final Intent intent = new Intent("com.android.camera.action.CROP");

				// 照片URL地址
				intent.setDataAndType(uri, "image/*");

				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 480);
				intent.putExtra("outputY", 480);
				// 输出路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				// 输出格式
				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				// 不启用人脸识别
				intent.putExtra("noFaceDetection", false);
				intent.putExtra("return-data", false);
				intent.putExtra("fileurl", FileUtils.SDPATH + address + ".JPEG");
				path = FileUtils.SDPATH + address + ".JPEG";
				Log.v("TAG", "url0=" + path);
				startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
