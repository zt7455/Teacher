package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.ConsoleMenuAdapter;
import com.shengliedu.teacher.teacher.bean.ConsoleMenu;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ConsoleMenus;
import com.shengliedu.teacher.teacher.util.OnTabActivityResultListener;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.zxing.activity.CaptureActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ConsoleActivity extends BaseActivity implements
		OnItemClickListener, OnTabActivityResultListener {
	private GridView grid_select;
	private ConsoleMenuAdapter adapter;
	private List<ConsoleMenu> list = new ArrayList<ConsoleMenu>();
	public List<IdName> roleArr;
	public static boolean roleJiaoshi;
	public static boolean roleBanzhuren;
	public static boolean roleJiaowu;
	public static boolean roleJiaoyan;
	public static boolean roleNianjizu;
	private String value;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		grid_select = getView(R.id.grid_select);
		grid_select.setFocusable(false);
		adapter = new ConsoleMenuAdapter(this, list);
		grid_select.setAdapter(adapter);
		grid_select.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = null;
		int function = list.get(arg2).number;
		switch (function) {
		// case 100:// 教学任务
		// if (roleJiaoshi || roleJiaowu || roleNianjizu) {
		// intent = new Intent(ConsoleActivity.this,
		// TeachTaskActivity.class);
		// startActivity(intent);
		// }
		// break;
		case 200:// 周计划
			if (roleJiaoshi || roleJiaowu || roleNianjizu) {
				intent = new Intent(ConsoleActivity.this,
						WeekPlanActivity.class);
				getParent().startActivity(intent);
			}
			break;
		case 300:// 教学日志
			if (roleJiaoshi || roleJiaowu || roleNianjizu) {
				intent = new Intent(ConsoleActivity.this,
						TeachLogActivity.class);
				getParent().startActivity(intent);
			}
			break;
		case 400:// 作业展评
			if (roleJiaoshi) {
				intent = new Intent(ConsoleActivity.this,
						HomeworkShowActivity.class);
				getParent().startActivity(intent);
			}
			break;
		case 500:// 通知
			intent = new Intent(ConsoleActivity.this, MessageActivity.class);
			getParent().startActivity(intent);
			break;
		case 600:// 备课浏览
			if (roleJiaoshi) {
				intent = new Intent(ConsoleActivity.this,
						BKbrowseActivity.class);
				getParent().startActivity(intent);
			}
			break;
		case 700:// 备课检查
			if (roleJiaowu || roleJiaoyan) {
				intent = new Intent(ConsoleActivity.this, BKCheckTeacherListActivity.class);
				getParent().startActivity(intent);
			}
			// toast("正在研发...");
			break;
		case 800:// 班级日志
			if (roleBanzhuren || roleJiaowu || roleNianjizu) {
				intent = new Intent(ConsoleActivity.this,
						ClassroomLogActivity.class);
				getParent().startActivity(intent);
			}
			break;
		case 900:// 批改作业
			if (roleJiaoshi) {
				intent = new Intent(ConsoleActivity.this,
						CorrectHomeworkActivity.class);
				getParent().startActivity(intent);
			}
			break;
		case 1000:// 批准请假
			if (roleBanzhuren || roleJiaowu || roleNianjizu) {
				intent = new Intent(ConsoleActivity.this,
						PLeaveListActivity.class);
				getParent().startActivity(intent);
			}
			break;
		// case 1100:// 我的空间
		// intent = new Intent(ConsoleActivity.this, MyZoneActivity.class);
		// startActivity(intent);
		// break;
		case 1200:// 扫扫登录
			Intent openCameraIntent = new Intent(ConsoleActivity.this,
					CaptureActivity.class);
			getParent().startActivityForResult(openCameraIntent, 0);
			break;
		case 1300:// 教学资源
			intent = new Intent(ConsoleActivity.this,
					TeachResourceActivity.class);
			getParent().startActivityForResult(intent, 0);
//			intent = new Intent(ConsoleActivity.this,
//					FlashHtmlActivity.class);
//			intent.putExtra("html", "http://192.168.1.203:83/client/classAssist.html?username=cslDispatchServerUser");
//			intent.putExtra("content_host", "http://192.168.1.203:83/client/classAssist.html?username=cslDispatchServerUser");
//			intent.putExtra("plan_info", "http://192.168.1.203:83/client/classAssist.html?username=cslDispatchServerUser");
//			getParent().startActivityForResult(intent, 0);
			break;
		case 1400:// ppt
			intent = new Intent(ConsoleActivity.this,
					ControlPPTActivity.class);
			getParent().startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		if (MyApplication.userInfo != null) {
			roleArr = MyApplication.userInfo.roleArr;
			if (roleArr != null && roleArr.size() > 0) {
				list.clear();
				for (int i = 0; i < roleArr.size(); i++) {
					IdName role = roleArr.get(i);
					if (role.id == Config1.JIAOSHI) {
						ConsoleMenus.removeListDuplicateObject(list,
								ConsoleMenus.teacher());
						roleJiaoshi = true;
					}
					if (role.id == Config1.BANZHUREN) {
						ConsoleMenus.removeListDuplicateObject(list,
								ConsoleMenus.banzhuren());
						roleBanzhuren = true;
					}
					if (role.id == Config1.JIAOWU) {
						ConsoleMenus.removeListDuplicateObject(list,
								ConsoleMenus.jiaowu());
						roleJiaowu = true;
					}
					if (role.id == Config1.NIANJIZUZHANG) {
						ConsoleMenus.removeListDuplicateObject(list,
								ConsoleMenus.nianjizu());
						roleNianjizu = true;
					}
					if (role.id == Config1.JIAOYAN) {
						ConsoleMenus.removeListDuplicateObject(list,
								ConsoleMenus.jiaoyan());
						roleJiaoyan = true;
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}
		}
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_console;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			value = bundle.getString("result");
//			 toast("ok="+value);
			requestData();
			// toast(scanResult);
			// Log.v("TAG", "scanResult:"+scanResult);
			// try {
			// JSONObject jsonObject = new JSONObject(scanResult);
			// value = jsonObject.getString("value");
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// toast("请扫描授课端二维码");
			// }
		}
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				toast((String) msg.obj);
				toast("扫描成功");
			}else if (msg.what==2){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		map.put("schoolId",
				MyApplication.userInfo.schoolinfo.id + "");
		map.put("userName", MyApplication.userInfo.name + "");
		map.put("token", value);
		Log.v("TAG", "schoolId:" + MyApplication.userInfo.schoolinfo.id
				+ ",userName:" + MyApplication.userInfo.name + ",token=" + value);
		toast("schoolId:" + MyApplication.userInfo.schoolinfo.id
				+ ",userName:" + MyApplication.userInfo.name + ",token=" + value);
		doPost(Config1.getInstance().CODE_LOGIN(), map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json)  {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}
}
