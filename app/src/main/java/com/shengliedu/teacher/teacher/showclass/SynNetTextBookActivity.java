package com.shengliedu.teacher.teacher.showclass;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.ImageAdapter;
import com.shengliedu.teacher.teacher.bean.BeanTextBook;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class SynNetTextBookActivity extends BaseActivity {
	private String name;
	private String content_host;
	private int id;
	private int book;
	private int content_type;
	
	private Gallery gallery;
	public ImageAdapter imageAdapter;
	private List<BeanTextBook> dataBook=new ArrayList<BeanTextBook>();
	private List<String> urls = new ArrayList<String>(); //
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		id = (Integer) getIntent().getExtras().get("id");
		book = (Integer) getIntent().getExtras().get("book");
		content_type = (Integer) getIntent().getExtras().get("content_type");
		name = (String) getIntent().getExtras().get(
				"name");
		content_host = (String) getIntent().getExtras().get(
				"content_host");
		setBack();
		showTitle(name);
		gallery=getView(R.id.gallery);
		imageAdapter = new ImageAdapter(urls, this);
		gallery.setAdapter(imageAdapter);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject object=JSON.parseObject((String) msg.obj);
				dataBook=JSON.parseArray(object.getString("data"), BeanTextBook.class);
				if (dataBook!=null) {
					urls.clear();
					for (int i = 0; i < dataBook.size(); i++) {
//						urls.add(dataBook.get(i).host+dataBook.get(i).link);
						urls.add(dataBook.get(i).link);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								imageAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}else if (msg.what==2){

			}
		}
	};
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("content_type", content_type);
		map1.put("id", id);
		map1.put("book", book);
		doGet(Config1.IP + Config1.getInstance().SHOWCLASSSINGLE(), map1,
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
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_textbook;
	}

}
