/**
 * 
 */
package com.shengliedu.teacher.teacher.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.constant.ImgConfig;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.dao.NewFriendDbHelper;
import com.shengliedu.teacher.teacher.chat.util.CircularImage;
import com.shengliedu.teacher.teacher.chat.util.Tool;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;

/**
 * @author MZH
 *
 */
public class SearchAdapter extends ArrayAdapter<String> {
	Activity context;
	public boolean isNewFriend = false;  //添加新朋友
	
	public SearchAdapter(Activity context) {
		super(context, 0);
		this.context = context;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_search, null);
		}
		final String item = getItem(position);
		CircularImage imgView = (CircularImage) convertView.findViewById(R.id.imgView);
		TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
		final Button addBtn = (Button) convertView.findViewById(R.id.addBtn);
		
		nameView.setText(item);
		ImgConfig.showHeadImg(context,item, imgView,nameView);
		if (NewFriendDbHelper.getInstance(getContext()).isDeal(item)) {
			addBtn.setVisibility(View.GONE);
		}
		else{
			addBtn.setVisibility(View.VISIBLE);
			addBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new XmppLoadThread(context) {
	
						@Override
						protected void result(Object o) {
							if ((Boolean) o) {
								Tool.initToast(context, context.getString(R.string.add_friends_success));
								addBtn.setVisibility(View.GONE);
								NewFriendDbHelper.getInstance(MyApplication.getInstance()).delFriend(item);
							}
						}
	
						@Override
						protected Object load() {
							return XmppConnection.getInstance().addUser(item);
	
						}
					};
				}
			});
		}
		
		if (!isNewFriend) {
			addBtn.setVisibility(View.GONE);
		}
		return convertView;
	}
}
