package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.JsonControl;
import com.shengliedu.teacher.teacher.bean.JsonControlData;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.FileUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;
import com.shengliedu.teacher.teacher.util.UploadUtil;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkShowAddActivity extends BaseActivity implements
		OnClickListener {
	private TextView d, p, l;
	private ImageView i;
	private String realname;
	private int userId;
	private int activityId;
	private ImageLoader imageLoader;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		imageLoader=ImageLoader.getInstance();
		realname = getIntent().getStringExtra("realname");
		userId = getIntent().getIntExtra("userId", -1);
		activityId = getIntent().getIntExtra("activityId", -1);
		setBack();
		showTitle(realname);
		d = getView(R.id.d);
		p = getView(R.id.p);
		l = getView(R.id.l);
		i = getView(R.id.i);
		d.setOnClickListener(this);
		p.setOnClickListener(this);
		l.setOnClickListener(this);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homework_show_add;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.d:
			i.setImageBitmap(null);
			photoUri = null;
			break;
		case R.id.p:
			photo();
			break;
		case R.id.l:
			new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					if (photoUri!=null&&!TextUtils.isEmpty(photoUri.toString())) {
						Log.v("TAG", photoUri.toString());
						String a = "";
						if (photoUri.getPath().endsWith(".jpg")
								|| photoUri.getPath().endsWith(".JPG")
								|| photoUri.getPath().endsWith(".JPEG")
								|| photoUri.getPath().endsWith(".jpeg")
								|| photoUri.getPath().endsWith(".png")) {
							File file = new File(photoUri.getPath());
							Log.v("TAG", photoUri.getPath().toString());
							handler.sendEmptyMessage(10);
							UploadUtil u = new UploadUtil();
							a = u.uploadFile(file, Config1.getInstance()
									.FILEUPLOAD(), activityId + "", userId + "");
							Log.v("TAG", "a=" + a);
							handler.sendEmptyMessage(11);
						}
						try {
							org.json.JSONObject json = new org.json.JSONObject(
									a);
							Message message = Message.obtain();
							message.obj = json.getString("link");
							message.what = 0;
							handler.sendMessage(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						handler.sendEmptyMessage(12);
					}
				}
			}.start();
			break;

		default:
			break;
		}
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				toast("上传成功！");
				copyHomework();
				setResult(601);
				if (MyApplication.chatLogin) {
					JsonControl jsonControl=new JsonControl();
					jsonControl.date=new Date().getTime();
					jsonControl.type="HomeworkShow";
					jsonControl.data=new JsonControlData();
					jsonControl.data.studentid=userId;
					jsonControl.data.link= (String) msg.obj;
					jsonControl.data.key=0;
					try {
						XmppConnection.getInstance().setRecevier(SharedPreferenceTool
								.getUserName(HomeworkShowAddActivity.this)+"slDispatchServerUser", ChatItem.CHAT);
						Log.v("TAG", "j="+JSON.toJSONString(jsonControl));
						XmppConnection.getInstance().sendMsg(JSON.toJSONString(jsonControl), ChatItem.CHAT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					loginAccount(SharedPreferenceTool
							.getUserName(HomeworkShowAddActivity.this), SharedPreferenceTool
							.getPassword(HomeworkShowAddActivity.this),(String) (msg.obj));
				}
			}else if (msg.what==2){

			}
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			if (msg.what == 0) {
				final String showImage=(String) msg.obj;
				Map<String,Object> map=new HashMap<>();
				map.put("activityId", activityId + "");
				map.put("userId", userId + "");
				map.put("showImage", (String) (msg.obj));
				Log.v("TAG", "activityId:" + activityId + ",userId:" + userId
						+ ",showImage:" + msg.obj);
				doPost(Config1.getInstance().HOMEWORK_SHOW(), map,
						new ResultCallback() {
							@Override
							public void onFailure(Call call, IOException e) {
								handlerReq.sendEmptyMessage(2);
							}

							@Override
							public void onResponse(Call call, Response response,String json){
								Message message=Message.obtain();
								message.what=1;
								message.obj=showImage;
								handlerReq.sendMessage(message);
							}
						});
			} else if (msg.what == 10) {
				showRoundProcessDialog(HomeworkShowAddActivity.this);
			} else if (msg.what == 11) {
				showRoundProcessDialogCancel();
			} else if (msg.what == 12) {
				toast("图片为空");
			}
		}
	};
	private void copyHomework(){
		String file0 = Environment
				.getExternalStorageDirectory().getPath() + "/zzkt/homeworkshow/";
		File fileDirHomework = new File(file0);
		if (!fileDirHomework.exists()) {
			fileDirHomework.mkdirs();
		}
		SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
		String    str    =    formatter.format(curDate);
		String file1 = Environment
				.getExternalStorageDirectory().getPath() + "/zzkt/homeworkshow/"+MyApplication.userInfo.id+"_"+str+"/";
		File fileDirHomeworkDate = new File(file1);
		if (!fileDirHomeworkDate.exists()) {
			fileDirHomeworkDate.mkdirs();
		}
		if (photoUri!=null){
			String filesave = Environment
					.getExternalStorageDirectory().getPath() + "/zzkt/homeworkshow/"+MyApplication.userInfo.id+"_"+str+"/"+photoUri.getPath().substring(photoUri.getPath().lastIndexOf("/")+1)+"";
			FileUtils.copyFile(photoUri.getPath(),filesave);
		}
	}
	private static final int TAKE_PICTURE = 0;
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
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 0:
			Log.v("TAG", "照片返回"+photoUri);
			if (photoUri!=null&&!TextUtils.isEmpty(photoUri.getPath())) {
				imageLoader.displayImage("file:/"+photoUri.getPath(), i);
			}
			break;

		default:
			break;
		}
	}
	private void loginAccount(final String userName, final String password,final String msg) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				Log.v("TAG", "userName="+userName);
				Log.v("TAG", "password="+password);
				boolean isSuccess = XmppConnection.getInstance().login(
						userName, password);
				Log.v("TAG", "isSuccess="+isSuccess);
				if (isSuccess) {
					Constants.USER_NAME = userName;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				MyApplication.chatLogin=isSuccess;
				if (isSuccess) {
					JsonControl jsonControl=new JsonControl();
					jsonControl.date=new Date().getTime();
					jsonControl.type="HomeworkShow";
					jsonControl.data=new JsonControlData();
					jsonControl.data.studentid=userId;
					jsonControl.data.link=msg;
					jsonControl.data.key=0;
					try {
						XmppConnection.getInstance().setRecevier(SharedPreferenceTool
								.getUserName(HomeworkShowAddActivity.this)+"slDispatchServerUser", ChatItem.CHAT);
						XmppConnection.getInstance().sendMsg(JSON.toJSONString(jsonControl), ChatItem.CHAT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					toast("服务器连接失败");
				}
			}

		};
	}
}
