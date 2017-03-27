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
import com.shengliedu.teacher.teacher.bean.Beike;

import java.util.List;

public class TeachPJFragment extends Fragment {
	private View fView;
	private BaseActivity activity;
	private List<Beike> hourArr;
	private int research_score;
	private TextView jwpj_content,jypj_content;
	@SuppressLint("ValidFragment")
	public TeachPJFragment(BaseActivity activity, List<Beike> hourArr, int research_score) {
		super();
		this.activity = activity;
		this.hourArr = hourArr;
		this.research_score = research_score;
	}
	public TeachPJFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fView=inflater.inflate(R.layout.fragment_teach_pj, container, false);
		initView();
		return fView;
	}

	@SuppressLint("NewApi")
	private void initView() {
		// TODO Auto-generated method stub
		jwpj_content=(TextView) fView.findViewById(R.id.jwpj_content);
		jypj_content=(TextView) fView.findViewById(R.id.jypj_content);
		if (hourArr!=null && hourArr.size()>0) {
			String jwpjText="";
			for (int i = 0; i < hourArr.size(); i++) {
				jwpjText+="第"+hourArr.get(i).order+"课时"+"-----------------"+(hourArr.get(i).finish==1?"已完成":"未完成")+"\n";
			}
			jwpj_content.setText(jwpjText);
		}
		String jypjText="";
		if (research_score==0) {
			jypjText="未评价";
		}else if (research_score==1) {
			jypjText="优";
		}else if (research_score==2) {
			jypjText="良";
		}else if (research_score==3) {
			jypjText="合格";
		}
		jypj_content.setText(jypjText);
	}
	
}
