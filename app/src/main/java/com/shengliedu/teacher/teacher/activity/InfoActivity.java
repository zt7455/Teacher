package com.shengliedu.teacher.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import android.widget.GridView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.ConsoleMenuAdapter;
import com.shengliedu.teacher.teacher.bean.ConsoleMenu;

public class InfoActivity extends BaseActivity {
	private GridView grid_select;
	private ConsoleMenuAdapter adapter;
	private List<ConsoleMenu> list = new ArrayList<ConsoleMenu>();
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		grid_select=getView(R.id.grid_select);
		grid_select.setFocusable(false);
		adapter=new ConsoleMenuAdapter(this, list);
		grid_select.setAdapter(adapter);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_console;
	}

}
