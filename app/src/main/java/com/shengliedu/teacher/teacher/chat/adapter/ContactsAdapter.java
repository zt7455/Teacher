package com.shengliedu.teacher.teacher.chat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.activitys.FriendActivity;
import com.shengliedu.teacher.teacher.chat.constant.ImgConfig;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.dao.NewMsgDbHelper;
import com.shengliedu.teacher.teacher.chat.model.Friend;
import com.shengliedu.teacher.teacher.chat.util.CircularImage;
import com.shengliedu.teacher.teacher.chat.util.PinyinUtils;

public class ContactsAdapter extends ArrayAdapter<Friend> {
	Activity context;
	public ContactsAdapter(Activity context) {
		super(context, 0);
		this.context = context;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final Friend item = getItem(position);	
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_contact, null);
			viewHolder = new ViewHolder();
			viewHolder.cateLayout = (LinearLayout) convertView.findViewById(R.id.cateLayout);
			viewHolder.nickView = (TextView) convertView.findViewById(R.id.nickView);
			viewHolder.cateView = (TextView)convertView.findViewById(R.id.cateView);
			viewHolder.countView = (TextView) convertView.findViewById(R.id.countView);
			viewHolder.headImg = (CircularImage) convertView.findViewById(R.id.headImg);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nickView.setText(item.username);

		if(item.username.equals("新的朋友")){
			ImgConfig.showImg("drawable://" + R.mipmap.news_friends_icon , viewHolder.headImg);
//			viewHolder.headImg.setImageBitmap(ImgHandler.ToCircular(BitmapFactory.decodeResource(context.getResources(), R.drawable.news_friends_icon)));
		}
		else if(item.username.equals("群聊")){  //群聊
			ImgConfig.showImg("drawable://" + R.mipmap.group_chat_icon , viewHolder.headImg);
//			viewHolder.headImg.setImageBitmap(ImgHandler.ToCircular(BitmapFactory.decodeResource(context.getResources(), R.drawable.group_chat_icon)));
		}
		else {
			ImgConfig.showHeadImg(context,item.username, viewHolder.headImg,viewHolder.nickView);
			viewHolder.headImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, FriendActivity.class);
					intent.putExtra("username", item.username);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			});
			
		}
		
		//字母排列
		String catalog = PinyinUtils.getPingYin(item.username).substring(0, 1).toUpperCase();
		
		if(position == 0 || position == 1){
			viewHolder.cateLayout.setVisibility(View.GONE);
		}
		else{
			String lastCatalog = PinyinUtils.getPingYin(getItem(position-1).username).substring(0,1);			
			if (catalog.equalsIgnoreCase(lastCatalog)) {
				viewHolder.cateLayout.setVisibility(View.GONE);
			} else {
				viewHolder.cateLayout.setVisibility(View.VISIBLE);
				viewHolder.cateView.setText(catalog);
			}
		}
		
		
		//count
		if (position == 0) {
			int count = NewMsgDbHelper.getInstance(MyApplication.getInstance()).getMsgCount(""+0);
			if (count > 0) {
				viewHolder.countView.setText(""+count);
				viewHolder.countView.setVisibility(View.VISIBLE);
			}
			else {
				viewHolder.countView.setVisibility(View.GONE);
			}
		}
		else {
			viewHolder.countView.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	
	class ViewHolder {
		LinearLayout cateLayout;
		TextView nickView,cateView,countView;
		CircularImage headImg;
	}
}