package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.activitys.LoginActivity;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;

public class MyActivity extends BaseActivity {
	private LinearLayout person_info_lin,teach_task_lin,my_zone_lin,class_manage_lin;
	private Button login_out;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("我的");
		person_info_lin=getView(R.id.person_info_lin);
//		teach_task_lin=getView(R.id.teach_task_lin);
//		teach_task_lin.setVisibility(View.GONE);
		my_zone_lin=getView(R.id.my_zone_lin);
		class_manage_lin=getView(R.id.class_manage_lin);
		login_out=getView(R.id.login_out);
		login_out.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				XmppConnection.getInstance().closeConnection();
				Intent intent = new Intent(MyActivity.this,
						LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		person_info_lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MyActivity.this,MyInfoActicity.class);
				startActivity(intent);
			}
		});
		my_zone_lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyActivity.this, MyZoneActivity.class);
				startActivity(intent);
			}
		});
//		if (ConsoleActivity.roleJiaoshi || ConsoleActivity.roleJiaowu || ConsoleActivity.roleJiaoyan) {
//			teach_task_lin.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(MyActivity.this,
//							TeachTaskActivity.class);
//					startActivity(intent);
//				}
//			});
//		}else {
//			teach_task_lin.setVisibility(View.GONE);
//		}
		if (ConsoleActivity.roleBanzhuren) {
			class_manage_lin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(MyActivity.this,ClassControlActivity.class);
					startActivity(intent);
				}
			});
		}else {
			class_manage_lin.setVisibility(View.GONE);
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_my;
	}
}
