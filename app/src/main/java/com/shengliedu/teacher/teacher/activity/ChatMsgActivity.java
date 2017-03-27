package com.shengliedu.teacher.teacher.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.activitys.ChatActivity;
import com.shengliedu.teacher.teacher.chat.activitys.ContactActivity;
import com.shengliedu.teacher.teacher.chat.adapter.MsgAdapter;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.dao.MsgDbHelper;
import com.shengliedu.teacher.teacher.chat.dao.NewMsgDbHelper;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;

import java.util.ArrayList;
import java.util.List;

public class ChatMsgActivity extends BaseActivity implements OnClickListener {
	private TextView emptyView;
	private EditText searchText;
	private ImageView searchBtn, searchImg, rightBtn;
	private ListView listView;
	public List<ChatItem> lastMsgs = new ArrayList<ChatItem>();
	private MsgDbHelper msgDbHelper;
	private MsgAdapter adapter;
	private NewMsgReceiver newMsgReceiver;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		showTitle("圈子");
		if (MyApplication.chatLogin) {
			initView();
			setRightImage(R.mipmap.add, new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showMenu();
				}
			});
			initData();
		} else {
			loginAccount(
					SharedPreferenceTool.getUserName(ChatMsgActivity.this),
					SharedPreferenceTool.getPassword(ChatMsgActivity.this));
		}
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
					initView();
					setRightImage(R.mipmap.add, new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							showMenu();
						}
					});
					initData();
				} else {
					toast("聊天连接失败");
				}
			}

		};
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.acti_msg;
	}

	public void initView() {
		listView = (ListView) findViewById(R.id.listView);
		emptyView = (TextView) findViewById(R.id.emptyView);
		searchText = (EditText) findViewById(R.id.searchText);
		searchImg = (ImageView) findViewById(R.id.searchImg);
		msgDbHelper = MsgDbHelper.getInstance(this);
		adapter = new MsgAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChatItem chatItem = lastMsgs.get(position);
				Intent intent = new Intent();
				intent.setClass(ChatMsgActivity.this, ChatActivity.class);
				intent.putExtra("chatName", chatItem.chatName);
				intent.putExtra("chatType", chatItem.chatType);
				startActivity(intent);
			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new AlertDialog.Builder(ChatMsgActivity.this)
						.setTitle("提示")
						.setMessage("确认删除信息？删除后不可恢复？")
						.setPositiveButton("是",
								new AlertDialog.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										String username = adapter
												.getItem(position).chatName;
										NewMsgDbHelper.getInstance(
												ChatMsgActivity.this)
												.delNewMsg(username);
										MyApplication
												.getInstance()
												.sendBroadcast(
														new Intent("ChatNewMsg"));
										MsgDbHelper.getInstance(
												ChatMsgActivity.this)
												.delChatMsg(username);
										adapter.notifyDataSetChanged();
									}
								}).setNegativeButton("否", null).show();
				return true;
			}
		});

		// 接收到新消息的事件监听
		newMsgReceiver = new NewMsgReceiver();
		registerReceiver(newMsgReceiver, new IntentFilter("ChatNewMsg"));
		searchText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					initData();
				} else {
					lastMsgs = msgDbHelper.getLastMsg(s.toString());
					adapter.clear();
					adapter.addAll(lastMsgs);
				}
			}
		});

	}

	public void initData() {
		lastMsgs = msgDbHelper.getLastMsg();
		adapter.clear();
		adapter.addAll(lastMsgs);
		if (adapter.getCount() == 0) {
			listView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchText:

			break;
		default:
			break;
		}
	}

	private PopupWindow popupWindow;

	/**
	 * 弹出顶部menu
	 */

	private void showMenu() {
		// TODO Auto-generated method stub
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.layout_popmenu, null, false);
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
		popupWindow_view.findViewById(R.id.tongxunlu).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ChatMsgActivity.this,
								ContactActivity.class);
						startActivityForResult(intent, 300);
						if (popupWindow != null) {
							popupWindow.dismiss();
						}
					}
				});
//		popupWindow_view.findViewById(R.id.qunliao).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						Intent intent = new Intent(ChatMsgActivity.this,
//								ChoseActivity.class);
//						startActivityForResult(intent, 300);
//						if (popupWindow != null) {
//							popupWindow.dismiss();
//						}
//					}
//				});
		// popupWindow_view.findViewById(R.id.person_info).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (popupWindow != null) {
		// popupWindow.dismiss();
		// }
		// }
		// });
	}

	private class NewMsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 更新界面
			initData();
		}
	}

	@Override
	public void onDestroy() {
		try {
			if (newMsgReceiver != null)
				unregisterReceiver(newMsgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		if (searchText!=null) {
			searchText.clearFocus();
		}
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
