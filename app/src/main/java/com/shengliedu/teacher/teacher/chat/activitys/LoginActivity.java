/**
 * 
 */
package com.shengliedu.teacher.teacher.chat.activitys;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

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
import com.shengliedu.teacher.teacher.chat.util.Tool;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.MD5Util;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zt10
 * 
 */
public class LoginActivity extends BaseActivity implements
		OnCheckedChangeListener, TextWatcher, OnClickListener {
	private Button loginBtn;
	private CheckBox checkBox;
	private TextView nameText, pwdText;
	private String name, pwd;
	private boolean isChecked = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			name = nameText.getText().toString();
			pwd = pwdText.getText().toString();
			if (TextUtils.isEmpty(name)) {
				Tool.initToast(LoginActivity.this,
						getString(R.string.register_name));
			} else if (TextUtils.isEmpty(pwd)) {
				Tool.initToast(LoginActivity.this,
						getString(R.string.register_password));
			} else {
				Log.v("TAG", "loginEdit:name="+name+",pwd="+pwd);
				login();
				// loginAccount(name, pwd);
			}
			break;

		// case R.id.changePwdBtn:
		// startActivity(new Intent(LoginActivity.this,
		// ForgetPwdActivity.class));
		// break;

		// case R.id.clearBtn:
		// new AlertDialog.Builder(this)
		// .setTitle("提示")
		// .setMessage("确认清除痕迹？清除后不可恢复？")
		// .setPositiveButton("是", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// MsgDbHelper.getInstance(getApplicationContext()).clear();
		// NewFriendDbHelper.getInstance(getApplicationContext()).clear();
		// NewMsgDbHelper.getInstance(getApplicationContext()).clear();
		// FileUtil.RecursionDeleteFile(new File(Constants.SAVE_IMG_PATH));
		// FileUtil.RecursionDeleteFile(new File(Constants.SAVE_SOUND_PATH));
		// }
		// })
		// .setNegativeButton("否", null)
		// .show();
		//
		//
		// break;

		default:
			break;
		}
	}
	public static void saveFile(String str) {  
	    String filePath = null;  
	    boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);  
	    if (hasSDCard) {  
	        filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "hello.txt";  
	    } else  
	        filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "hello.txt";  
	      
	    try {  
	        File file = new File(filePath);  
	        if (!file.exists()) {  
	            File dir = new File(file.getParent());  
	            dir.mkdirs();
	            file.createNewFile();  
	        }  
	        FileOutputStream outStream = new FileOutputStream(file);  
	        outStream.write(str.getBytes());  
	        outStream.close();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}
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
						handler.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json)  {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handler.sendMessage(message);
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
				} else {
					Tool.initToast(LoginActivity.this, getResources()
							.getString(R.string.login_error));
				}
				if (isChecked) {
					MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT,
							name);
					MyAndroidUtil.editXmlByString(Constants.LOGIN_PWD, pwd);
				} else {
					MyAndroidUtil.removeXml(Constants.LOGIN_ACCOUNT);
					MyAndroidUtil.removeXml(Constants.LOGIN_PWD);
				}
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
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
	protected void onResume() {
		// if (getIntent().getBooleanExtra("isRelogin", false)) {
		// Tool.initToast(getApplicationContext(), "此账号已在别处登录");
		// } else if (Constants.USER_NAME != null
		// && !Constants.USER_NAME.equals("")) {
		// Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
		// | Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent);
		// finish();
		// } else {
		// if (name != null && pwd != null) { // 都不为空,自动登录
		// loginAccount(name, pwd);
		// }
		// }
		super.onResume();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		checkBox.setChecked(isChecked);
		this.isChecked = isChecked;
		MyAndroidUtil.editXml(Constants.LOGIN_CHECK, isChecked);
		if (isChecked) {
			checkBox.setButtonDrawable(R.mipmap.login_checked);
		} else {
			checkBox.setButtonDrawable(R.mipmap.login_check);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		showTitle("登录");
		checkBox = getView(R.id.checkBox);
		loginBtn = getView(R.id.loginBtn);
		nameText = getView(R.id.nameText);
		pwdText = getView(R.id.pwdText);
		loginBtn.setOnClickListener(this);
		isChecked = MyApplication.sharedPreferences.getBoolean(
				Constants.LOGIN_CHECK, false);
		checkBox.setOnCheckedChangeListener(this);
		checkBox.setChecked(isChecked);
		nameText.addTextChangedListener(this);

		// 已登录过,自动登录
		name = MyApplication.sharedPreferences.getString(
				Constants.LOGIN_ACCOUNT, null);
		pwd = MyApplication.sharedPreferences.getString(Constants.LOGIN_PWD,
				null);
		// name = "sl70";
		// pwd = "123456";
		if (isChecked) {
			nameText.setText(name);
			pwdText.setText(pwd);
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.acti_login;
	}
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				String a= (String) msg.obj;
				BeanYunIp yunIp = JSON.parseObject(a,
						BeanYunIp.class);
				if (yunIp != null) {
					MyApplication.beanSchoolInfo=yunIp;
					if (!TextUtils.isEmpty(yunIp.ip)) {
						Log.v("TAG", "从云上请求下的ip=" + yunIp.ip);
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
						Log.v("TAG", "loginEdit2:name="+name+",pwd="+MD5Util
								.getMD5String(pwd).toUpperCase());
						Map<String,Object> map=new HashMap<String, Object>();
						map.put("name", name);
						map.put("password", MD5Util
								.getMD5String(pwd).toUpperCase());
						map.put("platform", "1");
						map.put("devtype", "1");
						doPost(Config1.getInstance().LOGIN(), map,
								new ResultCallback() {
									@Override
									public void onResponse(Call call, Response response, String json) {
										Message message=Message.obtain();
										message.what=3;
										message.obj=json;
										handler.sendMessage(message);

									}

									@Override
									public void onFailure(Call call, IOException exception) {
										handler.sendEmptyMessage(4);
									}
								});
					} else {
						toast("云里面此用户Ip为空");
					}
				} else {
					toast("云服务器无此数据");
				}
			}else if (msg.what==3){
				String a= (String) msg.obj;
				JSONObject object=JSON.parseObject(a);
//										saveFile(a);
				Log.v("TAG", "login2:result="+a);
				MyApplication.userInfo = JSON
						.parseObject(object.getString("data"),
								UserInfo.class);
				if (MyApplication.userInfo != null) {
					Log.v("TAG", "loginuser:"
							+ MyApplication.userInfo.id);
					if (MyApplication.userInfo.type == 1) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyApplication.dealLoginInfo();
						}
					}).start();
					SharedPreferenceTool
							.setUserName(
									LoginActivity.this,
									MyApplication.userInfo.name);
					SharedPreferenceTool
							.setPassword(
									LoginActivity.this,
									pwd);
					loginAccount(MyApplication.userInfo.name, pwd);
				}else {
						toast("您不是教师！");
					}
				}

//				Looper.prepare();

//				Looper.loop();
			}else if (msg.what==2){

			}else if (msg.what==4){

			}
		}
	};
}
