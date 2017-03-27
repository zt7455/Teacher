package com.shengliedu.teacher.teacher.util;

import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;

public class Config1 {
	public final static String MAIN_DC = "http://dc.yunclass.com/login/login?";
	public final static String MAIN_SL = "http://sl.yunclass.com:83/login/login?";
	public final static String MAIN_BOOK_IP = "http://dc.yunclass.com";
//	public final static String MAIN_BOOK_IP = "http://sl.yunclass.com:83";

	public static com.nostra13.universalimageloader.core.ImageLoader imageLoader;

	// 平台，供后台识别
	public static String PLATFORM = "1";

	/*
	 * 角色ID
	 */
	public static int BANZHUREN = 10000;
	public static int JIAOSHI = 2;
	public static int NIANJIZUZHANG = 20000;
	public static int JIAOWU = 5;
	public static int JIAOYAN = 6;

	public static String IP;
	// public static String CHATIP = "";

	private static Config1 config1;

	public static void initUtils(Context context) {
		imageLoader =ImageLoader.getInstance();
	}

	private Config1() {
		super();
	}

	public static Config1 getInstance() {
		if (config1 == null) {
			config1 = new Config1();
		}
		return config1;
	}

	// 登录 Post
	public String LOGIN() {
		return IP + "login/login";
	}

	// 教学计划 GET
	public String TEACH_TASK() {
		return IP + "teacher/teach-task?";
	}

	// 周计划列表 GET
	public String WEEK_PLAN_LIST() {
		return IP + "teacher/teacher-date-timetable?";
	}

	// 教学日志列表 GET
	public String TEACH_LOG_LIST() {
		return IP + "teacher/teacher-teach-journal?";
	}

	// 教学日志学生表现列表 GET
	public String TEACH_LOG_STUDENT_BEHAVIOR_LIST() {
		return IP + "teacher/activity-student-behavior?";
	}

	// 教学日志评价课堂 POST
	public String TEACH_LOG_PJ_ALL() {
		return IP + "teacher/activity-behavior-update";
	}

	// 教学日志评价学生 POST
	public String TEACH_LOG_PJ_ONE() {
		return IP + "teacher/student-behavior-update";
	}

	// 通知列表 GET
	public String MESSAGE_LIST() {
		return IP + "teacher/teacher-date-notice?";
	}

	// webView加载的链接 GET
	public String MESSAGE_INFO() {
		return IP + "teacher/single-notice?id=";
	}

	// 班级日志列表 GET
	public String CLASSROOM_LOG_LIST() {
		return IP + "teacher/classroom-journal?";
	}

	// 班级日志列表 GET
	public String CORRECT_HOMEWORK_LIST() {
		return IP + "teacher/teacher-date-homework?";
	}

	// 作业完成情况 GET
	public String HOMEWORK_FINISHED_LIST() {
		return IP + "teacher/homework-finish-view?";
	}

	// 查看学生作业是否已经完成批改 GET
	public String STUDENT_FINISHED_LIST() {
		return IP + "teacher/student-homework-check-status?";
	}

	// 查看全部作业学生的回答情况 GET
	public String ALL_STUDENT_HOMEWORK_LIST() {
		return IP + "teacher/homework-view?";
	}

	// 某一学生作业的回答情况 GET
	public String ONE_STUDENT_HOMEWORK_LIST() {
		return IP + "teacher/student-homework-view?";
	}

	// 查看班级请假条 GET
	public String P_LEAVE_LIST() {
		return IP + "teacher/note-for-leave?";
	}

	// 请假条审批 POST
	public String P_LEAVE_APPROVE() {
		return IP + "teacher/note-for-leave-approve";
	}

	// 获取当前的活动和学生信息 GET
	public String HOMEWORK_SHOW_LIST() {
		return IP + "teacher/activity-homework-show-data?";
	}

	// 文件上传 POST
	public String FILEUPLOAD() {
		return IP + "file/upload";
	}

	// 添加某活动某学生的作业进行展评 POST
	public String HOMEWORK_SHOW() {
		return IP + "teacher/activity-homework-show";
	}

	// 删除上传的作业展评 GET
	public String DELETE_HOMEWORK_SHOW() {
		return IP + "teacher/remove-activity-homework-show?";
	}

	// 更新用户信息 POST
	public String UPDATEUSERINFO() {
		return IP + "teacher/personal-info-update";
	}

	// 获取用户信息 GET
	public String PERSONINFO() {
		return IP + "teacher/personal-info?";
	}

	// 对学生提交型作业进行评价 POST
	public String TJ_PJ() {
		return IP + "teacher/activity-detail-quality";
	}

	// 对本次作业学生整体完成情况进行评价 POST
	public String ZT_PJ() {
		return IP + "teacher/activity-homework-note";
	}

	// 根据年级学科班型教师获取教材teachbook的目录 GET
	public String TEACH_BOOK_OUTLINE() {
		return IP + "teacher/teacher-teach-book-outline?";
	}

	// 据教材目录id（teachOutlineId）获取课件内容 GET
	public String TEACH_BOOK_OUTLINE_DETAIL() {
		return IP + "teacher/teach-outline-courseware?";
	}
	// 教务人员对教师课件进行评价  GET  hourId（整型，课时Id）finish（整型，1完成，0未完成）
	public String TEACH_HOUR_FINISH() {
		return IP + "teacher/hour-finish?";
	}
	// 教研员对教师课件进行评价  GET teachOutlineId（整型，章节Id）researchScore（整型，0未评价 1优 2良 3合格） 
	public String TEACH_RESEARCH_SCORE() {
		return IP + "teacher/research-score?";
	}

	// 获取个人空间内的所有文件 GET
	public String MYZONE_FILE() {
		return IP + "teacher/personal-file?";
	}

	// 删除个人空间某一个文件 GET
	public String MYZONE_FILE_DELETE() {
		return IP + "teacher/remove-personal-file?";
	}

	// 个人空间文件添加 POST
	public String MYZONE_FILE_ADD() {
		return IP + "teacher/personal-file";
	}

	// 教师端扫码登陆 POST
	public String CODE_LOGIN() {
		return MAIN_BOOK_IP+"/teacher/update-user-token";
	}

	// 获取班级学生列表 GET
	public String CLASSROOM_STUDENT() {
		return IP + "teacher/classroom-student?";
	}
	// 返回要展示资源的相关信息 GET
	public String SHOWCLASSSINGLE() {
		return "teacher/resource-show?";
	}
	// 获取书列表 GET
	public String BOOK_LIST() {
		return MAIN_BOOK_IP+"/book?";
	}


}
