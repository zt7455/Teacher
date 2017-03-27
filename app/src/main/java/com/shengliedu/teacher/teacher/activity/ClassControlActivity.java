package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;

public class ClassControlActivity extends BaseActivity {
	private LinearLayout control_stu_head_lin;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("班级管理");
		control_stu_head_lin=getView(R.id.control_stu_head_lin);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		control_stu_head_lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ClassControlActivity.this,StudentHeadActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_class_control;
	}
}
