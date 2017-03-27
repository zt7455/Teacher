package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.BookPageAdapter;
import com.shengliedu.teacher.teacher.bean.TeachBookContent;
import com.shengliedu.teacher.teacher.bean.Teachbook;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
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

public class TeachBookPageActivity extends BaseActivity {
	private static String TAG = "TeachBookPageActivity";
	private int outlineId;
	private int bookId;
	private int content_type;
	
	private Gallery gallery;
	public BookPageAdapter imageAdapter;
	private List<Teachbook> urls = new ArrayList<Teachbook>(); //
	private List<TeachBookContent> bookContent = new ArrayList<TeachBookContent>(); //
	private int position;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		outlineId = (Integer) getIntent().getExtras().get("outlineId");
		bookId = (Integer) getIntent().getExtras().get("bookId");
		setBack();
		gallery=getView(R.id.gallery);
		imageAdapter = new BookPageAdapter(bookId,urls, this);
		gallery.setAdapter(imageAdapter);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v("TAG", "Selected:"+arg2);
				position=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
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
						setRightText("目录",new View.OnClickListener() {

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
								Intent intent=new Intent(TeachBookPageActivity.this,TeachBookContentActivity.class);
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
					for (int i = 0; i < books.size(); i++) {
//						urls.add(dataBook.get(i).host+dataBook.get(i).link);
						urls.add(books.get(i));
					}
				}
				imageAdapter.notifyDataSetChanged();
				String user_book_page=SharedPreferenceTool.getUserBookPage(TeachBookPageActivity.this,MyApplication.userInfo.id+"_"+bookId);
				Log.v("TAG", "user_book_page:"+user_book_page);
				if (!TextUtils.isEmpty(user_book_page)) {
					int position=Integer.parseInt(user_book_page.split("_")[2]);
					if (urls.size()>position) {
						gallery.setSelection(position);
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
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("TAG", "onPause:"+position);
		if (position!=0) {
			SharedPreferenceTool.setUserBookPage(this, MyApplication.userInfo.id+"_"+bookId,MyApplication.userInfo.id+"_"+bookId+"_"+position);
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0==199 && arg1==200) {
			int position=arg2.getIntExtra("position", gallery.getSelectedItemPosition());
			if (position != gallery.getSelectedItemPosition()) {
				gallery.setSelection(position);
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
					public void onResponse(Call call, Response response,String json){
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
		return R.layout.activity_textbook;
	}

}
