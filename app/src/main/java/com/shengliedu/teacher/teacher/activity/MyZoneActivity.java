package com.shengliedu.teacher.teacher.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.MyZoneAdapter;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.CallOtherOpeanFile;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.FileUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.UploadUtil;
import com.shengliedu.teacher.teacher.util.ZZKTHttpDownLoad;
import com.shengliedu.teacher.teacher.view.CustomDialog;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyZoneActivity extends BaseActivity {
	private RadioGroup my_zone_rg;
	private ListView my_zone_list;
	private List<IdName> fileArr = new ArrayList<IdName>();
	private MyZoneAdapter adapter;

	private static final int FILE_SELECT_CODE = 1;
	private String path;
	private int typeId;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("我的空间");
		setRightImage(R.mipmap.add_r, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMenu();
			}
		});
		my_zone_list = getView(R.id.my_zone_list);
		adapter = new MyZoneAdapter(this, fileArr);
		my_zone_list.setAdapter(adapter);
		my_zone_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> arg0, View arg1, final int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(fileArr.get(arg2).location)) {

					CallOtherOpeanFile c = new CallOtherOpeanFile();
					String mimeType = c.getMIMETypeForString(fileArr.get(arg2).location);
					Log.v("TAG", "mimeType:" + mimeType);
					if (fileArr.get(arg2).location.toLowerCase()
							.endsWith("doc")
							|| fileArr.get(arg2).location.toLowerCase()
									.endsWith("docx")
							|| fileArr.get(arg2).location.toLowerCase()
									.endsWith("ppt")
							|| fileArr.get(arg2).location.toLowerCase()
									.endsWith("pptx")
							|| fileArr.get(arg2).location.toLowerCase()
									.endsWith("xls")
							|| fileArr.get(arg2).location.toLowerCase()
									.endsWith("xlsx")
							|| fileArr.get(arg2).location.toLowerCase()
									.endsWith("apk")) {
						final ProgressDialog pd = new ProgressDialog(context);
						pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pd.setCancelable(true);
						pd.setCanceledOnTouchOutside(false);
						pd.setMessage("正在下载...");

						File file = new File(
								Environment.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/zzkt/teacher/"
										+ fileArr.get(arg2).location
												.substring(fileArr.get(arg2).location
														.lastIndexOf("/") + 1));
						if (!file.exists()) {
							pd.show();
							ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
							zzktHttpDownLoad.downloads(
									Config1.getInstance().IP
											+ fileArr.get(arg2).location
											.replace("\\/", "/"),
									Environment.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/zzkt/teacher/"
											+ fileArr.get(arg2).location.substring(fileArr
											.get(arg2).location
											.lastIndexOf("/") + 1),
									new Callback() {
										@Override
										public void onFailure(Call call, IOException e) {

										}

										@Override
										public void onResponse(Call call, Response response) throws IOException {
											CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
											callOtherOpeanFile.openFile(
													context, new File(Environment.getExternalStorageDirectory()
															.getAbsolutePath()
															+ "/zzkt/teacher/"
															+ fileArr.get(arg2).location.substring(fileArr
															.get(arg2).location
															.lastIndexOf("/") + 1)));
										}
									});

						} else {
							CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
							callOtherOpeanFile.openFile(context, file);
						}
					} else {
						try {
							Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
							Log.v("TAG",
									"url:"
											+ Config1.getInstance().IP
											+ fileArr.get(arg2).location
													.replace("\\/", "/"));
							mediaIntent.setDataAndType(
									Uri.parse(Config1.getInstance().IP
											+ fileArr.get(arg2).location
													.replace("\\/", "/")),
									mimeType);
							startActivity(mediaIntent);
						} catch (ActivityNotFoundException e) {
							// TODO: handle exception
							toast("sorry附件不能打开，请下载相关软件！");
						}
					}
				}
			}
		});
		my_zone_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (fileArr != null) {
					CustomDialog.Builder customDialog = new CustomDialog.Builder(
							context);
					customDialog.setMessage("亲,是否删除该文件?");
					customDialog.setTitle("提示");
					customDialog.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog, int which) {
									// 设置你的操作事项
									dialog.dismiss();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("id", fileArr.get(arg2).id);
									doGet(Config1.getInstance()
													.MYZONE_FILE_DELETE(), map,
											new ResultCallback() {
												@Override
												public void onFailure(Call call, IOException e) {

												}

												@Override
												public void onResponse(Call call, Response response,String json) {
													toast("删除成功");
													getDatas();
												}
											});
								}
							});
					customDialog.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					customDialog.create().show();
				}
				return false;
			}
		});
	}

	/**
	 * 弹出顶部menu
	 */
	private PopupWindow popupWindow;

	private void showMenu() {
		// TODO Auto-generated method stub
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.layout_popmenu2, null, false);
		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果
		// popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 这里是位置显示方式,在屏幕的左侧
		// popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		popupWindow.showAsDropDown(findViewById(R.id.ctitle), screenwidth, 0);
		popupWindow_view.findViewById(R.id.wendang).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						typeId = 1;
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("*/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(Intent.createChooser(intent,
									"Select a File to Upload"),
									FILE_SELECT_CODE);
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(MyZoneActivity.this,
									"Please install a File Manager.",
									Toast.LENGTH_SHORT).show();
						}
						if (popupWindow != null) {
							popupWindow.dismiss();
						}
					}
				});
		popupWindow_view.findViewById(R.id.tupian).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						typeId = 2;
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("*/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(Intent.createChooser(intent,
									"Select a File to Upload"),
									FILE_SELECT_CODE);
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(MyZoneActivity.this,
									"Please install a File Manager.",
									Toast.LENGTH_SHORT).show();
						}
						if (popupWindow != null) {
							popupWindow.dismiss();
						}
					}
				});
		popupWindow_view.findViewById(R.id.yinpin).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						typeId = 3;
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("*/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(Intent.createChooser(intent,
									"Select a File to Upload"),
									FILE_SELECT_CODE);
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(MyZoneActivity.this,
									"Please install a File Manager.",
									Toast.LENGTH_SHORT).show();
						}
						if (popupWindow != null) {
							popupWindow.dismiss();
						}
					}
				});
		popupWindow_view.findViewById(R.id.shipin).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						typeId = 4;
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("*/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(Intent.createChooser(intent,
									"Select a File to Upload"),
									FILE_SELECT_CODE);
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(MyZoneActivity.this,
									"Please install a File Manager.",
									Toast.LENGTH_SHORT).show();
						}
						if (popupWindow != null) {
							popupWindow.dismiss();
						}
					}
				});
		popupWindow_view.findViewById(R.id.qita).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						typeId = 5;
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("*/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(Intent.createChooser(intent,
									"Select a File to Upload"),
									FILE_SELECT_CODE);
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(MyZoneActivity.this,
									"Please install a File Manager.",
									Toast.LENGTH_SHORT).show();
						}
						if (popupWindow != null) {
							popupWindow.dismiss();
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("TAG", "requestCode=" + requestCode + ",resultCode=" + resultCode
				+ ",data:" + (data == null));
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				// Get the Uri of the selected file
				Uri uri = data.getData();
				Log.v("TAG", "uri=" + uri);
				Log.v("TAG", "uriP=" + uri.getPath());
				path = FileUtils.getImageAbsolutePath(this, uri);
				// if (uri != null) {
				// String[] proj = {MediaStore.Images.Media.DATA };
				// Cursor actualimagecursor =
				// MyZoneActivity.this.managedQuery(uri,
				// proj, null, null, null);
				// int actual_image_column_index = actualimagecursor
				// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// actualimagecursor.moveToFirst();
				// path = actualimagecursor
				// .getString(actual_image_column_index);
				// }
				Log.v("TAG", "path=" + path);
				if (!TextUtils.isEmpty(path)) {
					final ProgressDialog pd = new ProgressDialog(context);
					pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pd.setCancelable(true);
					pd.setCanceledOnTouchOutside(false);
					pd.setMessage("正在上传...");
					pd.show();
					Log.v("TAG", "path1=" + path);
					final File file = new File(path);
					pd.setMax(Integer.parseInt(file.length() + ""));
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							UploadUtil u = new UploadUtil();
							Log.v("UUU", Config1
									.getInstance().FILEUPLOAD());
							String a = u.uploadFile2(file, Config1
									.getInstance().FILEUPLOAD(),
									MyApplication.userInfo.id + "",
									typeId + "", pd);
							pd.dismiss();
							if (!TextUtils.isEmpty(a)) {
								Message message = Message.obtain();
								message.obj = a;
								message.what = 1;
								handler.sendMessage(message);
							}
						}
					}.start();
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String a = (String) msg.obj;
				try {
					org.json.JSONObject object = new org.json.JSONObject(a);

					// String link = a.substring(a.indexOf("link") + 7,
					// a.indexOf(",") - 1).replace("\\/", "/");
					String link = object.getString("link").replace("\\/", "/");
					int size = object.getInt("size");
					Log.v("TAG", "link=" + link + ",size=" + size);
					Map<String,Object> map=new HashMap<>();
					map.put("userId", MyApplication.userInfo.id
							+ "");
					map.put("typeId", typeId + "");
					map.put("location", link);
					map.put("fileSize", size + "");
					map.put("name",
							link.substring(link.lastIndexOf("/")));
					doPost(Config1.getInstance().MYZONE_FILE_ADD(), map,
							new ResultCallback() {
								@Override
								public void onFailure(Call call, IOException e) {

								}

								@Override
								public void onResponse(Call call, Response response,String json) {
	if (response.code()==200){
		getDatas();
	}
								}
							});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (msg.what == 2) {

			}
		}
	};
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject jsonObject=JSON.parseObject((String) msg.obj);
				final List<IdName> alldata = JSON.parseArray(
						jsonObject.getString("data"), IdName.class);
				if (!MyApplication.listIsEmpty(alldata)) {
					my_zone_rg = getView(R.id.my_zone_rg);
					my_zone_rg.removeAllViews();
					for (int i = 0; i < alldata.size(); i++) {
						RadioButton button = (RadioButton) LayoutInflater.from(
								MyZoneActivity.this).inflate(
								R.layout.radio_without_button, null);
						button.setText(alldata.get(i).name);
						// button.setTag(i);
						button.setId(i);
						// button.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						// 20,
						// 0);
						my_zone_rg.addView(button);
					}
					my_zone_rg.check(0);
					fileArr.clear();
					fileArr.addAll(alldata.get(0).fileArr);
					System.out.println("cece:" + fileArr.toString());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							adapter.notifyDataSetChanged();
						}
					});
					my_zone_rg
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(RadioGroup arg0,
															 int arg1) {
									// TODO Auto-generated method stub
									fileArr.clear();
									fileArr.addAll(alldata.get(arg1).fileArr);
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											adapter.notifyDataSetChanged();
										}
									});
								}
							});
				} else {
					toast("暂无数据");
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", MyApplication.userInfo.id);
		doGet(Config1.getInstance().MYZONE_FILE(), map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json) {
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
		return R.layout.activity_my_zone;
	}

}
