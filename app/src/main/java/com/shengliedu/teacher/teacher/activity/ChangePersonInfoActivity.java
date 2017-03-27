package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.util.MobileUtils;

public class ChangePersonInfoActivity extends BaseActivity {
	private TextView title,message,positiveButton,negativeButton;
	private EditText edit_message;
	private int changetype;
	private String name;
	@Override
	public void initViews() {
		name=getIntent().getStringExtra("name");
		changetype=getIntent().getIntExtra("changetype",-1);
		// TODO Auto-generated method stub
		title=getView(R.id.title);
		positiveButton=getView(R.id.positiveButton);
		negativeButton=getView(R.id.negativeButton);
		edit_message=getView(R.id.edit_message);
		if (!TextUtils.isEmpty(name)) {
			edit_message.setText(name);
		}
		positiveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (edit_message.getText().toString().length()>20) {
					toast("不能超过20个字");
				}else {
					if (changetype==5) {
						if (MobileUtils.isMobileNO(edit_message.getText().toString())) {
							Intent intent=new Intent();
							intent.putExtra("name", edit_message.getText().toString());
							setResult(600+changetype,intent);
							finish();
						}else {
							toast("请输入正确的手机号");
						}
					}else {
						Intent intent=new Intent();
						intent.putExtra("name", edit_message.getText().toString());
						setResult(600+changetype,intent);
						finish();
					}
				}
			}
		});
		negativeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		switch (changetype) {
		case 5:
			title.setText("手机");
			edit_message.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case 6:
			title.setText("地址");
			edit_message.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 7:
			title.setText("签名");
			edit_message.setInputType(InputType.TYPE_CLASS_TEXT);
			break;

		default:
			break;
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_change_person_info;
	}

}
