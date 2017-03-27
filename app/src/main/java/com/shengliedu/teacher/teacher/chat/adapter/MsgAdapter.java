package com.shengliedu.teacher.teacher.chat.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.ImgConfig;
import com.shengliedu.teacher.teacher.chat.dao.NewMsgDbHelper;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.util.CircularImage;
import com.shengliedu.teacher.teacher.chat.util.StringUtil;
import com.shengliedu.teacher.teacher.chat.view.expression.ExpressionUtil;

public class MsgAdapter extends ArrayAdapter<ChatItem> {
	private Activity cxt;
	private NewMsgDbHelper newMsgDbHelper ;
	
	public MsgAdapter(Activity context) {
		super(context, 0);
		this.cxt = context;
		newMsgDbHelper = NewMsgDbHelper.getInstance(cxt);
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(cxt).inflate(R.layout.row_msg, null);
			holder = new ViewHolder();
			holder.headImg = (CircularImage) convertView.findViewById(R.id.headImg);
			holder.nickView = (TextView) convertView.findViewById(R.id.nickView);
			holder.msgView = (TextView) convertView.findViewById(R.id.msgView);
			holder.countView = (TextView) convertView.findViewById(R.id.countView);
			holder.dateView = (TextView) convertView.findViewById(R.id.dateView);
			convertView.setTag(holder); 
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChatItem item = getItem(position);

		
//	//是图片或者表情则先替换
		if (item.msg!=null) {
			if(item.msg.contains(Constants.SAVE_IMG_PATH))
				holder.msgView.setText("[图片]");
			else if(item.msg.contains(Constants.SAVE_SOUND_PATH))
					holder.msgView.setText("[语音]");
			else if(item.msg.contains("[/g0"))
					holder.msgView.setText("[动画表情]");
			else if(item.msg.contains("[/f0"))  //适配表情
				holder.msgView.setText(ExpressionUtil.getText(cxt, StringUtil.Unicode2GBK(item.msg)));
			else if(item.msg.contains("[/a0"))
				holder.msgView.setText("[位置]");
			else{
				holder.msgView.setText(item.msg);
			}
		}
		holder.nickView.setText(item.chatName);
		holder.countView.setVisibility(View.GONE);
		holder.dateView.setText(item.sendDate);
		if (item.chatType == ChatItem.CHAT) {
			ImgConfig.showHeadImg(cxt,item.username, holder.headImg,holder.nickView);
		}
		else{
			holder.headImg.setImageResource(R.mipmap.group_chat_icon);
		}
			
		//是否显示有消息
		int newCount = newMsgDbHelper.getMsgCount(item.chatName);
		if(newCount!=0){
			holder.countView.setVisibility(View.VISIBLE);
			holder.countView.setText(""+newCount);
		}
		return convertView;
	}
	
	static class ViewHolder {
		CircularImage headImg;
		TextView nickView;
		TextView msgView;
		TextView countView;
		TextView dateView;
	} 
}