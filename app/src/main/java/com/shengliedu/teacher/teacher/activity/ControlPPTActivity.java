package com.shengliedu.teacher.teacher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.JsonControl;
import com.shengliedu.teacher.teacher.bean.JsonControlData;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.KeyEvent;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;

import java.util.Date;

public class ControlPPTActivity extends BaseActivity implements OnClickListener {
	private Button classroom_control_next_page, classroom_control_last_page,
			classroom_control_esc, classroom_control_last_item,
			classroom_control_next_item;
	private PPTReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 会话内容改变，接受广播
		receiver = new PPTReceiver();
		registerReceiver(receiver, new IntentFilter("islastPPT"));
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		setRightImage(R.mipmap.diandiandian, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMenu();
			}
		});
		if (MyApplication.chatLogin) {
			classroom_control_last_page = getView(R.id.classroom_control_last_page);
			classroom_control_next_page = getView(R.id.classroom_control_next_page);
			classroom_control_esc = getView(R.id.classroom_control_esc);
			classroom_control_last_item = getView(R.id.classroom_control_last_item);
			classroom_control_next_item = getView(R.id.classroom_control_next_item);
			classroom_control_last_page.setOnClickListener(this);
			classroom_control_next_page.setOnClickListener(this);
//			classroom_control_esc.setOnClickListener(this);
			classroom_control_last_item
					.setOnClickListener(ControlPPTActivity.this);
			classroom_control_next_item
					.setOnClickListener(ControlPPTActivity.this);
			JsonControl jsonControl = new JsonControl();
			jsonControl.date = new Date().getTime();
			jsonControl.data = new JsonControlData();
			jsonControl.data.studentid = 0;
			jsonControl.data.link = "";
			jsonControl.data.value = "";
			jsonControl.type = "isLastPPT";
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			loginAccount(
					MyApplication.userInfo.name,
					SharedPreferenceTool.getPassword(ControlPPTActivity.this));
		}
	}

	private PopupWindow popupWindow;

	/**
	 * 弹出顶部menu
	 */

	private void showMenu() {
		// TODO Auto-generated method stub
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.layout_popmenu_introduction, null, false);
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
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
	}

	private void loginAccount(final String userName, final String password) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				Log.v("TAG", "userName=" + userName);
				Log.v("TAG", "password=" + password);
				boolean isSuccess = XmppConnection.getInstance().login(
						userName, password);
				Log.v("TAG", "isSuccess=" + isSuccess);
				if (isSuccess) {
					Constants.USER_NAME = userName;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				MyApplication.chatLogin = isSuccess;
				if (isSuccess) {
					classroom_control_last_page = getView(R.id.classroom_control_last_page);
					classroom_control_next_page = getView(R.id.classroom_control_next_page);
					classroom_control_esc = getView(R.id.classroom_control_esc);
					classroom_control_last_item = getView(R.id.classroom_control_last_item);
					classroom_control_next_item = getView(R.id.classroom_control_next_item);
					classroom_control_last_page
							.setOnClickListener(ControlPPTActivity.this);
					classroom_control_next_page
							.setOnClickListener(ControlPPTActivity.this);
//					classroom_control_esc
//							.setOnClickListener(ControlPPTActivity.this);
					classroom_control_last_item
							.setOnClickListener(ControlPPTActivity.this);
					classroom_control_next_item
							.setOnClickListener(ControlPPTActivity.this);
					JsonControl jsonControl = new JsonControl();
					jsonControl.date = new Date().getTime();
					jsonControl.data = new JsonControlData();
					jsonControl.data.studentid = 0;
					jsonControl.data.link = "";
					jsonControl.data.value = "";
					jsonControl.type = "isLastPPT";
					try {
						XmppConnection
								.getInstance()
								.setRecevier(
										MyApplication.userInfo.name
												+ "slDispatchServerUser", ChatItem.CHAT);
						XmppConnection.getInstance().sendMsg(
								JSON.toJSONString(jsonControl), ChatItem.CHAT);
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

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_ppt_controler;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		JsonControl jsonControl = new JsonControl();
		jsonControl.date = new Date().getTime();
		jsonControl.data = new JsonControlData();
		jsonControl.data.studentid = 0;
		jsonControl.data.link = "";
		switch (arg0.getId()) {
		case R.id.classroom_control_last_page:
			jsonControl.type = "Key";
			jsonControl.data.key = KeyEvent.VK_PAGE_UP;
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.classroom_control_next_page:
			jsonControl.type = "Key";
			jsonControl.data.key = KeyEvent.VK_PAGE_DOWN;
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.classroom_control_esc:
			jsonControl.type = "lastPPT";
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.classroom_control_last_item:
			jsonControl.type = "ResourceNavigator";
			jsonControl.data.direction = "prev";
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.classroom_control_next_item:
			jsonControl.type = "ResourceNavigator";
			jsonControl.data.direction = "next";
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_UP) {
			JsonControl jsonControl = new JsonControl();
			jsonControl.date = new Date().getTime();
			jsonControl.type = "Key";
			jsonControl.data = new JsonControlData();
			jsonControl.data.studentid = 0;
			jsonControl.data.link = "";
			jsonControl.data.key = KeyEvent.VK_PAGE_UP;
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		if (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN) {
			JsonControl jsonControl = new JsonControl();
			jsonControl.date = new Date().getTime();
			jsonControl.type = "Key";
			jsonControl.data = new JsonControlData();
			jsonControl.data.studentid = 0;
			jsonControl.data.link = "";
			jsonControl.data.key = KeyEvent.VK_PAGE_DOWN;
			try {
				XmppConnection
						.getInstance()
						.setRecevier(
								MyApplication.userInfo.name
										+ "slDispatchServerUser", ChatItem.CHAT);
				XmppConnection.getInstance().sendMsg(
						JSON.toJSONString(jsonControl), ChatItem.CHAT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_UP) {
			return true;
		}
		if (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	private class PPTReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到廣播更新我们的界面
			String jsonStr=intent.getStringExtra("value");
			Log.v("TAG", "xulaoshi2=" +jsonStr );
			if (intent.getAction().equals("islastPPT")) {
				if (!TextUtils.isEmpty(jsonStr)) {
					JsonControl jsonControl=JSON.parseObject(jsonStr, JsonControl.class);
					if (jsonControl!=null && jsonControl.data!=null) {
						if ("yes".equals(jsonControl.data.value)) {
							classroom_control_esc.setBackground(ControlPPTActivity.this.getResources().getDrawable(R.drawable.classroom_control_esc_press));
							classroom_control_esc.setOnClickListener(ControlPPTActivity.this);
						}else if ("no".equals(jsonControl.data.value)) {
							classroom_control_esc.setBackground(ControlPPTActivity.this.getResources().getDrawable(R.mipmap.classroom_control_no_esc));
							classroom_control_esc.setOnClickListener(null);
						}
					}
				}
			}
		}
	}
}
