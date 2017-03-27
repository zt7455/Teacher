package com.shengliedu.teacher.teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.shengliedu.teacher.teacher.activity.ChatMsgActivity;
import com.shengliedu.teacher.teacher.activity.ConsoleActivity;
import com.shengliedu.teacher.teacher.activity.MyActivity;
import com.shengliedu.teacher.teacher.adapter.ViewPagerAdapter;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.OnTabActivityResultListener;
import com.shengliedu.teacher.teacher.view.NoScrollViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends BaseActivity {
	private NoScrollViewPager viewPager;
	private ViewPagerAdapter adapter;
	private List<View> views = new ArrayList<View>();
	private RadioGroup radioGroup;
	private int current=0;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		File file = new File(Environment
				.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/zzkt/");
		if (!file.exists()){
			file.mkdir();
		}
		MyApplication.isRun=true;
		current=getIntent().getIntExtra("current", 0);
		View view1 = manager.startActivity("tab1",
				new Intent(MainActivity.this, ConsoleActivity.class))
				.getDecorView();
		View view2 = manager
				.startActivity(
						"tab2",
						new Intent(MainActivity.this,
								ChatMsgActivity.class))
				.getDecorView();
		views.add(view1);
		views.add(view2);
		viewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
		adapter = new ViewPagerAdapter(MainActivity.this, views);
		viewPager.setAdapter(adapter);
		viewPager.setNoScroll(true);
		viewPager.setCurrentItem(current, false);
		radioGroup = (RadioGroup) findViewById(R.id.main_group);
		switch (current) {
		case 0:
			((RadioButton) findViewById(R.id.main_btn1)).setChecked(true);
			break;
		case 1:
			((RadioButton) findViewById(R.id.main_btn2)).setChecked(true);
			break;

		default:
			break;
		}
		((RadioButton) findViewById(R.id.main_btn3)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,MyActivity.class);
				startActivity(intent);
			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.main_btn1:
					viewPager.setCurrentItem(0, false);
					break;
				case R.id.main_btn2:
					viewPager.setCurrentItem(1, false);
					break;
				case R.id.main_btn3:
					
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		AutoUpdate autoUpdate=new AutoUpdate();
		autoUpdate.getVersionData(this, "0");
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_main;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
			Log.v("TAG", "5");
			return false;
		}
		Log.v("TAG", "6");
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 */
	private Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Log.v("TAG", "1");
			Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
			Log.v("TAG", "11");
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
					Log.v("TAG", "2");
				}
			}, 2000);
			Log.v("TAG", "3");
		} else {
			Log.v("TAG", "4");
			MyApplication.isRun=false;
			finish();
			System.exit(0);
		}
	}
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		// 判断是否实现返回值接口
		if (arg0 == 0) {
			Activity subActivity = manager.getActivity("tab1");
			Log.v("TAG2", "r=" + subActivity.getLocalClassName()
					+ (subActivity instanceof OnTabActivityResultListener));
			// 获取返回值接口实例
			OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(arg0, arg1, arg2);
		} 
	}
}
