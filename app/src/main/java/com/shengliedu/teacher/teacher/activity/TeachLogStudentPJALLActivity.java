package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class TeachLogStudentPJALLActivity extends BaseActivity {
	private Button ok;
	private RadioGroup rg;
	private EditText et_py;
	private RadioButton rb0, rb1, rb2, rb3;

	private int activityId;
	private int behavior;
	private String behaviorNote;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		activityId = getIntent().getIntExtra("activityId", -1);
		behavior = getIntent().getIntExtra("behavior", -1);
		behaviorNote = getIntent().getStringExtra("behaviorNote");
		setBack();
		showTitle("评价课堂");
		ok = getView(R.id.ok);
		rg = getView(R.id.rg);
		et_py = getView(R.id.et_py);
		et_py.setText(behaviorNote+"");
		rb0 = getView(R.id.rb0);
		rb1 = getView(R.id.rb1);
		rb2 = getView(R.id.rb2);
		rb3 = getView(R.id.rb3);
		switch (behavior) {
		case 1:
			rb0.setChecked(true);
			break;
		case 2:
			rb1.setChecked(true);
			break;
		case 3:
			rb2.setChecked(true);
			break;
		case 4:
			rb3.setChecked(true);
			break;

		default:
			break;
		}
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.rb0:
					behavior = 1;
					break;
				case R.id.rb1:
					behavior = 2;
					break;
				case R.id.rb2:
					behavior = 3;
					break;
				case R.id.rb3:
					behavior = 4;
					break;

				default:
					break;
				}
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				requestData();
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				toast("评价成功！");
				Intent intent=new Intent();
				intent.putExtra("behavior", behavior);
				intent.putExtra("behaviorNote", behaviorNote);
				setResult(401,intent);
				finish();
			}else if (msg.what==2){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		behaviorNote=et_py.getText().toString();
		Map<String,Object> map=new HashMap<>();
		map.put("id", activityId+"");
		map.put("behavior", behavior+"");
		map.put("behaviorNote", behaviorNote);
		doPost(Config1.getInstance().IP+"activity", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json) {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_teach_log_pj_all;
	}

}
