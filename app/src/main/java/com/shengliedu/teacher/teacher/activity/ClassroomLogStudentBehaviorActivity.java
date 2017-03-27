package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.SortAdapter;
import com.shengliedu.teacher.teacher.bean.PersonBean;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.PinyinComparator;
import com.shengliedu.teacher.teacher.util.PinyinUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SideBar;
import com.shengliedu.teacher.teacher.util.SideBar.OnTouchingLetterChangedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ClassroomLogStudentBehaviorActivity extends BaseActivity {

	private ListView listView;
	private SortAdapter sortadapter;
	private List<PersonBean> data = new ArrayList<PersonBean>();
	private SideBar sidebar;
	private TextView dialog, all_pj;
	private int activityId;
	private int behavior;
	private int subject;
	private int userId;
	private String classroom_name;
	private String behaviorNote;
	private RelativeLayout t_rel;
	private PersonBean person;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		activityId = getIntent().getIntExtra("activityId", -1);
		behavior = getIntent().getIntExtra("behavior", -1);
		subject = getIntent().getIntExtra("subject", -1);
		userId = getIntent().getIntExtra("userId", -1);
		classroom_name = getIntent().getStringExtra("classroom_name");
		behaviorNote = getIntent().getStringExtra("behaviorNote");
		setBack();
		showTitle(classroom_name + "评价");
		t_rel = getView(R.id.t_rel);
		sidebar = getView(R.id.sidebar);
		listView = getView(R.id.listview);
		dialog = getView(R.id.dialog);
		all_pj = getView(R.id.all_pj);
		String biaoxian = "";
		switch (behavior) {
		case 1:
			biaoxian = "优";
			break;
		case 2:
			biaoxian = "良";
			break;
		case 3:
			biaoxian = "中";
			break;
		case 4:
			biaoxian = "差";
			break;

		default:
			break;
		}
		all_pj.setText(biaoxian);
		sidebar.setTextView(dialog);
		sortadapter = new SortAdapter(this, data);
		listView.setAdapter(sortadapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				person = data.get(arg2);
				Intent intent = new Intent(
						ClassroomLogStudentBehaviorActivity.this,
						ClassroomLogStudentPJOneActivity.class);
				intent.putExtra("behavior", person.evaluate);
				intent.putExtra("subject", subject);
				intent.putExtra("behaviorNote", person.comment);
				intent.putExtra("realname", person.realname);
				startActivity(intent);
			}
		});
		t_rel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						ClassroomLogStudentBehaviorActivity.this,
						ClassroomLogStudentPJALLActivity.class);
				intent.putExtra("behavior", behavior);
				intent.putExtra("subject", subject);
				intent.putExtra("behaviorNote", behaviorNote);
				startActivity(intent);
			}
		});
		sidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// TODO Auto-generated method stub
				int position = sortadapter.getPositionForSelection(s.charAt(0));

				if (position != -1) {
					listView.setSelection(position);
				}
			}
		});
		Collections.sort(data, new PinyinComparator());
		sortadapter = new SortAdapter(this, data);
		listView.setAdapter(sortadapter);
	}
//
//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(arg0, arg1, arg2);
//		if (arg0 == 301 && arg1 == 401) {
//			int behavior = arg2.getIntExtra("behavior", -1);
//			String biaoxian = "";
//			switch (behavior) {
//			case 1:
//				biaoxian = "优";
//				break;
//			case 2:
//				biaoxian = "良";
//				break;
//			case 3:
//				biaoxian = "中";
//				break;
//			case 4:
//				biaoxian = "差";
//				break;
//
//			default:
//				break;
//			}
//			all_pj.setText(biaoxian);
//		} else if (arg0 == 301 && arg1 == 402) {
//			int behavior = arg2.getIntExtra("behavior", -1);
//			if (behavior != -1) {
//				if (person.behavior != behavior) {
//					requestData();
//				}
//			}
//		}
//	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		requestData();
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<PersonBean> listarray = JSON.parseArray(
						(String) msg.obj, PersonBean.class);
				if (!MyApplication.listIsEmpty(listarray)) {
					data.clear();
					for (int i = 0; i < listarray.size(); i++) {
						String pinyin = PinyinUtils
								.getPingYin(listarray.get(i).realname);
						String Fpinyin = pinyin.substring(0, 1)
								.toUpperCase();
						PersonBean person = new PersonBean();
						person.realname = listarray.get(i).realname;
						person.comment = listarray.get(i).comment;
						person.evaluate = listarray.get(i).evaluate;
						person.behavior = listarray.get(i).behavior;
						person.behaviorNote = listarray.get(i).behaviorNote;
						person.id = listarray.get(i).id;
						person.pinyin = pinyin;
						if (Fpinyin.matches("[A-Z]")) {
							person.firstpinyin = Fpinyin;
						} else {
							person.firstpinyin = "#";
						}
						data.add(person);
					}
					Collections.sort(data, new PinyinComparator());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							sortadapter.notifyDataSetChanged();
						}
					});

				}
			}else if (msg.what==2){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activity", activityId);
		map.put("studentBehavior", 1);
		doGet(Config1.getInstance().IP+"activityStudent?", map,
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
		return R.layout.activity_teach_log_student_behavior;
	}

}
