package com.shengliedu.teacher.teacher.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.TeachTaskActivity;
import com.shengliedu.teacher.teacher.bean.TeachTask;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

/**
 * 
 * 
 */
public class TeachTaskAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TeachTask> list;

	public TeachTaskAdapter(Context context, List<TeachTask> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater
					.inflate(R.layout.teach_task_item, null);
			viewHolder.teach_task_item_text = (TextView) convertView
					.findViewById(R.id.teach_task_item_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int count = 0;
		String keinfo = "";
		if (!MyApplication.listIsEmpty(list.get(position).hourInfo)) {

			for (int i = 0; i < list.get(position).hourInfo.size(); i++) {
				count += list.get(position).hourInfo.get(i).count;
				if (list.get(position).hourInfo.size() - 1 == i) {
					keinfo = keinfo + list.get(position).hourInfo.get(i).name
							+ list.get(position).hourInfo.get(i).count + "课时";
				} else {
					keinfo = keinfo + list.get(position).hourInfo.get(i).name
							+ list.get(position).hourInfo.get(i).count + "课时,";
				}
			}

		}
		String classroominfo = "";
		if (!MyApplication.listIsEmpty(list.get(position).classroomArr)) {
			for (int i = 0; i < list.get(position).classroomArr.size(); i++) {
				classroominfo = classroominfo
						+ list.get(position).classroomArr
										.get(i).name
						+ MyApplication.getClassroomTypeNameForId(list
								.get(position).classroomArr.get(i).type) + "\n";
			}
		}
		String teachername="";
		if (TeachTaskActivity.type==1) {
			teachername=list.get(position).realname;
		}else if (TeachTaskActivity.type==2) {
			teachername=MyApplication.userInfo.realname;
		}
		String year = null;
		String month = null;
		String edition = null;
		if (list.get(position).bookInfo.version!=null) {
			year=list.get(position).bookInfo.version.year;
			month=list.get(position).bookInfo.version.month;
			edition=list.get(position).bookInfo.version.edition;
		}
		viewHolder.teach_task_item_text.setText("教材名称:"
				+ list.get(position).bookInfo.name
				+ "\n"
				+ "出版社:"
				+ list.get(position).bookInfo.press
				+ "\n"
				+ "版次:"
				+ year
				+ "年"
				+ month
				+ "月,第"
				+ edition
				+ "版\n\n"
				+ "当前授课学期:"
				+ list.get(position).currentSemester.name
				+ "\n"
				+ "本学期共"
				+ daysBetween(list.get(position).currentSemester.startdate,
						list.get(position).currentSemester.enddate) / 7 + "周\n"
				+ "总计" + count + "课时," + keinfo + "\n\n" + "授课教师:"
				+ teachername + "          授课科目:"
				+ list.get(position).gradeInfo.name
				+ list.get(position).subjectInfo.name + "\n\n授课班级\n"
				+ classroominfo);
		return convertView;
	}

	static class ViewHolder {
		public TextView teach_task_item_text;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}
}
