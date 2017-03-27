package com.shengliedu.teacher.teacher.chat.xmpp;

import android.content.Intent;
import android.util.Log;

import com.shengliedu.teacher.teacher.chat.activitys.LoginActivity;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.util.MyAndroidUtil;

import org.jivesoftware.smack.ConnectionListener;


public class XmppConnecionListener implements ConnectionListener {

	@Override
	public void connectionClosed() {
		Log.e("smack xmpp", "close");
		XmppConnection.getInstance().setNull();
	}

	@Override
	public void connectionClosedOnError(Exception e) {
//		Log.e("smack xmpp", e.getMessage());
		if(e.getMessage().contains("conflict")){
			MyAndroidUtil.removeXml(Constants.LOGIN_PWD);
			if (!MyApplication.sharedPreferences.getBoolean(Constants.LOGIN_CHECK, false)) {
				MyAndroidUtil.removeXml(Constants.LOGIN_ACCOUNT);
			}
			Constants.USER_NAME = "";
			Constants.loginUser = null;
			XmppConnection.getInstance().closeConnection();
			MyApplication.getInstance().sendBroadcast(new Intent("conflict"));
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("isRelogin", true);
			intent.setClass(MyApplication.getInstance(), LoginActivity.class);
			MyApplication.getInstance().startActivity(intent);
		}
	}

	@Override
	public void reconnectingIn(int seconds) {
//		Log.e("smack xmpp", "reconing:"+seconds);
	}

	@Override
	public void reconnectionSuccessful() {
		XmppConnection.getInstance().loadFriendAndJoinRoom();
//		Log.e("smack xmpp", "suc");
	}

	@Override
	public void reconnectionFailed(Exception e) {
//		Log.e("smack xmpp", "re fail");
	}

	

}
