package com.shengliedu.teacher.teacher.chat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.activitys.FriendActivity;
import com.shengliedu.teacher.teacher.chat.constant.ImgConfig;
import com.shengliedu.teacher.teacher.chat.model.Friend;
import com.shengliedu.teacher.teacher.chat.util.CircularImage;
import com.shengliedu.teacher.teacher.chat.util.PinyinUtils;

public class ChoseAdapter extends ArrayAdapter<Friend> {
	Activity context;
	public ChoseAdapter(Activity context) {
		super(context, 0);
		this.context = context;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_contact, null);
		}
		final Friend item = getItem(position);
		LinearLayout cateLayout = (LinearLayout) convertView.findViewById(R.id.cateLayout);
		final CircularImage headImg = (CircularImage) convertView.findViewById(R.id.headImg);
		TextView nickView = (TextView) convertView.findViewById(R.id.nickView);
		TextView cateView = (TextView)convertView.findViewById(R.id.cateView);
		ImageView selectBtn = (ImageView)convertView.findViewById(R.id.selectBtn);
		selectBtn.setVisibility(View.VISIBLE);
		nickView.setText(item.username);
		
		if (item.isChose) {
			selectBtn.setImageResource(R.mipmap.login_checked);
		}
		else {
			selectBtn.setImageResource(R.mipmap.login_check);
		}
		ImgConfig.showHeadImg(context,item.username, headImg,nickView);
		
		headImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FriendActivity.class);
				intent.putExtra("username", item.username);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		
		
		//��ĸ����
		String catalog = PinyinUtils.getPingYin(item.username).substring(0, 1).toUpperCase();
		
		if(position == 0){
			cateLayout.setVisibility(View.VISIBLE);
			cateView.setText(catalog);
		}
		else{
			String lastCatalog = PinyinUtils.getPingYin(
					getItem(position-1).username).substring(0,
					1);
			if (catalog.equalsIgnoreCase(lastCatalog)) {
				cateLayout.setVisibility(View.GONE);
			} else {
				cateLayout.setVisibility(View.VISIBLE);
				cateView.setText(catalog);
			}
		}
		return convertView;
	}

}