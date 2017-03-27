package com.shengliedu.teacher.teacher.activity;

import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.ShowHomeworkStudentAdapter;
import com.shengliedu.teacher.teacher.bean.IdName;

public class HomeworkShowStudentListActivity extends BaseActivity {

	private ListView listView;
	private ShowHomeworkStudentAdapter showHomeworkStudentAdapter;
	private List<IdName> data;
	private int activityId;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		activityId = getIntent().getIntExtra("activityId", -1);
		data=(List<IdName>) getIntent().getSerializableExtra("students");
		setBack();
		showTitle(getIntent().getStringExtra("t"));
		listView = getView(R.id.listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeworkShowStudentListActivity.this,HomeworkShowAddActivity.class);
				intent.putExtra("activityId", activityId);
				intent.putExtra("userId", data.get(arg2).id);
				intent.putExtra("realname", data.get(arg2).realname);
				startActivityForResult(intent, 600);
			}
		});
		if (data!=null) {
			showHomeworkStudentAdapter = new ShowHomeworkStudentAdapter(this, data);
			listView.setAdapter(showHomeworkStudentAdapter);
		}
	}


	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homework_show_student_list;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		Log.v("TAG", "00:"+arg0+","+arg1);
		if (arg0==600&&arg1==601) {
			setResult(501);
			finish();
		}
	}
	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		
	}
}
