package com.shengliedu.teacher.teacher.chat.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.MainActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.BeanYunIp;
import com.shengliedu.teacher.teacher.bean.UserInfo;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.util.MyAndroidUtil;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.AppValue;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.MD5Util;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SpringBoardActivity extends BaseActivity {
	private String name, pwd;
	private void login() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("platform", "1");
		map.put("devtype", "1");
		map.put("password", MD5Util.getMD5String(pwd).toUpperCase());
		doGet(Config1.MAIN_DC, map,
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

	private void loginAccount(final String userName, final String password) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				boolean isSuccess = XmppConnection.getInstance().login(
						userName, password);
				if (isSuccess) {
					Constants.USER_NAME = name;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				MyApplication.chatLogin=isSuccess;
				if (isSuccess) {
						MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT,
								name);
						MyAndroidUtil.editXmlByString(Constants.LOGIN_PWD, pwd);
					Intent intent = new Intent();
					intent.putExtra("current", 1);
					intent.setClass(SpringBoardActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
			}

		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getIntent().getBooleanExtra("isRelogin", false)) {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		// 已登录过,自动登录
		name = MyApplication.sharedPreferences.getString(
				Constants.LOGIN_ACCOUNT, null);
		pwd = MyApplication.sharedPreferences.getString(Constants.LOGIN_PWD,
				null);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Log.v("TAG", "MyApplication.userInfo:::"+(MyApplication.userInfo!=null));
		Log.v("TAG", "isRunningApp:::"+(AppValue.isRunningApp(MyApplication.getInstance(),MyApplication.getInstance().getPackageName())));
		if (MyApplication.isRun) {
			Intent intent = new Intent(); // 要跳去的界面
			intent.putExtra("current", 1);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(SpringBoardActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}else {
			login();
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.acti_jump;
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				BeanYunIp yunIp = JSON.parseObject((String) msg.obj,
						BeanYunIp.class);
				if (yunIp != null) {
					MyApplication.beanSchoolInfo=yunIp;
					if (!TextUtils.isEmpty(yunIp.ip)) {
						Log.v("TAG", "yunIp.ip=" + yunIp.ip);
						Config1.getInstance().IP = "http://" + yunIp.ip + "/";
						// Config1.getInstance().IP =
						// "http://192.168.1.207/";
						// Config1.getInstance().IP =
						// "http://192.168.1.50/";
						// Constants.SERVER_HOST = yunIp.ip;
						if (yunIp.ip.contains(":20780")) {
							Constants.SERVER_HOST = "192.168.1.207";
							Constants.SERVER_NAME = "192.168.1.207";
						}
						// Constants.SERVER_HOST = "192.168.1.50";
						Constants.init();
						// Constants.SERVER_URL =
						// "http://"+Constants.SERVER_HOST+":9090/plugins/xmppservice/";
						Log.v("TAG", "Config1.IP=" + Config1.getInstance().IP);
						Log.v("TAG", "Config1.LOGIN="
								+ Config1.getInstance().LOGIN());
						Map<String,Object> map=new HashMap<String, Object>();
						map.put("name", name);
						map.put("password", MD5Util
								.getMD5String(pwd).toUpperCase());
						map.put("platform", "1");
						map.put("devtype", "1");
						doPost(Config1.getInstance().LOGIN(), map,
								new ResultCallback() {
									@Override
									public void onFailure(Call call, IOException e) {
										handlerReq.sendEmptyMessage(2);
									}

									@Override
									public void onResponse(Call call, Response response,String json)  {
										Message message=Message.obtain();
										message.what=3;
										message.obj=json;
										handlerReq.sendMessage(message);
									}
								});
					} else {
					}
				} else {
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				JSONObject object=JSON.parseObject((String) msg.obj);
				MyApplication.userInfo = JSON
						.parseObject(object
										.getString("data"),
								UserInfo.class);
				if (MyApplication.userInfo != null) {
					Log.v("TAG", "loginuser:"
							+ MyApplication.userInfo.id);
					if (MyApplication.userInfo.type == 1) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								MyApplication.dealLoginInfo();
							}
						}).start();
						SharedPreferenceTool
								.setUserName(
										SpringBoardActivity.this,
										MyApplication.userInfo.name);
						SharedPreferenceTool
								.setPassword(
										SpringBoardActivity.this,
										pwd);
						loginAccount(MyApplication.userInfo.name, pwd);
					}else {
						toast("您不是教师！");
					}
				}

			}else if (msg.what==4){

			}
		}
	};
}
