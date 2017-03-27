package com.shengliedu.teacher.teacher.chat.activitys;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.adapter.ContactsAdapter;
import com.shengliedu.teacher.teacher.chat.model.Friend;
import com.shengliedu.teacher.teacher.chat.util.MySideBar;
import com.shengliedu.teacher.teacher.chat.util.MySideBar.OnTouchingLetterChangedListener;
import com.shengliedu.teacher.teacher.chat.util.PinyinUtils;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.view.D3View;
import com.shengliedu.teacher.teacher.chat.view.MyListView;
import com.shengliedu.teacher.teacher.chat.view.MyListView.OnRefreshListener;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;

public class ContactActivity extends BaseActivity implements OnTouchingLetterChangedListener{
	@D3View public EditText searchText;
	@D3View TextView newCountView;
	@D3View MyListView listView;
	@D3View MySideBar sideBar;
	@D3View Button groupBtn, addBtn;
	ContactsAdapter adapter;
	FriendReceiver reciver;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.acti_contacts);
		initView();
		reciver = new FriendReceiver();
		registerReceiver(reciver,new IntentFilter("friendChange"));
		registerReceiver(reciver,new IntentFilter("FriendNewMsg"));
		initData();
	}
	@Override
	public void onResume() {
		searchText.clearFocus();
		super.onResume();
	}
	
	public void initData() {
		adapter.clear();
		adapter.add(new Friend("新的朋友"));
		adapter.add(new Friend("群聊"));
		adapter.addAll(XmppConnection.getInstance().getFriendBothList());
	}
	
	
	public void initView() {
		sideBar.setOnTouchingLetterChangedListener(this);
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();
				if (position == 1) {
					intent.setClass(ContactActivity.this, NewFriendActivity.class);
				}
				else if (position == 2) {
					intent.setClass(ContactActivity.this, MyRoomActivity.class);
				}
				else {
					intent.setClass(ContactActivity.this, ChatActivity.class);
					intent.putExtra("chatName", XmppConnection.getInstance().getFriendBothList().get(position-3).username);
				}
				startActivity(intent);
			}
		});
		listView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				friendChange();
			}
		});
		
		
		
		adapter = new ContactsAdapter(this);
		listView.setAdapter(adapter);
		adapter.add(new Friend("新的朋友"));
		adapter.add(new Friend("群聊"));
		
		searchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				adapter.clear();
				adapter.add(new Friend("新的朋友"));
				adapter.add(new Friend("群聊"));
				if (s.toString().equals("")) {
					adapter.addAll(XmppConnection.getInstance().getFriendList());
				}
				else {
					List<Friend>  friendTemps = new ArrayList<Friend>();
					for (Friend friend : XmppConnection.getInstance().getFriendList()) {
						if (friend.username.contains(s.toString())) {
							friendTemps.add(friend);
						}
					}
					adapter.addAll(friendTemps);
				}
			}
		});
	}
	
	public void friendChange() {
		new XmppLoadThread(this) {
			@Override
			protected void result(Object object) {
				initData();
				listView.onRefreshComplete();
			}
			
			@Override
			protected Object load() {
				return XmppConnection.getInstance().getFriendBothList();
			}
		};
	}
	
	
	public void onClick(View v) {
	}
	
	public int alphaIndexer(String s) {
		int position = 0;
		String alpha;
		for (int i = 0; i < XmppConnection.getInstance().getFriendBothList().size(); i++) {
			alpha = PinyinUtils.getPingYin(XmppConnection.getInstance().getFriendBothList().get(i).username).toUpperCase();
			if (alpha.startsWith(s)) {
				position = i;
				break;
			}
		}
		return position;
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		if (alphaIndexer(s) > 0) {
			int position = alphaIndexer(s);
			listView.setSelection(position);
		}
	}
	
	private class FriendReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("FriendNewMsg")) {
				adapter.notifyDataSetChanged();
			}
			else {
				//更新界面
				friendChange();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(reciver);
	}
}
