package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PLeaveDetailActivity extends BaseActivity {
	private TextView p_leave_detail_info;
	private Normal leave;
	private ImageView pass,nopass;
	private int approve;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		leave=(Normal) getIntent().getSerializableExtra("leave");
		setBack();
		p_leave_detail_info=getView(R.id.p_leave_detail_info);
		pass=getView(R.id.pass);
		nopass=getView(R.id.nopass);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (leave!=null) {
			showTitle(leave.studentName+"同学");
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
			String qingjiatype = "";
			String qingjiatime = "";
			String qingjiashiduan = "";
			String childname = leave.studentName;
			String teacher = MyApplication.getTeacherNameForId(leave.teacher);
			if (leave.type == 1) {
				qingjiatype = "事假";
			} else {
				qingjiatype = "病假";
			}
			if (leave.part == 0) {
				qingjiashiduan = "全天";
			} else if (leave.part == 1) {
				qingjiashiduan = "上午";
			}else if (leave.part == 1) {
				qingjiashiduan = "下午";
			}
			if (qingjiatime.contains(",")) {
				qingjiatime.replace(
						qingjiatime.charAt(qingjiatime.lastIndexOf(',')),
						' ');
			}
			p_leave_detail_info.setText("尊敬的" + teacher
					+ "老师:\n"+ "    我是本班 " +childname + " 同学,因 " + leave.content+" "+qingjiashiduan
					+ " 请" + qingjiatype + ",请假时间从 "
					+ format.format(new Date(leave.stime * 1000))
					+ " 到 "
					+ format.format(new Date(leave.etime * 1000))
					+ "。");
			pass.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					approve=1;
					requestData();
				}
			});
			nopass.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					approve=2;
					requestData();
				}
			});
		}
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				Intent intent=new Intent();
				intent.putExtra("approve", approve);
				setResult(301,intent);
				toast("批请假成功！");
				finish();
			}else if (msg.what==2){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", leave.id);
		map.put("approve", approve);
		doPost(Config1.getInstance().P_LEAVE_APPROVE(), map,
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
		return R.layout.activity_p_leave_detail;
	}

}
