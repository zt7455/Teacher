package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachLogAdapter;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.DateBean;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.TeachLogList;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.util.DateUtil;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.PopupWindowUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.view.PickerView;
import com.shengliedu.teacher.teacher.view.PickerView.onSelectListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ClassroomLogActivity extends BaseActivity implements OnClickListener {
	private TextView title1;
	private ListView week_plan_list;
	private TeachLogAdapter teachLogAdapter;
	private List<TeachLogList> list = new ArrayList<TeachLogList>();

	private PopupWindow PopupWindow;// 弹出的时间选择框

	private LinearLayout lin1, lin2, lin3, lin4, lin5, lin6, lin7;
	private TextView day1, day2, day3, day4, day5, day6, day7, week1, week2,
			week3, week4, week5, week6, week7;
	private LinearLayout[] lins = { lin1, lin2, lin3, lin4, lin5, lin6, lin7 };
	private TextView[] days = { day1, day2, day3, day4, day5, day6, day7 };
	private TextView[] weeks = { week1, week2, week3, week4, week5, week6,
			week7 };
	private int[] linsId = { R.id.lin1, R.id.lin2, R.id.lin3, R.id.lin4,
			R.id.lin5, R.id.lin6, R.id.lin7 };
	private int[] daysId = { R.id.day1, R.id.day2, R.id.day3, R.id.day4,
			R.id.day5, R.id.day6, R.id.day7 };
	private int[] weeksId = { R.id.week1, R.id.week2, R.id.week3, R.id.week4,
			R.id.week5, R.id.week6, R.id.week7 };
	private DateBean[] dates = { new DateBean(), new DateBean(),
			new DateBean(), new DateBean(), new DateBean(), new DateBean(),
			new DateBean() };
	Map<Boolean, Object> map = new HashMap<Boolean, Object>();

	private String dateString = "";
	private int classroomId;

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		showTitle("班级日志");
		for (int i = 0; i < lins.length; i++) {
			lins[i] = getView(linsId[i]);
			days[i] = getView(daysId[i]);
			weeks[i] = getView(weeksId[i]);
		}
		map.put(true, -1);
		if (ConsoleActivity.roleJiaowu) {
			List<ClassRoom> classroomDic=MyApplication.classroomDic;
			if (!MyApplication.listIsEmpty(classroomDic)) {
				classroomId = classroomDic.get(0).id;
				setRightText(MyApplication.getClassRoomNameForclassroomid(classroomDic.get(0).id),
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								// 教务
								Intent intent = new Intent(ClassroomLogActivity.this,
										SelectGradeClassroom2Activity.class);
								startActivityForResult(intent, 300);
							}
						});
			}
		} else if (ConsoleActivity.roleNianjizu) {
			List<IdName> list=MyApplication.userInfo.leadGrades;
			List<ClassRoom> classroomDic=MyApplication.classroomDic;
			if (!MyApplication.listIsEmpty(list)&&!MyApplication.listIsEmpty(classroomDic)) {
				c:for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < classroomDic.size(); j++) {
						if (list.get(i).id==classroomDic.get(j).grade) {
							classroomId = classroomDic.get(j).id;
							setRightText(MyApplication.getClassRoomNameForclassroomid(classroomDic.get(j).id),
									new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											// 教务
											Intent intent = new Intent(ClassroomLogActivity.this,
													SelectGradeClassroom2Activity.class);
											startActivityForResult(intent, 300);
										}
									});
							break c;
						}
					}
				}
			}
		} else if (ConsoleActivity.roleBanzhuren) {
			List<ClassRoom>  list=MyApplication.userInfo.leadClassrooms;
			if (!MyApplication.listIsEmpty(list)) {
				classroomId = list.get(0).id;
				setRightText(MyApplication.getClassRoomNameForclassroomid(list.get(0).id),
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								// 教务
								Intent intent = new Intent(ClassroomLogActivity.this,
										SelectGradeClassroom2Activity.class);
								startActivityForResult(intent, 300);
							}
						});
			}
		}
//		List<ClassRoom> leadClassrooms=MyApplication.userInfo.leadClassrooms;
//		if (!MyApplication.listIsEmpty(leadClassrooms)) {
//			classroomId = leadClassrooms.get(0).id;
//		}
		title1 = getView(R.id.title1);
		initDate(title1);
		week_plan_list = getView(R.id.week_plan_list);
		teachLogAdapter = new TeachLogAdapter(this, list);
		week_plan_list.setAdapter(teachLogAdapter);
		week_plan_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TeachLogList teachlog=list.get(arg2);
				Intent intent = new Intent(ClassroomLogActivity.this,ClassroomLogStudentBehaviorActivity.class);
				intent.putExtra("activityId", teachlog.id);
				intent.putExtra("subject", teachlog.subject);
				intent.putExtra("behavior", teachlog.behavior);
				intent.putExtra("behaviorNote", teachlog.behaviorNote);
				intent.putExtra("classroom_name", teachlog.classroom_name);
				intent.putExtra("classroomId", classroomId);
				startActivity(intent);
			}
		});
		title1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDateView(title1);
				PopupWindow = PopupWindowUtils.showCamerPopupWindow(
						dateSelected, R.layout.activity_week_plan,
						ClassroomLogActivity.this);
			}
		});
		for (int i = 0; i < lins.length; i++) {
			lins[i].setOnClickListener(this);
		}
//		if (!MyApplication.listIsEmpty(leadClassrooms)&&(ConsoleActivity.roleJiaowu || ConsoleActivity.roleNianjizu)) {
//			setRightText(leadClassrooms.get(0).name,
//					new OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							// 教务
//							Intent intent = new Intent(ClassroomLogActivity.this,
//									SelectGradeClassroomActivity.class);
//							startActivityForResult(intent, 300);
//						}
//					});
//		}
		requestData();
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_teach_log;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 300 && arg1 == 201) {
			int id = classroomId;
			classroomId = arg2.getIntExtra("classroomId", classroomId);
			if (classroomId != id) {
				setRightText(MyApplication.getClassRoomNameForclassroomid(classroomId));
				requestData();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int l = (int) map.get(true);
		if (l != -1) {
			lins[l].setBackgroundResource(R.drawable.text_border1);
			days[l].setTextColor(getResources().getColor(R.color.text_dark2));
			weeks[l].setTextColor(getResources().getColor(R.color.text_dark2));
		}
		map.clear();
		switch (v.getId()) {
		case R.id.lin1:
			if (MyApplication.userInfo.currentDate.equals(dates[0].date)) {

			} else {
				lins[0].setBackgroundResource(R.color.seagreen);
				days[0].setTextColor(getResources().getColor(R.color.white));
				weeks[0].setTextColor(getResources().getColor(R.color.white));
			}
			map.put(true, 0);
			dateString = dates[0].date;
			if (l != 0) {
				requestData();
			}
			break;
		case R.id.lin2:
			if (MyApplication.userInfo.currentDate.equals(dates[1].date)) {

			} else {
				lins[1].setBackgroundResource(R.color.seagreen);
				days[1].setTextColor(getResources().getColor(R.color.white));
				weeks[1].setTextColor(getResources().getColor(R.color.white));
			}
			map.put(true, 1);
			dateString = dates[1].date;
			if (l != 1) {
				requestData();
			}
			break;
		case R.id.lin3:
			if (MyApplication.userInfo.currentDate.equals(dates[2].date)) {

			} else {
				lins[2].setBackgroundResource(R.color.seagreen);
				days[2].setTextColor(getResources().getColor(R.color.white));
				weeks[2].setTextColor(getResources().getColor(R.color.white));
			}
			map.put(true, 2);
			dateString = dates[2].date;
			if (l != 2) {
				requestData();
			}
			break;
		case R.id.lin4:
			if (MyApplication.userInfo.currentDate.equals(dates[3].date)) {

			} else {
				lins[3].setBackgroundResource(R.color.seagreen);
				days[3].setTextColor(getResources().getColor(R.color.white));
				weeks[3].setTextColor(getResources().getColor(R.color.white));
			}
			map.put(true, 3);
			dateString = dates[3].date;
			if (l != 3) {
				requestData();
			}
			break;
		case R.id.lin5:
			if (MyApplication.userInfo.currentDate.equals(dates[4].date)) {

			} else {
				lins[4].setBackgroundResource(R.color.seagreen);
				days[4].setTextColor(getResources().getColor(R.color.white));
				weeks[4].setTextColor(getResources().getColor(R.color.white));
			}
			map.put(true, 4);
			dateString = dates[4].date;
			if (l != 4) {
				requestData();
			}
			break;
		case R.id.lin6:
			if (MyApplication.userInfo.currentDate.equals(dates[5].date)) {

			} else {
				lins[5].setBackgroundResource(R.color.seagreen);
				days[5].setTextColor(getResources().getColor(R.color.white));
				weeks[5].setTextColor(getResources().getColor(R.color.white));
			}
			map.put(true, 5);
			dateString = dates[5].date;
			if (l != 5) {
				requestData();
			}
			break;
		case R.id.lin7:
			if (MyApplication.userInfo.currentDate.equals(dates[6].date)) {

			} else {
				lins[6].setBackgroundResource(R.color.seagreen);
				days[6].setTextColor(getResources().getColor(R.color.white));
				weeks[6].setTextColor(getResources().getColor(R.color.white));
			}
			map.put(true, 6);
			dateString = dates[6].date;
			if (l != 6) {
				requestData();
			}
			break;

		default:
			break;
		}
		for (int i = 0; i < dates.length; i++) {
			if (MyApplication.userInfo.currentDate.equals(dates[i].date)) {
				lins[i].setBackgroundResource(R.color.text_dark2);
				days[i].setTextColor(getResources().getColor(R.color.white));
				weeks[i].setTextColor(getResources().getColor(R.color.white));
			}
		}
	}

	private void initDateView(final TextView text) {
		// TODO Auto-generated method stub
		// 时间选择器
		layoutInflater = LayoutInflater.from(this);
		dateSelected = layoutInflater.inflate(
				R.layout.appointment_dateselect_windowview, null);
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
						if (PopupWindow != null) {
							PopupWindow.dismiss();
						}
					}
				});
		date_year = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_year);
		date_month = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_month);
		date_day = (PickerView) dateSelected
				.findViewById(R.id.appoint_date_day);
		initData();
		setData();
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				List<TeachLogList> list1 = JSON.parseArray((String) msg.obj,
						TeachLogList.class);
				list.clear();
				if (list1 != null && list1.size()>0) {
					list.addAll(list1);
				}
				teachLogAdapter.notifyDataSetChanged();
			}else if (msg.what==2){

			}
		}
	};
	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", dateString);
		map.put("classroom", classroomId);
		map.put("withName", 1);
		doGet(Config1.getInstance().IP+"activity?", map,
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
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("date", dateString);
//		map.put("classroomId", classroomId);
//		doGet(Config1.getInstance().CLASSROOM_LOG_LIST(), map,
//				new ResultCallback() {
//					@Override
//					public void onFailure(Call call, IOException e) {
//						handlerReq.sendEmptyMessage(2);
//					}
//
//					@Override
//					public void onResponse(Call call, Response response,String json) {
//						Message message=Message.obtain();
//						message.what=1;
//						message.obj=json;
//						handlerReq.sendMessage(message);
//					}
//				});
	}

	// --------------------------时间选择器----------------

	private View dateSelected;
	private LayoutInflater layoutInflater;
	private int year, month, day, hour, minute;
	private PickerView date_year, date_month, date_day;
	// private PickerView date_hour, date_minute;
	private List<String> years, months, days_b, days_m, days_s, days_ss, hours,
			minutes;

	// private TextView selected_date;// 展示时间的控件

	private void initData() {
		// 年的数组
		years = new ArrayList<String>();
		for (int i = 1949; i < 2100; i++) {
			years.add(i + "年");
		}
		// 月的数组
		months = new ArrayList<String>();
		for (int i = 1; i < 13; i++) {
			months.add(i < 10 ? "0" + i + "月" : i + "月");
		}
		// 日的数组（31）
		days_b = new ArrayList<String>();
		days_m = new ArrayList<String>();
		days_s = new ArrayList<String>();
		days_ss = new ArrayList<String>();
		for (int i = 1; i < 32; i++) {
			days_b.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			if (i < 31) {
				days_m.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			}
			if (i < 30) {
				days_s.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			}
			if (i < 29) {
				days_ss.add(i < 10 ? "0" + i + "日" : "" + i + "日");
			}
		}

		hours = new ArrayList<String>();
		for (int i = 0; i <= 23; i++) {
			hours.add(i < 10 ? "0" + i : "" + i);
		}

		minutes = new ArrayList<String>();
		for (int i = 0; i <= 59; i++) {
			minutes.add(i < 10 ? "0" + i : "" + i);
		}
	}

	private void setData() {
		date_year.setData(years);
		date_year.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				year = Integer.parseInt(text.substring(0, 4));
				// updateTime(selected_date);
			}
		});
		date_month.setData(months);
		date_month.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				month = Integer.parseInt(text.substring(0, 2));
				if (year % 4 != 0 && year % 400 != 0) {
					if (month == 2) {
						date_day.setData(days_s);
					} else if (month == 4 || month == 6 || month == 9
							|| month == 11) {
						date_day.setData(days_m);
					} else {
						date_day.setData(days_b);
					}
				} else {
					if (month == 2) {
						date_day.setData(days_ss);
					} else if (month == 4 || month == 6 || month == 9
							|| month == 11) {
						date_day.setData(days_m);
					} else {
						date_day.setData(days_b);
					}
				}
				date_day.setSelected(day + "");
				// updateTime(selected_date);
			}
		});
		date_day.setData(days_b);
		date_day.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				day = Integer.parseInt(text.substring(0, 2));
				// updateTime(selected_date);
			}
		});
		// 设置PickerView的起始时间
		date_year.setSelected(year - 1949);
		date_month.setSelected(month - 1);
		date_day.setSelected(day - 1);
		// date_hour.setSelected(hour - 1);
		// date_minute.setSelected(minute - 1);
	}

	public void updateTime(TextView tv) {
		String month_ = "";
		if (month < 10) {
			month_ = "0" + month;
		} else {
			month_ = "" + month;
		}

		String day_ = "";
		if (day < 10) {
			day_ = "0" + day;
		} else {
			day_ = "" + day;
		}

		tv.setText(year + "年" + month_ + "月" + day_ + "日");
		String selectDate = year + "-" + month_ + "-" + day_;
		if (!selectDate.equals(dateString)) {
			dates[0].date = DateUtil.getSpecifiedDayBefore(selectDate, 3);
			dates[1].date = DateUtil.getSpecifiedDayBefore(selectDate, 2);
			dates[2].date = DateUtil.getSpecifiedDayBefore(selectDate, 1);
			dates[3].date = selectDate;
			dates[4].date = DateUtil.getSpecifiedDayAfter(selectDate, 1);
			dates[5].date = DateUtil.getSpecifiedDayAfter(selectDate, 2);
			dates[6].date = DateUtil.getSpecifiedDayAfter(selectDate, 3);
			for (int i = 0; i < dates.length; i++) {
				dates[i].day = dates[i].date.substring(8);
				dates[i].weekDay = DateUtil.getWeek(dates[i].date);
				days[i].setText(dates[i].date.substring(8));
				weeks[i].setText(DateUtil.getWeek(dates[i].date));
			}
			dateString = year + "-" + month_ + "-" + day_;
			// int l = (int) map.get(true);
			// if (l != -1) {
			// lins[l].setBackgroundResource(R.drawable.text_border1);
			// days[l].setTextColor(getResources()
			// .getColor(R.color.text_dark2));
			// weeks[l].setTextColor(getResources().getColor(
			// R.color.text_dark2));
			// }
			for (int i = 0; i < dates.length; i++) {
				if (MyApplication.userInfo.currentDate.equals(dates[i].date)) {
					lins[i].setBackgroundResource(R.color.text_dark2);
					days[i].setTextColor(getResources().getColor(R.color.white));
					weeks[i].setTextColor(getResources()
							.getColor(R.color.white));
				} else {
					if (i == 3) {
						map.clear();
						map.put(true, 3);
						lins[i].setBackgroundResource(R.color.seagreen);
						days[i].setTextColor(getResources().getColor(
								R.color.white));
						weeks[i].setTextColor(getResources().getColor(
								R.color.white));
					} else {
						lins[i].setBackgroundResource(R.drawable.text_border1);
						days[i].setTextColor(getResources().getColor(
								R.color.text_dark2));
						weeks[i].setTextColor(getResources().getColor(
								R.color.text_dark2));
					}
				}
			}
			requestData();
		}
	}

	/**
	 * 获取系统的当前时间并赋值给TextView
	 */
	private void initDate(TextView text) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			dateString = MyApplication.userInfo.currentDate;
			date = format.parse(MyApplication.userInfo.currentDate);
			String strdate = format.format(date);
			text.setText(strdate);
			// 分别获取当前时间的年月日
			SimpleDateFormat format_year = new SimpleDateFormat("yyyy");
			SimpleDateFormat format_moth = new SimpleDateFormat("MM");
			SimpleDateFormat format_day = new SimpleDateFormat("dd");
			// SimpleDateFormat format_hour = new SimpleDateFormat("hh");
			// SimpleDateFormat format_minute = new SimpleDateFormat("mm");
			year = Integer.parseInt(format_year.format(date));
			month = Integer.parseInt(format_moth.format(date));
			day = Integer.parseInt(format_day.format(date));
			// hour = Integer.parseInt(format_hour.format(date));
			// minute = Integer.parseInt(format_minute.format(date));
			String selectDate = MyApplication.userInfo.currentDate;
			Log.v("TAG", "b:" + selectDate);
			dates[0].date = DateUtil.getSpecifiedDayBefore(selectDate, 3);
			dates[1].date = DateUtil.getSpecifiedDayBefore(selectDate, 2);
			dates[2].date = DateUtil.getSpecifiedDayBefore(selectDate, 1);
			dates[3].date = selectDate;
			dates[4].date = DateUtil.getSpecifiedDayAfter(selectDate, 1);
			dates[5].date = DateUtil.getSpecifiedDayAfter(selectDate, 2);
			dates[6].date = DateUtil.getSpecifiedDayAfter(selectDate, 3);
			for (int i = 0; i < dates.length; i++) {
				dates[i].day = dates[i].date.substring(8);
				dates[i].weekDay = DateUtil.getWeek(dates[i].date);
				days[i].setText(dates[i].date.substring(dates[i].date
						.lastIndexOf("-") + 1));
				weeks[i].setText(DateUtil.getWeek(dates[i].date));
				if (i == 3) {
					lins[i].setBackgroundResource(R.color.text_dark2);
					days[i].setTextColor(getResources().getColor(R.color.white));
					weeks[i].setTextColor(getResources()
							.getColor(R.color.white));
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * @return转换时间为服务器所需要的类型
	 */
	public String switchTime() {
		String str = year + "/" + month + "/" + day;
		return str;
	}
}
