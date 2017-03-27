package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachBookContentAdapter;
import com.shengliedu.teacher.teacher.bean.TeachBookContent;

import java.util.List;

public class TeachBookContentActivity extends BaseActivity {
	private ListView listView;
	private List<TeachBookContent> bookContent;
	private TeachBookContentAdapter adapter;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		bookContent = (List<TeachBookContent>) getIntent().getExtras()
				.getSerializable("datas");
		listView = (ListView) findViewById(R.id.clistView);
		setBack();
		showTitle("目录");
		if (bookContent != null) {
			adapter = new TeachBookContentAdapter(this, bookContent);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					Intent intent = new Intent();
					Log.v("TAG", "c="+bookContent.get(i).page);
					intent.putExtra("position",
							bookContent.get(i).page);
					setResult(200, intent);
					finish();
				}
			});
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_book_content;
	}
}
