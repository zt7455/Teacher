package com.shengliedu.teacher.teacher.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.Beike;
import com.shengliedu.teacher.teacher.bean.Grade_Subject_classroomType;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.teachbooks;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.fragment.TeachDaoRuFragment;
import com.shengliedu.teacher.teacher.fragment.TeachPJFragment;
import com.shengliedu.teacher.teacher.fragment.TeachProcessFragment;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.PopupWindowUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;
import com.shengliedu.teacher.teacher.view.SmallPickerView;
import com.shengliedu.teacher.teacher.view.SmallPickerView.onSelectListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class BKbrowseActivity extends BaseActivity {
	private TextView title1;
	private RadioGroup title_rg;
	private RadioButton teacher1, teacher2, teacher3;

	private PopupWindow PopupWindow;// 弹出的课时选择框
	private List<IdName> contents = new ArrayList<IdName>();
	private List<Grade_Subject_classroomType> user_grade_Subject_classroomTypes = new ArrayList<Grade_Subject_classroomType>();
	private Grade_Subject_classroomType value;

	private int gradeId;
	private int subjectId;
	private int classroomTypeId;
	private int userId;

	private List<Beike> guide = new ArrayList<Beike>();
	private List<Beike> reflection = new ArrayList<Beike>();
	private List<Beike> hourArr = new ArrayList<Beike>();

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("备课浏览");
		title1 = getView(R.id.title1);
		title_rg = getView(R.id.title_rg);
		teacher1 = getView(R.id.teacher1);
		teacher2 = getView(R.id.teacher2);
		teacher3 = getView(R.id.teacher3);
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
				requestData();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 200 && arg1 == 301) {
			if (arg2 != null) {
				gradeId = arg2.getIntExtra("gradeId", 0);
				subjectId = arg2.getIntExtra("subjectId", 0);
				classroomTypeId = arg2.getIntExtra("classroomTypeId", 0);
				userId = arg2.getIntExtra("userId", 0);
				if (gradeId != 0 && subjectId != 0 && classroomTypeId != 0
						&& userId != 0) {
					setRightText(MyApplication.getGradeNameForId(gradeId)
							+ "-"
							+ MyApplication.getSubjectNameForId(subjectId)
							+ "\n"
							+ MyApplication
									.getClassroomTypeNameForId(classroomTypeId));
					requestData();
				}
			}
		}
	}

	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				JSONObject jsonObject=JSON.parseObject((String) msg.obj);
				if (!TextUtils.isEmpty(jsonObject.getString("teachOutlines"))){
					List<IdName> idNames= JSON.parseArray(jsonObject.getString("teachOutlines"),IdName.class);
					contents.clear();
					if (idNames!=null && idNames.size()>0){
						int parentId=0;
						for (IdName idName:idNames){
							if (idName.parent==0){
								parentId=idName.id;
							}
						}
						for (IdName idName:idNames){
							if (idName.parent==parentId && !"封面".equals(idName.name)
									&& !"目录1".equals(idName.name)
									&& !"封底".equals(idName.name)
									&& !"扉页".equals(idName.name)
									&& !"目录".equals(idName.name)){
								idName.items=new ArrayList<>();
								contents.add(idName);
							}
						}
						for (int i=0;i<contents.size();i++){
							for (int j=0;j<idNames.size();j++){
								if (contents.get(i).id==idNames.get(j).parent){
									contents.get(i).items.add(idNames.get(j));
								}
							}
						}
					}
					if (contents.size()>0){
					 onePosition=0;
				     twoPosition=0;
					 title1.setText(contents.get(0).name
										+ "-"
										+ contents.get(onePosition).items
										.get(twoPosition).name);
						title1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										initDateView(title1);
										PopupWindow = PopupWindowUtils
												.showCamerPopupWindow(
														dateSelected,
														R.layout.activity_bk_browse,
														BKbrowseActivity.this);
									}
								});
						requestData2();
					}
				}
			}else if (msg.what==2){

			}else if (msg.what==3){
				final Beike beikedata = JSON.parseObject((String) msg.obj, Beike.class);
				if (beikedata != null) {// GO
					// title1.setText(contents.get(onePosition).name + "-"
					// +
					// contents.get(onePosition).items.get(twoPosition).name);
					guide.clear();
					guide.addAll(beikedata.analysisArr);
					hourArr.clear();
					hourArr.addAll(beikedata.hours);
					reflection.clear();
					reflection.addAll(beikedata.reflectionArr);
							teacher1.setChecked(true);
							final int research_score = beikedata.research_score;
							getSupportFragmentManager()
									.beginTransaction()
									.replace(
											R.id.fragment_content,
											new TeachDaoRuFragment(
													BKbrowseActivity.this,
													guide)).commit();

							title_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(RadioGroup arg0,
															 int arg1) {
									// TODO Auto-generated method stub
									switch (arg1) {
										case R.id.teacher1:
											getSupportFragmentManager()
													.beginTransaction()
													.replace(
															R.id.fragment_content,
															new TeachDaoRuFragment(
																	BKbrowseActivity.this,
																	guide))
													.commit();
											break;
										case R.id.teacher2:
											getSupportFragmentManager()
													.beginTransaction()
													.replace(
															R.id.fragment_content,
															new TeachProcessFragment(
																	BKbrowseActivity.this,
																	hourArr))
													.commit();
											break;
										case R.id.teacher3:
											getSupportFragmentManager()
													.beginTransaction()
													.replace(
															R.id.fragment_content,
															new TeachDaoRuFragment(
																	BKbrowseActivity.this,
																	reflection))
													.commit();
											break;
										case R.id.teacher4:
											getSupportFragmentManager()
													.beginTransaction()
													.replace(
															R.id.fragment_content,
															new TeachPJFragment(
																	BKbrowseActivity.this,
																	hourArr,
																	research_score)).commit();
											break;

										default:
											break;
									}
								}
							});
				}
			}else if (msg.what==4){

			}else if (msg.what==5){
                final List<teachbooks> teachbookses= JSON.parseArray((String) msg.obj,teachbooks.class);
				if (teachbookses!=null && teachbookses.size()>0){
					if (teachbookses.size()==1){
						teachBookId=teachbookses.get(0).id;
						teachBookName=teachbookses.get(0).name;
						requestTeachBookData();
					}else if (teachbookses.size()>1){
						teachBookId=teachbookses.get(0).id;
						teachBookName=teachbookses.get(0).name;
						requestTeachBookData();
						setRightText(MyApplication
								.getGradeNameForId(teachbookses
										.get(0).grade) + "-" + MyApplication
								.getSubjectNameForId(teachbookses
										.get(0).subject), new OnClickListener() {
							@Override
							public void onClick(View view) {
								AlertDialog.Builder builder3 = new AlertDialog.Builder(
										BKbrowseActivity.this);
								builder3.setTitle("我的备课");
								// 给对话框设定单选列表项
								int size = teachbookses.size();
								String[] grades = new String[size];
								for (int i = 0; i < teachbookses
										.size(); i++) {
									grades[i] = MyApplication
											.getGradeNameForId(teachbookses
													.get(i).grade)+"-"+MyApplication
											.getSubjectNameForId(teachbookses
													.get(i).subject);
								}
								builder3.setSingleChoiceItems(grades, 0,
										new AlertDialog.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												setRightText(MyApplication
														.getGradeNameForId(teachbookses
																.get(which).grade)+"-"+MyApplication
														.getSubjectNameForId(teachbookses
																.get(which).subject));

												if (teachbookses.get(which).id!=teachBookId){
													teachBookId=teachbookses.get(which).id;
													teachBookName=teachbookses.get(which).name;
													requestTeachBookData();
												}
												dialog.dismiss();
											}
										});
								dialog = builder3.show();
								dialog.show();
							}
						});

					}
				}
			}else if (msg.what==6){

			}
		}
	};
	private int teachBookId;
	private String teachBookName;
	private AlertDialog dialog;
	private void requestData() {
		// TODO Auto-generated method stub
//		teachbooks？userId=1&semesterId=100
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("checkUserTeachBook", 1);
		map.put("userId", MyApplication.userInfo.id);
		map.put("semesterId", MyApplication.userInfo.currentSemester.id);
		map.put("schoolId", MyApplication.beanSchoolInfo.id);
		doGet(Config1.getInstance().IP+"teachbooks?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(6);
					}

					@Override
					public void onResponse(Call call, Response response,String json) {
						Message message=Message.obtain();
						message.what=5;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}
	private void requestTeachBookData() {
		// TODO Auto-generated method stub
//		teachoutline?teachBookId=100
		if (!TextUtils.isEmpty(teachBookName)){
			showTitle(teachBookName.length()>5?(teachBookName.substring(0,5)+"..."):teachBookName);
		}else {
			showTitle("");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teachBookId", teachBookId);
		doGet(Config1.getInstance().IP + "teachoutline?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response, String json) {
						Message message = Message.obtain();
						message.what = 1;
						message.obj = json;
						handlerReq.sendMessage(message);
					}
				});
	}
	private void requestData2() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
//		teachoutline/100?appCheckData=1
		map.put("appCheckData", 1);
		doGet(Config1.getInstance().IP+"teachoutline/"+contents.get(onePosition).items.get(twoPosition).id+"?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(4);
					}

					@Override
					public void onResponse(Call call, Response response,String json) {
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
		return R.layout.activity_bk_browse;
	}

	// --------------------------选择器----------------

	private View dateSelected;
	private LayoutInflater layoutInflater;
	private int onePosition, twoPosition;
	private SmallPickerView date_year, date_month;
	// private PickerView date_hour, date_minute;
	private List<String> oneDir = new ArrayList<String>();
	private List<String> twoDir = new ArrayList<String>();

	private void initDateView(final TextView text) {
		// TODO Auto-generated method stub
		// 时间选择器
		layoutInflater = LayoutInflater.from(this);
		dateSelected = layoutInflater.inflate(R.layout.appointment_select_ke,
				null);
		// 选择开始时间
		dateSelected.findViewById(R.id.button_cacel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (PopupWindow != null) {
							PopupWindow.dismiss();
						}
					}
				});
		dateSelected.findViewById(R.id.button_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						updateTime(text);
						onePosition = 0;
						twoPosition = 0;
						if (PopupWindow != null) {
							PopupWindow.dismiss();
						}
					}
				});
		date_year = (SmallPickerView) dateSelected
				.findViewById(R.id.kemu_select);
		date_month = (SmallPickerView) dateSelected
				.findViewById(R.id.nianji_select);
		initData();
		setData();
	}

	private void initData() {
		// 年的数组
		oneDir = new ArrayList<String>();
		for (int i = 0; i < contents.size(); i++) {
			oneDir.add(contents.get(i).name);
		}
		System.out.println(oneDir.toString());
		if (!MyApplication.listIsEmpty(contents.get(0).items)) {
			twoDir.clear();
			for (int j = 0; j < contents.get(0).items.size(); j++) {
				twoDir.add(contents.get(onePosition).items.get(j).name);
			}
		}
		System.out.println(twoDir.toString());
	}

	private void setData() {
		date_year.setData(oneDir);
		date_year.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				for (int i = 0; i < contents.size(); i++) {
					if (contents.get(i).name.endsWith(text)) {
						onePosition = i;
						twoDir.clear();
						if (!MyApplication.listIsEmpty(contents.get(i).items)) {
							for (int j = 0; j < contents.get(i).items.size(); j++) {
								twoDir.add(contents.get(i).items.get(j).name);
							}
						}
						System.out.println(twoDir.toString());
						Log.v("TAG", "v=" + twoDir.get(0));
						date_month.setData(twoDir);
						date_month.setSelected(twoDir.get(0));
					}
					// System.out.println(twoDir.toString());
				}
			}
		});
		date_month.setData(twoDir);
		date_month.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				List<IdName> two = contents.get(onePosition).items;
				for (int i = 0; i < two.size(); i++) {
					if (two.get(i).name.endsWith(text)) {
						twoPosition = i;
					}
				}
			}
		});
		Log.v("TAG",
				"onePosition=" + onePosition + ",v=" + oneDir.get(onePosition));
		Log.v("TAG",
				"twoPosition=" + twoPosition + ",v=" + twoDir.get(twoPosition));
		date_year.setSelected(oneDir.get(onePosition));
		date_month.setSelected(twoDir.get(twoPosition));
	}

	public void updateTime(TextView tv) {
		tv.setText(contents.get(onePosition).name + "-"
				+ contents.get(onePosition).items.get(twoPosition).name);
		SharedPreferenceTool.setTeachBkll(BKbrowseActivity.this,
				userId+"_"+gradeId+"_"+subjectId+"_"+userId+"_"+classroomTypeId+"_"+contents.get(onePosition).name+"_"+contents.get(onePosition).items.get(twoPosition).name);
		requestData2();
	}
}
