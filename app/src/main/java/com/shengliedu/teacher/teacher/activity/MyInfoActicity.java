package com.shengliedu.teacher.teacher.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.UserInfo;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.BitmapBlur;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.FileUtils;
import com.shengliedu.teacher.teacher.util.HttpImage;
import com.shengliedu.teacher.teacher.util.PopupWindowUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.UploadUtil;
import com.shengliedu.teacher.teacher.util.ZZKTHttpDownLoad;
import com.shengliedu.teacher.teacher.view.PickerView;
import com.shengliedu.teacher.teacher.view.PickerView.onSelectListener;
import com.shengliedu.teacher.teacher.view.RoundImageView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MyInfoActicity extends BaseActivity implements OnClickListener {
	private RoundImageView touxiang;
	private RelativeLayout head_rel, name_rel, sex_rel, birth_rel, phone_rel,
			addr_rel, qm_rel;
	private ImageView back, setting;
	private TextView change_head_text, name_text, sex_text, birth_text,
			phone_text, addr_text, qm_text;
	private PopupWindow pop;


	private int changetype;
	private String changeText = "";
	private int sexvalue = MyApplication.userInfo.sex;

	private AlertDialog dialog;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		touxiang = getView(R.id.touxiang);
		head_rel = getView(R.id.head_rel);
		name_rel = getView(R.id.name_rel);
		sex_rel = getView(R.id.sex_rel);
		birth_rel = getView(R.id.birth_rel);
		phone_rel = getView(R.id.phone_rel);
		addr_rel = getView(R.id.addr_rel);
		qm_rel = getView(R.id.qm_rel);
		name_text = getView(R.id.name_text);
		sex_text = getView(R.id.sex_text);
		birth_text = getView(R.id.birth_text);
		phone_text = getView(R.id.phone_text);
		addr_text = getView(R.id.addr_text);
		qm_text = getView(R.id.qm_text);

		back = getView(R.id.back);
		setting = getView(R.id.setting);
		setting.setVisibility(View.GONE);
		change_head_text = getView(R.id.change_head_text);
		back.setOnClickListener(this);
		// setting.setOnClickListener(this);
		change_head_text.setOnClickListener(this);
		name_rel.setOnClickListener(this);
		sex_rel.setOnClickListener(this);
		birth_rel.setOnClickListener(this);
		phone_rel.setOnClickListener(this);
		addr_rel.setOnClickListener(this);
		qm_rel.setOnClickListener(this);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.mipmap.ceshi);
		Drawable drawable = BitmapBlur.BoxBlurFilter(bitmap);
		head_rel.setBackgroundDrawable(drawable);
		setinfo();
		initDate(birth_text);
	}
	private void setinfo() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(MyApplication.userInfo.image)) {
			Log.v("setinfo","MyApplication.userInfo.image="+MyApplication.userInfo.image);
			final File file = new File(Environment.getExternalStorageDirectory()
					+ "/zzkt/"
					+ MyApplication.userInfo.image.replace("\\/", "/")
							.substring(
									MyApplication.userInfo.image.replace("\\/",
											"/").lastIndexOf("/")));
			if (!file.exists()) {
				String downLoadUrl=MyApplication.userInfo.image.contains("http://")?MyApplication.userInfo.image.replace("\\/", "/"):Config1.IP
						+ MyApplication.userInfo.image.replace("\\/", "/");
				ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
				zzktHttpDownLoad.downloads(downLoadUrl,
						Environment.getExternalStorageDirectory()
								+ "/zzkt/"
								+ MyApplication.userInfo.image.replace("\\/",
								"/").substring(
								MyApplication.userInfo.image.replace(
										"\\/", "/").lastIndexOf("/")),
						new Callback() {
							@Override
							public void onFailure(Call call, IOException e) {

							}

							@Override
							public void onResponse(Call call, Response response) throws IOException {
								Message message=Message.obtain();
								message.what=7;
								message.obj=file;
								handlerReq.sendMessage(message);
							}
						});

			} else {
				// Bitmap bitmap = BitmapFactory
				// .decodeFile(file.getAbsolutePath());
				Bitmap bitmap = HttpImage.ratio(file.getAbsolutePath(), 320,
						240);
				touxiang.setImageBitmap(bitmap);
				Drawable drawable = BitmapBlur.BoxBlurFilter(bitmap);
				head_rel.setBackgroundDrawable(drawable);
			}
		}
		name_text.setText(MyApplication.userInfo.realname);
		sex_text.setText(MyApplication.userInfo.sex == 1 ? "女" : "男");
		birth_text.setText(MyApplication.userInfo.birth);
		phone_text.setText(MyApplication.userInfo.tel);
		addr_text.setText(MyApplication.userInfo.hometown);
		qm_text.setText(MyApplication.userInfo.signature);
	}

	private void setinfo2() {
		// TODO Auto-generated method stub
		switch (changetype) {
		case 1:
			Log.v("setinfo2","MyApplication.userInfo.image="+MyApplication.userInfo.image);
			OkHttpClient okHttpClient=new OkHttpClient();
			final File file = new File(Environment.getExternalStorageDirectory()
					+ "/zzkt/"
					+ MyApplication.userInfo.image.replace("\\/", "/")
					.substring(
							MyApplication.userInfo.image.replace("\\/",
									"/").lastIndexOf("/")));
			if (!file.exists()) {
				ZZKTHttpDownLoad zzktHttpDownLoad = new ZZKTHttpDownLoad();
				String downLoadUrl = MyApplication.userInfo.image.contains("http://") ? MyApplication.userInfo.image.replace("\\/", "/") : Config1.IP
						+ MyApplication.userInfo.image.replace("\\/", "/");
				zzktHttpDownLoad.downloads(downLoadUrl,
						Environment.getExternalStorageDirectory()
								+ "/zzkt/"
								+ MyApplication.userInfo.image.replace("\\/", "/")
								.substring(
										MyApplication.userInfo.image
												.replace("\\/", "/")
												.lastIndexOf("/")),
						new Callback() {
							@Override
							public void onFailure(Call call, IOException e) {
								handlerReq.sendEmptyMessage(6);
							}

							@Override
							public void onResponse(Call call, Response response) throws IOException {
								Message message = Message.obtain();
								message.what = 7;
								message.obj = file;
								handlerReq.sendMessage(message);
							}
						});
			}else {
				Bitmap bitmap = HttpImage.ratio(file.getAbsolutePath(), 320,
						240);
				touxiang.setImageBitmap(bitmap);
				Drawable drawable = BitmapBlur.BoxBlurFilter(bitmap);
				head_rel.setBackgroundDrawable(drawable);
			}
			break;
		case 3:
			sex_text.setText(MyApplication.userInfo.sex == 1 ? "女" : "男");
			break;
		case 4:
			birth_text.setText(MyApplication.userInfo.birth);
			break;
		case 5:
			phone_text.setText(MyApplication.userInfo.tel);
			break;
		case 6:
			addr_text.setText(MyApplication.userInfo.hometown);
			break;
		case 7:
			qm_text.setText(MyApplication.userInfo.signature);
			break;

		default:
			break;
		}
		changetype = 0;
		changeText = "";
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject object = JSON.parseObject(
						(String) msg.obj);
				if (object.getInteger("code")==1000){
					MyApplication.userInfo= JSON.parseObject(object.getString("data"),UserInfo.class);
					changetype = 1;
					setinfo2();
				}else {
					toast(object.getString("message"));
				}

			}else if (msg.what==2){

			}else if (msg.what==3){
				switch (changetype) {
					case 3:
						MyApplication.userInfo.sex = Integer
								.parseInt((String) msg.obj);
					case 4:
						MyApplication.userInfo.birth = (String) msg.obj;
						break;
					case 5:
						MyApplication.userInfo.tel = (String) msg.obj;
						break;
					case 6:
						MyApplication.userInfo.hometown = (String) msg.obj;
						break;
					case 7:
						MyApplication.userInfo.signature = (String) msg.obj;
						break;

					default:
						break;
				}
				setinfo2();
			}else  if (msg.what==4){
			}else  if (msg.what==5){
				requestData();
			}else  if (msg.what==6){

			}else  if (msg.what==7){
				File file = (File) msg.obj;
				Bitmap bitmap = BitmapFactory.decodeFile(file
						.getAbsolutePath());
				touxiang.setImageBitmap(bitmap);
				Drawable drawable = BitmapBlur
						.BoxBlurFilter(bitmap);
				head_rel.setBackgroundDrawable(drawable);
			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", MyApplication.userInfo.id);
		doGet(Config1.getInstance().PERSONINFO(), map, new ResultCallback() {
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

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_my_info;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.setting:
			// startActivity(new Intent(MyInfoActicity.this,
			// SettingActicity.class));
			break;
		case R.id.change_head_text:// 更改头像
			showPop();
			break;
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
			intent = new Intent(
					// 相册
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, RESULT_LOAD_IMAGE);
			break;
		case R.id.quxiao:// 取消
			if (pop != null) {
				pop.dismiss();
			}
			break;
		case R.id.sex_rel:// 性别
			changetype = 3;
			changeText = "sex";
			Builder builder3 = new Builder(this);
			builder3.setTitle("性别");
			// 给对话框设定单选列表项
			// 参数一： 选项的值 参数二： 默认选中项的索引 参数三：监测选中的项的监听器
			builder3.setSingleChoiceItems(new String[] { "男", "女" },
					MyApplication.userInfo.sex == 1 ? 1 : 0,
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 参数一：对话框对象 参数二： 选中的列表项的索引
							// Toast.makeText(MyActicity.this, "选中： " + which,
							// 0).show();
							switch (which) {
							case 0:
								if (MyApplication.userInfo.sex == 1) {
									submitChange(0 + "");
								}
								break;
							case 1:
								if (MyApplication.userInfo.sex == 0) {
									submitChange(1 + "");
								}
								break;

							default:
								break;
							}
							dialog.dismiss();
						}
					});
			dialog = builder3.show();
			dialog.show();
			break;
		case R.id.birth_rel:// 生日
			changetype = 4;
			changeText = "birth";
			// TODO Auto-generated method stub
			initDateView(birth_text);
			PopupWindow = PopupWindowUtils.showCamerPopupWindow(dateSelected,
					R.layout.activity_week_plan, MyInfoActicity.this);
			break;
		case R.id.phone_rel:// 手机
			changetype = 5;
			changeText = "tel";
			intent = new Intent(MyInfoActicity.this,
					ChangePersonInfoActivity.class);
			intent.putExtra("changetype", changetype);
			intent.putExtra("name", MyApplication.userInfo.tel);
			startActivityForResult(intent, 500);

			break;
		case R.id.addr_rel:// 地址
			changetype = 6;
			changeText = "hometown";
			intent = new Intent(MyInfoActicity.this,
					ChangePersonInfoActivity.class);
			intent.putExtra("changetype", changetype);
			intent.putExtra("name", MyApplication.userInfo.hometown);
			startActivityForResult(intent, 500);

			break;
		case R.id.qm_rel:// 签名
			changetype = 7;
			changeText = "signature";
			intent = new Intent(MyInfoActicity.this,
					ChangePersonInfoActivity.class);
			intent.putExtra("changetype", changetype);
			intent.putExtra("name", MyApplication.userInfo.signature);
			startActivityForResult(intent, 500);
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
				// toast(path + "");
				Log.v("TAG", path + "");
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
		case 500:
			if (data != null) {
				String backName = data.getStringExtra("name");
				if (resultCode == 605) {
					if (backName != null
							&& backName.equals(MyApplication.userInfo.tel)) {
						changetype = 0;
						changeText = "";
					} else {
						submitChange(backName);
					}
				} else if (resultCode == 606) {
					if (backName != null
							&& backName.equals(MyApplication.userInfo.hometown)) {
						changetype = 0;
						changeText = "";
					} else {
						submitChange(backName);
					}

				} else if (resultCode == 607) {
					if (backName != null
							&& backName
									.equals(MyApplication.userInfo.signature)) {
						changetype = 0;
						changeText = "";
					} else {
						submitChange(backName);
					}
				}
			}
			break;
		}
	}

	private void submitChange(final String backName) {
		// TODO Auto-generated method stub
		Log.v("TAG", "changetype:" + changetype + ",changeText:" + changeText
				+ ",keyValue:" + backName);
		Map<String,Object> map=new HashMap<>();
		map.put("userId", MyApplication.userInfo.id + "");
		map.put("infoKey", changeText);
		map.put("keyValue", backName);
		doPost(Config1.getInstance().UPDATEUSERINFO(), map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
							handlerReq.sendEmptyMessage(4);
					}

					@Override
					public void onResponse(Call call, Response response,String json) {
						Message message=Message.obtain();
						message.what=3;
						message.obj=backName;
						handlerReq.sendMessage(message);
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String a = (String) msg.obj;
				Map<String,Object> map=new HashMap<>();
				map.put("userId", MyApplication.userInfo.id
						+ "");
				map.put("infoKey", "image");
				map.put("keyValue",
						a.substring(a.indexOf("link") + 7, a.indexOf(",") - 1));
				doPost(Config1.getInstance().UPDATEUSERINFO(), map,
						new ResultCallback() {
							@Override
							public void onFailure(Call call, IOException e) {
								handlerReq.sendEmptyMessage(6);
							}

							@Override
							public void onResponse(Call call, Response response,String json){
								Message message=Message.obtain();
								message.what=5;
								message.obj=json;
								handlerReq.sendMessage(message);

							}
						});
			} else if (msg.what == 2) {
				showRoundProcessDialog(MyInfoActicity.this);
			} else if (msg.what == 3) {
				showRoundProcessDialogCancel();
			}else if (msg.what == 4) {

			}else if (msg.what == 5) {

			}else if (msg.what == 6) {

			}
		}
	};

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
			String address = sDateFormat.format(new Date());
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

	// -----------------------------------------------
	private void initDateView(final TextView text) {
		// TODO Auto-generated method stub
		// 时间选择器
		layoutInflater = LayoutInflater.from(this);
		dateSelected = layoutInflater.inflate(
				R.layout.appointment_dateselect_windowview, null);
		// 选择开始时间
		dateSelected.findViewById(R.id.button_cacel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (PopupWindow != null) {
							PopupWindow.dismiss();
						}
					}
				});
		dateSelected.findViewById(R.id.button_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						updateTime(text);
						if (PopupWindow != null) {
							PopupWindow.dismiss();
						}
					}
				});
		date_year = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_year);
		date_month = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_month);
		date_day = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_day);
		initData();
		setData();
	}

	// --------------------------时间选择器----------------
	private String dateString = "";
	private PopupWindow PopupWindow;// 弹出的时间选择框
	private View dateSelected;
	private LayoutInflater layoutInflater;
	private int year, month, day;
	private PickerView date_year, date_month, date_day;
	// private PickerView date_hour, date_minute;
	private List<String> years, months, days_b, days_m, days_s, days_ss, hours,
			minutes;

	// private TextView selected_date;// 展示时间的控件

	private void initData() {
		// 年的数组
		years = new ArrayList<String>();
		for (int i = 1949; i < 2100; i++) {
			years.add(i + "年");
		}
		// 月的数组
		months = new ArrayList<String>();
		for (int i = 1; i < 13; i++) {
			months.add(i < 10 ? "0" + i + "月" : i + "月");
		}
		// 日的数组（31）
		days_b = new ArrayList<String>();
		days_m = new ArrayList<String>();
		days_s = new ArrayList<String>();
		days_ss = new ArrayList<String>();
		for (int i = 1; i < 32; i++) {
			days_b.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			if (i < 31) {
				days_m.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			}
			if (i < 30) {
				days_s.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			}
			if (i < 29) {
				days_ss.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			}
		}

		hours = new ArrayList<String>();
		for (int i = 0; i <= 23; i++) {
			hours.add(i < 10 ? "0" + i : "" + i);
		}

		minutes = new ArrayList<String>();
		for (int i = 0; i <= 59; i++) {
			minutes.add(i < 10 ? "0" + i : "" + i);
		}
	}

	private void setData() {
		date_year.setData(years);
		date_year.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				year = Integer.parseInt(text.substring(0, 4));
				// updateTime(selected_date);
			}
		});
		date_month.setData(months);
		date_month.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				month = Integer.parseInt(text.substring(0, 2));
				if (year % 4 != 0 && year % 400 != 0) {
					if (month == 2) {
						date_day.setData(days_s);
					} else if (month == 4 || month == 6 || month == 9
							|| month == 11) {
						date_day.setData(days_m);
					} else {
						date_day.setData(days_b);
					}
				} else {
					if (month == 2) {
						date_day.setData(days_ss);
					} else if (month == 4 || month == 6 || month == 9
							|| month == 11) {
						date_day.setData(days_m);
					} else {
						date_day.setData(days_b);
					}
				}
				date_day.setSelected(day + "");
				// updateTime(selected_date);
			}
		});
		date_day.setData(days_b);
		date_day.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				day = Integer.parseInt(text.substring(0, 2));
				// updateTime(selected_date);
			}
		});
		// 设置PickerView的起始时间
		date_year.setSelected(year - 1949);
		date_month.setSelected(month - 1);
		date_day.setSelected(day - 1);
		// date_hour.setSelected(hour - 1);
		// date_minute.setSelected(minute - 1);
	}

	public void updateTime(TextView tv) {
		String month_ = "";
		if (month < 10) {
			month_ = "0" + month;
		} else {
			month_ = "" + month;
		}

		String day_ = "";
		if (day < 10) {
			day_ = "0" + day;
		} else {
			day_ = "" + day;
		}

		// tv.setText(year + "年" + month_ + "月" + day_ + "日");
		dateString = year + "-" + month_ + "-" + day_;
		Log.v("TAG", "b:" + dateString);
		submitChange(dateString);
	}

	/**
	 * 获取系统的当前时间并赋值给TextView
	 */
	private void initDate(TextView text) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			dateString = MyApplication.userInfo.birth;
			if (TextUtils.isEmpty(dateString)) {
				dateString = format.format(new Date());
			}
			date = format.parse(dateString);
			String strdate = format.format(date);
			text.setText(strdate);
			// 分别获取当前时间的年月日
			SimpleDateFormat format_year = new SimpleDateFormat("yyyy");
			SimpleDateFormat format_moth = new SimpleDateFormat("MM");
			SimpleDateFormat format_day = new SimpleDateFormat("dd");
			// SimpleDateFormat format_hour = new SimpleDateFormat("hh");
			// SimpleDateFormat format_minute = new SimpleDateFormat("mm");
			year = Integer.parseInt(format_year.format(date));
			month = Integer.parseInt(format_moth.format(date));
			day = Integer.parseInt(format_day.format(date));
			// hour = Integer.parseInt(format_hour.format(date));
			// minute = Integer.parseInt(format_minute.format(date));
			String selectDate = MyApplication.userInfo.birth;
			Log.v("TAG", "b:" + selectDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * @return转换时间为服务器所需要的类型
	 */
	public String switchTime() {
		String str = year + "/" + month + "/" + day;
		return str;
	}
}
