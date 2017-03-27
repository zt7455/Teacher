package com.shengliedu.teacher.teacher.chat.xmpp;

import android.content.Intent;
import android.util.Log;

import com.shengliedu.teacher.teacher.chat.dao.NewMsgDbHelper;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.model.Room;
import com.shengliedu.teacher.teacher.chat.util.DateUtil;
import com.shengliedu.teacher.teacher.chat.util.FileUtil;
import com.shengliedu.teacher.teacher.chat.util.MyAndroidUtil;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.dao.MsgDbHelper;
import com.shengliedu.teacher.teacher.util.AppValue;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.DelayInformation;

import java.util.Date;


public class XmppMessageListener implements PacketListener {

	@Override
	public void processPacket(Packet packet) {
		final Message nowMessage = (Message) packet;
		Log.v("XmppMessageListener", "info:" + nowMessage.getBody());
		Date date = new Date();
		SharedPreferenceTool.setChattime(MyApplication.getInstance(),
				date.getTime() + "");
		if (Constants.IS_DEBUG)
			Log.e("xmppchat come", nowMessage.toXML());

		if (nowMessage.toXML().contains("<invite")) {
			String noti = "你被邀请加入群组"
					+ XmppConnection.getRoomName(nowMessage.getFrom());
			if (AppValue.isRunningApp(MyApplication.getInstance(),
					MyApplication.getInstance().getPackageName())) {
				MyAndroidUtil.showNoti(noti);
			}
			String userName = XmppConnection.getRoomName(nowMessage.getFrom());
			;
			ChatItem msg = new ChatItem(ChatItem.GROUP_CHAT, userName,
					userName, "", noti, DateUtil.now_yyyy_MM_dd_HH_mm_ss(), 0);
			NewMsgDbHelper.getInstance(MyApplication.getInstance()).saveNewMsg(
					userName);
			MsgDbHelper.getInstance(MyApplication.getInstance()).saveChatMsg(
					msg);
			MyApplication.getInstance().sendBroadcast(new Intent("ChatNewMsg"));

			Log.v("TAG",
					"getRoomName:"
							+ XmppConnection.getRoomName(nowMessage.getFrom()));
			XmppConnection.getInstance().joinMultiUserChat(Constants.USER_NAME,
					XmppConnection.getRoomName(nowMessage.getFrom()), false);
		}

		Type type = nowMessage.getType();
		Log.v("TAG", "ss:" + nowMessage.getBody() + ",userName="
				+ XmppConnection.getUsername(nowMessage.getFrom()));
		if ((type == Type.groupchat || type == Type.chat)
				&& !nowMessage.getBody().equals("")) { //
			String chatName = "";
			String userName = "";
			int chatType = ChatItem.CHAT;

			// name
			if (type == Type.groupchat) {
				chatName = XmppConnection.getRoomName(nowMessage.getFrom());
				userName = XmppConnection.getRoomUserName(nowMessage.getFrom());
				chatType = ChatItem.GROUP_CHAT;
			} else {
				chatName = userName = XmppConnection.getUsername(nowMessage
						.getFrom());
			}

			if (!userName.equals(Constants.USER_NAME)
					&& !userName.toLowerCase().equals((Constants.USER_NAME
							+ "slDispatchServerUser").toLowerCase())) { // 不是自己发出的,防群聊
				// time
				String dateString;
				DelayInformation inf = (DelayInformation) nowMessage
						.getExtension("x", "jabber:x:delay");
				if (inf == null)
					dateString = DateUtil.now_yyyy_MM_dd_HH_mm_ss();
				else
					dateString = DateUtil.dateToStr_yyyy_MM_dd_HH_mm_ss(inf
							.getStamp());

				// msg
				ChatItem msg = null;
				String msgBody; // 判断是否图片
				if (nowMessage.getProperty("imgData") != null) {
					if (FileUtil.getType(nowMessage.getBody()) == FileUtil.SOUND)
						msgBody = Constants.SAVE_SOUND_PATH + "/"
								+ nowMessage.getBody();
					else
						msgBody = Constants.SAVE_IMG_PATH + "/"
								+ nowMessage.getBody();
					FileUtil.saveFileByBase64(nowMessage.getProperty("imgData")
							.toString(), msgBody);
				} else if (nowMessage.getType() == Type.groupchat
						& nowMessage.getBody().contains(":::")) { // 被迫的
					String[] msgAndData = nowMessage.getBody().split(":::");
					if (FileUtil.getType(msgAndData[0]) == FileUtil.SOUND)
						msgBody = Constants.SAVE_SOUND_PATH + "/"
								+ msgAndData[0];
					else
						msgBody = Constants.SAVE_IMG_PATH + "/" + msgAndData[0];
					FileUtil.saveFileByBase64(msgAndData[1], msgBody);
				} else
					msgBody = nowMessage.getBody();

				if (type == Type.groupchat
						&& XmppConnection.leaveRooms
								.contains(new Room(chatName))) { // 正常保存了
					System.out.println("我已经离开这个房间了");
				} else if (nowMessage.getBody().contains("[RoomChange")) {
					// String leaveRoom = nowMessage.getBody().split(",")[1];
					// String leaveUser = nowMessage.getBody().split(",")[2];
					// XmppConnection.getInstance().getMyRoom()
					// .get(XmppConnection.getInstance().getMyRoom().indexOf(new
					// Room(leaveRoom))).friendList.remove(leaveUser);
					XmppConnection.getInstance().reconnect();
				} else {
					Log.v("TAG", "date=" + dateString);
					msg = new ChatItem(chatType, chatName, userName, "",
							msgBody, dateString, 0);
					NewMsgDbHelper.getInstance(MyApplication.getInstance())
							.saveNewMsg(chatName);
					MsgDbHelper.getInstance(MyApplication.getInstance())
							.saveChatMsg(msg);
					MyApplication.getInstance().sendBroadcast(
							new Intent("ChatNewMsg"));
					// if (AppValue.isBackground(MyApplication.getInstance())) {
					MyAndroidUtil.showNoti(msgBody);
					// }
				}
			} else if (userName.toLowerCase().equals((Constants.USER_NAME
					+ "slDispatchServerUser").toLowerCase())) {
				Intent intent = new Intent("islastPPT");
				Log.v("TAG", "xulaoshi=" + nowMessage.getBody());
				intent.putExtra("value", nowMessage.getBody());
				MyApplication.getInstance().sendBroadcast(intent);
			}
		}
	}
}
