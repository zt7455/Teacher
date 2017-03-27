package com.shengliedu.teacher.teacher.util;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.ConsoleMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsoleMenus {
//	private static ConsoleMenu consoleMenu = new ConsoleMenu(100,
//			R.drawable.teach_task, "教学任务");
	private static ConsoleMenu consoleMenu1 = new ConsoleMenu(200,
			R.mipmap.week_plan, "周计划");
	private static ConsoleMenu consoleMenu2 = new ConsoleMenu(300,
			R.mipmap.teach_log, "教学日志");
	private static ConsoleMenu consoleMenu3 = new ConsoleMenu(400,
			R.mipmap.homework_show, "作业展评");
//	private static ConsoleMenu consoleMenu4 = new ConsoleMenu(500,
//			R.mipmap.message, "通知");
	private static ConsoleMenu consoleMenu5 = new ConsoleMenu(600,
			R.mipmap.prepare_lessons, "备课浏览");
	private static ConsoleMenu consoleMenu6 = new ConsoleMenu(700,
			R.mipmap.prepare_lessons_examine, "备课检查");
	private static ConsoleMenu consoleMenu7 = new ConsoleMenu(800,
			R.mipmap.class_log, "班级日志");
	private static ConsoleMenu consoleMenu8 = new ConsoleMenu(900,
			R.mipmap.homework_correcting, "批改作业");
	private static ConsoleMenu consoleMenu9 = new ConsoleMenu(1000,
			R.mipmap.qingjia, "批请假");
//	private static ConsoleMenu consoleMenu10 = new ConsoleMenu(1100,
//			R.drawable.my_zone, "我的空间");
	private static ConsoleMenu consoleMenu11 = new ConsoleMenu(1200,
			R.mipmap.code_login, "扫扫登录");
	private static ConsoleMenu consoleMenu12 = new ConsoleMenu(1300,
			R.mipmap.teach_resource, "教学资源");
	private static ConsoleMenu consoleMenu13 = new ConsoleMenu(1400,
			R.mipmap.classroom_control, "课堂助手");
	private static List<ConsoleMenu> list = new ArrayList<ConsoleMenu>();

	public static List<ConsoleMenu> banzhuren() {
		list.clear();
//		list.add(consoleMenu4);
		list.add(consoleMenu7);
		list.add(consoleMenu9);
//		list.add(consoleMenu10);
		return list;
	}

	public static List<ConsoleMenu> teacher() {
		list.clear();
//		list.add(consoleMenu);
//		list.add(consoleMenu1);
		list.add(consoleMenu2);
		list.add(consoleMenu3);
//		list.add(consoleMenu4);
		list.add(consoleMenu5);
		list.add(consoleMenu8);
//		list.add(consoleMenu10);
		list.add(consoleMenu11);
		list.add(consoleMenu12);
		list.add(consoleMenu13);
		return list;
	}

	public static List<ConsoleMenu> jiaowu() {
		list.clear();
//		list.add(consoleMenu);
//		list.add(consoleMenu1);
		list.add(consoleMenu2);
//		list.add(consoleMenu4);
		list.add(consoleMenu5);
		list.add(consoleMenu6);
		list.add(consoleMenu7);
		list.add(consoleMenu9);
//		list.add(consoleMenu10);
		list.add(consoleMenu12);
		return list;
	}

	public static List<ConsoleMenu> jiaoyan() {
		list.clear();
//		list.add(consoleMenu4);
		list.add(consoleMenu5);
		list.add(consoleMenu6);
//		list.add(consoleMenu10);
		list.add(consoleMenu12);
		return list;
	}

	public static List<ConsoleMenu> nianjizu() {
		list.clear();
//		list.add(consoleMenu);
//		list.add(consoleMenu1);
		list.add(consoleMenu2);
//		list.add(consoleMenu4);
		list.add(consoleMenu7);
		list.add(consoleMenu9);
//		list.add(consoleMenu10);
		return list;
	}

	public static List<ConsoleMenu> removeListDuplicateObject(List<ConsoleMenu> list1,List<ConsoleMenu> list2) {
		list1.addAll(list2);
		Set<ConsoleMenu> set=new HashSet<ConsoleMenu>(list1);
		list1.clear();
		list1.addAll(set);
		Collections.sort(list1,comparator);
		return list1;
	}
	
	 private static Comparator<ConsoleMenu> comparator = new Comparator<ConsoleMenu>() {
         public int compare(ConsoleMenu s1, ConsoleMenu s2) {
                 return s1.number - s2.number;
         }
     };
}
