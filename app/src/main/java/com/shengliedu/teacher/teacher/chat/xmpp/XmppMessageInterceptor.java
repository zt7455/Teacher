package com.shengliedu.teacher.teacher.chat.xmpp;

import android.content.Intent;
import android.util.Log;

import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.dao.MsgDbHelper;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.util.DateUtil;
import com.shengliedu.teacher.teacher.chat.util.FileUtil;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class XmppMessageInterceptor implements PacketInterceptor {

	@Override
	public void interceptPacket(Packet packet) {
		Message nowMessage = (Message) packet;
		if (Constants.IS_DEBUG)
			Log.e("xmppchat send", nowMessage.toXML());
		if (nowMessage.getType() == Message.Type.groupchat
				|| nowMessage.getType() == Message.Type.chat) {
			String chatName = "";
			String userName = "";
			int chatType = ChatItem.CHAT;
			// name
			if (nowMessage.getType() == Message.Type.groupchat) {
				chatName = XmppConnection.getRoomName(nowMessage.getTo());
				userName = nowMessage.getTo();
				chatType = ChatItem.GROUP_CHAT;
			} else {
				chatName = userName = XmppConnection.getUsername(nowMessage
						.getTo());
			}
			// type
			// 记录我们发出去的消息
			String msgBody;
			if (nowMessage.getProperty("imgData") != null) {
				if (FileUtil.getType(nowMessage.getBody()) == FileUtil.SOUND)
					msgBody = Constants.SAVE_SOUND_PATH + "/"
							+ nowMessage.getBody();
				else
					msgBody = Constants.SAVE_IMG_PATH + "/"
							+ nowMessage.getBody();
			} else if (nowMessage.getType() == Message.Type.groupchat
					& nowMessage.getBody().contains(":::")) { // 被迫的
				String[] msgAndData = nowMessage.getBody().split(":::");
				if (FileUtil.getType(msgAndData[0]) == FileUtil.SOUND)
					msgBody = Constants.SAVE_SOUND_PATH + "/" + msgAndData[0];
				else
					msgBody = Constants.SAVE_IMG_PATH + "/" + msgAndData[0];
			} else
				msgBody = nowMessage.getBody();

			if (nowMessage.getBody().contains("[RoomChange")) {
				System.out.println("房间要发生改变了");
			} else {
				if (chatName!=null && !chatName.equals(Constants.USER_NAME+"slDispatchServerUser")) {
					ChatItem msg = new ChatItem(chatType, chatName, userName, "",
							msgBody, DateUtil.now_yyyy_MM_dd_HH_mm_ss(), 1);
					MsgDbHelper.getInstance(MyApplication.getInstance())
					.saveChatMsg(msg);
					MyApplication.getInstance().sendBroadcast(
							new Intent("ChatNewMsg"));
				}
			}
		}
	}
}
