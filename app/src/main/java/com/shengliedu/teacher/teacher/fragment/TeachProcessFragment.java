package com.shengliedu.teacher.teacher.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.ClassPlanAdapter;
import com.shengliedu.teacher.teacher.bean.Beike;

import java.util.List;

public class TeachProcessFragment extends Fragment {
	private View fView;
	private BaseActivity activity;
	private List<Beike> hourArr;
	private ExpandableListView expandableListView;
	private ClassPlanAdapter adapter;
	@SuppressLint("ValidFragment")
	public TeachProcessFragment(BaseActivity activity, List<Beike> hourArr) {
		super();
		this.activity = activity;
		this.hourArr = hourArr;
	}
	public TeachProcessFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fView=inflater.inflate(R.layout.fragment_teach_process, container, false);
		initView();
		return fView;
	}

	@SuppressLint("NewApi")
	private void initView() {
		// TODO Auto-generated method stub
		expandableListView = (ExpandableListView) fView.findViewById(R.id.expandableListView_teach);
		if (hourArr!=null) {
			adapter = new ClassPlanAdapter(activity, hourArr);
			expandableListView.setAdapter(adapter);
			expandableListView
			.setOnGroupCollapseListener(new OnGroupCollapseListener() {
				
				@Override
				public void onGroupCollapse(int arg0) {
					// TODO Auto-generated method stub
					expandableListView.expandGroup(arg0, false);
				}
			});
			for (int i = 0; i < hourArr.size(); i++) {
				expandableListView.expandGroup(i, false);
			}
		}
	}
	
}
