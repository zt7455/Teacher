package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.TeachBookContent;
import com.shengliedu.teacher.teacher.bean.Teachbook;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.fragment.BookPageHtmFragment;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class TeachBookPageHtmActivity extends BaseActivity {
	private static String TAG = "TeachBookPageActivity";
	private int outlineId;
	private int bookId;
	
	private List<Teachbook> urls = new ArrayList<Teachbook>(); //
	private List<TeachBookContent> bookContent = new ArrayList<TeachBookContent>(); //
	private ViewPager textbook_pager;
	private List<Fragment> fragments;
	private ViewPagerAdapter adapter;
	private int position;;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		outlineId = (Integer) getIntent().getExtras().get("outlineId");
		bookId = (Integer) getIntent().getExtras().get("bookId");
		setBack();
		textbook_pager = (ViewPager) findViewById(R.id.textbook_pager);
		textbook_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				position = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("TAG", "onPause:"+position);
		if (position!=0) {
			SharedPreferenceTool.setUserBookPage(this,MyApplication.userInfo.id+"_"+bookId, MyApplication.userInfo.id+"_"+bookId+"_"+position);
		}
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		getBookPage();
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<TeachBookContent> mainbookContent=JSON.parseArray((String) msg.obj, TeachBookContent.class);
				bookContent.clear();
				if (mainbookContent!=null && mainbookContent.size()>0) {
					for (TeachBookContent teachBookContent:mainbookContent){
						if (teachBookContent.parent!=0){
							bookContent.add(teachBookContent);
						}
					}
					if (!MyApplication.listIsEmpty(bookContent) && !MyApplication.listIsEmpty(urls)) {
						setRightText("目录",new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								for (int i = 0; i < bookContent.size(); i++) {
									for (int k = 0; k < urls.size(); k++) {
										if (bookContent.get(i).page == -1 && bookContent.get(i).id == urls.get(k).outline_id) {
											bookContent.get(i).page=k;
										}
									}
								}
								Intent intent=new Intent(TeachBookPageHtmActivity.this,TeachBookContentActivity.class);
								Bundle bundle=new Bundle();
								bundle.putSerializable("datas", (Serializable) bookContent);
								intent.putExtras(bundle);
								startActivityForResult(intent, 199);
							}
						});
					}else {
						if (MyApplication.listIsEmpty(bookContent)) {
							toast("目录为空");
						}
						if (MyApplication.listIsEmpty(urls)) {
							toast("书页为空");
						}
					}
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				List<Teachbook> books=JSON.parseArray((String) msg.obj, Teachbook.class);
				urls.clear();
				if (books!=null) {
					fragments = new ArrayList<Fragment>();
					for (int i = 0; i < books.size(); i++) {
//						urls.add(dataBook.get(i).host+dataBook.get(i).link);
						urls.add(books.get(i));
						BookPageHtmFragment fragment = new BookPageHtmFragment(urls.get(i).link);
						fragments.add(fragment);
					}
				}
				adapter = new ViewPagerAdapter(getSupportFragmentManager());
				textbook_pager.setAdapter(adapter);
				String user_book_page=SharedPreferenceTool.getUserBookPage(TeachBookPageHtmActivity.this,MyApplication.userInfo.id+"_"+bookId);
				if (!TextUtils.isEmpty(user_book_page)) {
					int position=Integer.parseInt(user_book_page.split("_")[2]);
					if (urls.size()>position) {
						textbook_pager.setCurrentItem(position);
					}
				}
				getBookContent();
			}else if (msg.what==4){

			}
		}
	};
	private void getBookContent() {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("outlineId", outlineId);
		doGet(Config1.MAIN_BOOK_IP + "/outline?", map1,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(4);
					}

					@Override
					public void onResponse(Call call, Response response,String json){
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0==199 && arg1==200) {
			int position=arg2.getIntExtra("position",textbook_pager.getCurrentItem());
			if (position != textbook_pager.getCurrentItem()) {
				textbook_pager.setCurrentItem(position);
			}
		}
	}
	private void getBookPage() {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("outlineId", outlineId);
		map1.put("bookId", bookId);
		doGet(Config1.MAIN_BOOK_IP + "/oe?", map1,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(4);
					}

					@Override
					public void onResponse(Call call, Response response,String json)  {
						Message message=Message.obtain();
						message.what=3;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_ziyuan;
	}


	private BookPageHtmFragment fragment;

	class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			fragment = (BookPageHtmFragment) fragments.get(arg0);
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.size();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			fragment = (BookPageHtmFragment) adapter.getItem(position);
			Log.v("TAG", "1");
			boolean b=fragment.onKeyDown(keyCode, event);
			if (b) {
				return b;
			}else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
