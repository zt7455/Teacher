package com.shengliedu.teacher.teacher.activity;

import android.widget.TextView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

public class ClassroomLogStudentPJOneActivity extends BaseActivity {
	private TextView teacher_pj,teacher_pj_v,teacher_py,teacher_py_v;
	private int behavior;
	private int subject;
	private String behaviorNote;
	private String realname;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		subject = getIntent().getIntExtra("subject", -1);
		behavior = getIntent().getIntExtra("behavior", -1);
		realname = getIntent().getStringExtra("realname");
		behaviorNote = getIntent().getStringExtra("behaviorNote");
		setBack();
		showTitle("评价"+realname);
		teacher_pj=getView(R.id.teacher_pj);
		teacher_pj_v=getView(R.id.teacher_pj_v);
		teacher_py=getView(R.id.teacher_py);
		teacher_py_v=getView(R.id.teacher_py_v);
		if (subject!=-1&&subject!=0) {
			teacher_pj.setText(MyApplication.getSubjectNameForId(subject)+"老师评价:");
			String pj="";
			switch (behavior) {
				case 0:
					pj="未评价";
					break;
			case 1:
				pj="A";
				break;
			case 2:
				pj="B";
				break;
			case 3:
				pj="C";
				break;
			case 4:
				pj="D";
				break;

			default:
				break;
			}
			teacher_pj_v.setText(pj);
			teacher_py.setText(MyApplication.getSubjectNameForId(subject)+"老师评语:");
			teacher_py_v.setText(behaviorNote);
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_classroom_log_pj;
	}

}
