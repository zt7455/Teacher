package com.shengliedu.teacher.teacher.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;

public class TeachThinkFragment extends Fragment {
	private View fView;
	private BaseActivity activity;
	private TextView message_text;
	private String think;
	@SuppressLint("ValidFragment")
	public TeachThinkFragment(BaseActivity activity, String think) {
		super();
		this.activity = activity;
		this.think = think;
	}
	public TeachThinkFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fView=inflater.inflate(R.layout.fragment_think, container, false);
		initView();
		return fView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		message_text=(TextView) fView.findViewById(R.id.message_text);
		if (think!=null) {
			message_text.setText(think);
		}
	}
	
}
