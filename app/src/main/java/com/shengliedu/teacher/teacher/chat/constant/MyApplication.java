package com.shengliedu.teacher.teacher.chat.constant;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.shengliedu.teacher.teacher.LoginDbHelper;
import com.shengliedu.teacher.teacher.bean.BeanYunIp;
import com.shengliedu.teacher.teacher.bean.ClassRoom;
import com.shengliedu.teacher.teacher.bean.ClassRoomType;
import com.shengliedu.teacher.teacher.bean.EditionVersionArr;
import com.shengliedu.teacher.teacher.bean.Grade_Subject_classroomType;
import com.shengliedu.teacher.teacher.bean.IdName;
import com.shengliedu.teacher.teacher.bean.SchoolstageArr;
import com.shengliedu.teacher.teacher.bean.Subject_classroomType;
import com.shengliedu.teacher.teacher.bean.Teacher;
import com.shengliedu.teacher.teacher.bean.TeacherAssign;
import com.shengliedu.teacher.teacher.bean.UserInfo;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MyApplication extends Application implements
		UncaughtExceptionHandler {
	private static MyApplication instance;
	public static SharedPreferences sharedPreferences;
	public static double lat = 23.117055306224895;
	public static double lon = 113.2759952545166;
	public static boolean isRun;
	public static boolean chatLogin;

	public static MyApplication getInstance() {
		return instance;
	}

	public static UserInfo userInfo = new UserInfo();
	public static BeanYunIp beanSchoolInfo;
	public static List<IdName> gradeDic;// 年级字典
	public static List<IdName> subjectDic;// 学科字典
	public static List<ClassRoom> classroomDic;// 班级字典
	public static List<Teacher> teacherDic;// 老师字典
	public static List<IdName> classroomTypeDic;// 班型字典
	public static List<SchoolstageArr> schoolstageArr;
	public static List<EditionVersionArr> editionVersionArr;
	public static List<TeacherAssign> teacherAssigns;// 所以老师教学分工

	public static boolean listIsEmpty(List<?> list) {
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 全局未知异常捕获
		// Thread.setDefaultUncaughtExceptionHandler(this);
		Config1.initUtils(this);
		sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES,
				Context.MODE_PRIVATE);
		int time = SharedPreferenceTool.getAppRunTimes(getInstance());
		SharedPreferenceTool.setAppRunTimes(getInstance(), time + 1);
		ImgConfig.initImageLoader();
//		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//			@Override
//			public void onViewInitFinished(boolean arg0) {
//				// TODO Auto-generated method stub
//				Log.e("app", " onViewInitFinished is " + arg0);
//			}
//
//			@Override
//			public void onCoreInitFinished() {
//				// TODO Auto-generated method stub
//
//			}
//		};
//		QbSdk.initX5Environment(getApplicationContext(), cb);
		new Timer().schedule(new TimerTask() { // 1秒后开始，5分钟上传一次自己的位置
					@Override
					public void run() {
						if (MyApplication.sharedPreferences.getBoolean(
								"isShare", true)) {
							uploadAdr();
						}
					}
				}, 1000, Constants.UPDATE_TIME);
	}

	public void uploadAdr() {
		if (Constants.loginUser != null
				&& (lat != 23.117055306224895 || lon != 113.2759952545166)) { //
			Constants.loginUser.vCard.setField("latAndlon", lat + "," + lon);
			XmppConnection.getInstance().changeVcard(Constants.loginUser.vCard);
		}
	}

	public void clearAdr() {
		if (Constants.loginUser != null) {
			Constants.loginUser.vCard.setField("latAndlon",
					4.9E-324 + "," + 4.9E-324);
			XmppConnection.getInstance().changeVcard(Constants.loginUser.vCard);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	// 处理登录返回数据
	public static void dealLoginInfo() {
		gradeDic = userInfo.gradeDic;
		subjectDic = userInfo.subjectDic;
		classroomDic = userInfo.classroomDic;
		teacherDic = userInfo.teacherDic;
		classroomTypeDic = userInfo.classroomTypeDic;
		teacherAssigns = userInfo.teacherAssigns;
		schoolstageArr = userInfo.schoolstageArr;
		editionVersionArr = userInfo.editionVersionArr;
		if (!listIsEmpty(teacherAssigns)) {
			for (int i = 0; i < teacherAssigns.size(); i++) {
				LoginDbHelper.getInstance(getInstance(),
						SharedPreferenceTool.getAppRunTimes(getInstance()))
						.saveAllTeacherTeacherAssign(teacherAssigns.get(i));
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		Log.v("TAG", "Start=" + str);
		getTree();
		Date curDate1 = new Date(System.currentTimeMillis());// 获取当前时间
		String str1 = formatter.format(curDate1);
		Log.v("TAG", "TreeEnd=" + str1);
		getTree1();
		Date curDate2 = new Date(System.currentTimeMillis());// 获取当前时间
		String str2 = formatter.format(curDate2);
		Log.v("TAG", "Tree1End=" + str2);
		getTree2();
		Date curDate3 = new Date(System.currentTimeMillis());// 获取当前时间
		String str3 = formatter.format(curDate3);
		Log.v("TAG", "Tree2End=" + str3);
		getTree3();
		Date curDate4 = new Date(System.currentTimeMillis());// 获取当前时间
		String str4 = formatter.format(curDate4);
		Log.v("TAG", "Tree3End=" + str4);
//		getTree4();
		Date curDate5 = new Date(System.currentTimeMillis());// 获取当前时间
		String str5 = formatter.format(curDate5);
		Log.v("TAG", "Tree4End=" + str5);
		getTree5();
		Date curDate6 = new Date(System.currentTimeMillis());// 获取当前时间
		String str6 = formatter.format(curDate6);
		Log.v("TAG", "Tree5End=" + str6);
		getTree6();
		Date curDate7 = new Date(System.currentTimeMillis());// 获取当前时间
		String str7 = formatter.format(curDate7);
		Log.v("TAG", "Tree6 AllEnd=" + str7);
	}

	/*
	 * 根据schoolstage在字典中查询学段名称
	 */
	public static String getSchoolstageNameForId(int id) {
		if (!listIsEmpty(schoolstageArr)) {
			for (int i = 0; i < schoolstageArr.size(); i++) {
				if (schoolstageArr.get(i).id == id) {
					return schoolstageArr.get(i).name;
				}
			}
		}
		return "";
	}

	/*
	 * 根据classroomid在字典中classroom名称
	 */
	public static String getClassRoomNameForclassroomid(int classroomid) {
		if (!listIsEmpty(classroomDic)) {
			for (int i = 0; i < classroomDic.size(); i++) {
				if (classroomDic.get(i).id == classroomid) {
					String ji = getGradeNameForId(classroomDic.get(i).grade);
					String classroomname = classroomDic.get(i).name;
					if (classroomname.contains("级")
							&& classroomname.contains("班")) {
						return classroomname;
					} else if (!classroomname.contains("级")
							&& classroomname.contains("班")) {
						return ji + classroomname;
					} else if (!classroomname.contains("级")
							&& !classroomname.contains("班")) {
						return ji + classroomname + "班";
					}
					return classroomDic.get(i).name;
				}
			}
		}
		return "";
	}

	/*
	 * 根据editionVersion在字典中查询书版本名称
	 */
	public static String getEditionVersionNameForId(int id) {
		if (!listIsEmpty(editionVersionArr)) {
			for (int i = 0; i < editionVersionArr.size(); i++) {
				if (editionVersionArr.get(i).id == id) {
					return editionVersionArr.get(i).name;
				}
			}
		}
		return "";
	}

	/*
	 * 根据年级id在字典中查询年级名称
	 */
	public static String getGradeNameForId(int id) {
		if (!listIsEmpty(gradeDic)) {
			for (int i = 0; i < gradeDic.size(); i++) {
				if (gradeDic.get(i).id == id) {
					return gradeDic.get(i).name;
				}
			}
		}
		return "null";
	}

	/*
	 * 根据学科id在字典中查询学科名称
	 */
	public static String getSubjectNameForId(int id) {
		if (!listIsEmpty(subjectDic)) {
			for (int i = 0; i < subjectDic.size(); i++) {
				if (subjectDic.get(i).id == id) {
					return subjectDic.get(i).name;
				}
			}
		}
		return "";
	}

	/*
	 * 根据班级id在字典中查询班级名称
	 */
	public static String getClassroomNameForId(int id) {
		if (!listIsEmpty(classroomDic)) {
			for (int i = 0; i < classroomDic.size(); i++) {
				if (classroomDic.get(i).id == id) {
					return classroomDic.get(i).name;
				}
			}
		}
		return "";
	}

	/*
	 * 根据老师id在字典中查询老师名称
	 */
	public static String getTeacherNameForId(int id) {
		if (!listIsEmpty(teacherDic)) {
			for (int i = 0; i < teacherDic.size(); i++) {
				if (teacherDic.get(i).id == id) {
					return teacherDic.get(i).realname;
				}
			}
		}
		return "";
	}

	/*
	 * 根据班型id在字典中查询班型名称
	 */
	public static String getClassroomTypeNameForId(int id) {
		if (!listIsEmpty(classroomTypeDic)) {
			for (int i = 0; i < classroomTypeDic.size(); i++) {
				if (classroomTypeDic.get(i).id == id) {
					return classroomTypeDic.get(i).name;
				}
			}
		}
		return "";
	}

	public static List<Grade_Subject_classroomType> grade_Subject_classroomTypes = new ArrayList<Grade_Subject_classroomType>();

	/*
	 * 处理四级结构数据 年级-学科-班型-教师
	 */
	public static void getTree() {
		if (MyApplication.userInfo != null) {
			List<Integer> teacherAssign_1 = LoginDbHelper.getInstance(
					getInstance(),
					SharedPreferenceTool.getAppRunTimes(getInstance()))
					.getGradeId();
			// List<TeacherAssign> teacherAssign0 =
			// MyApplication.userInfo.teacherAssigns;
			// List<TeacherAssign> teacherAssign =
			// MyApplication.userInfo.teacherAssigns;
			// List<TeacherAssign> teacherAssign2 =
			// MyApplication.userInfo.teacherAssigns;
			if (!MyApplication.listIsEmpty(teacherAssign_1)) {
				for (int i = 0; i < teacherAssign_1.size(); i++) {
					Grade_Subject_classroomType grade_Subject_classroomType = new Grade_Subject_classroomType();
					grade_Subject_classroomType.id = teacherAssign_1.get(i);
					grade_Subject_classroomType.name = MyApplication
							.getGradeNameForId(teacherAssign_1.get(i));
					List<Subject_classroomType> subject_classroomTypes = new ArrayList<Subject_classroomType>();
					List<TeacherAssign> teacherAssign0 = LoginDbHelper
							.getInstance(
									getInstance(),
									SharedPreferenceTool
											.getAppRunTimes(getInstance()))
							.getGradeIdSubjectId(teacherAssign_1.get(i));
					if (!MyApplication.listIsEmpty(teacherAssign0)) {
						for (int j = 0; j < teacherAssign0.size(); j++) {
							if (grade_Subject_classroomType.id == teacherAssign0
									.get(j).grade_id) {
								Subject_classroomType subject_classroomType = new Subject_classroomType();
								subject_classroomType.id = teacherAssign0
										.get(j).subject_id;
								subject_classroomType.name = MyApplication
										.getSubjectNameForId(teacherAssign0
												.get(j).subject_id);

								List<ClassRoomType> classroomTypes = new ArrayList<ClassRoomType>();
								List<TeacherAssign> teacherAssign = LoginDbHelper
										.getInstance(
												getInstance(),
												SharedPreferenceTool
														.getAppRunTimes(getInstance()))
										.getGradeIdSubjectIdClassroomType(
												teacherAssign_1.get(i),
												teacherAssign0.get(j).subject_id);
								if (!MyApplication.listIsEmpty(teacherAssign)) {
									for (int k = 0; k < teacherAssign.size(); k++) {
										if (grade_Subject_classroomType.id == teacherAssign
												.get(k).grade_id
												&& subject_classroomType.id == teacherAssign
														.get(k).subject_id) {
											ClassRoomType classRoomType = new ClassRoomType();
											classRoomType.id = teacherAssign
													.get(k).classroom_type;
											classRoomType.name = MyApplication
													.getClassroomTypeNameForId(teacherAssign
															.get(k).classroom_type);

											List<IdName> teachers = new ArrayList<IdName>();
											List<TeacherAssign> teacherAssign2 = LoginDbHelper
													.getInstance(
															getInstance(),
															SharedPreferenceTool
																	.getAppRunTimes(getInstance()))
													.getGradeIdSubjectIdClassroomTypeUserId(
															teacherAssign_1
																	.get(i),
															teacherAssign0
																	.get(j).subject_id,
															teacherAssign
																	.get(k).classroom_type);
											if (!MyApplication
													.listIsEmpty(teacherAssign2)) {
												for (int l = 0; l < teacherAssign2
														.size(); l++) {
													if (grade_Subject_classroomType.id == teacherAssign2
															.get(l).grade_id
															&& subject_classroomType.id == teacherAssign2
																	.get(l).subject_id
															&& classRoomType.id == teacherAssign2
																	.get(l).classroom_type) {
														IdName teacher = new IdName();
														teacher.id = teacherAssign2
																.get(l).user_id;
														teacher.name = MyApplication
																.getTeacherNameForId(teacherAssign2
																		.get(l).user_id);
														teachers.add(teacher);
													}
												}
											}
											Set<IdName> set = new HashSet<IdName>(
													teachers);
											teachers.clear();
											teachers.addAll(set);
											Collections.sort(teachers,
													comparator3);
											classRoomType.teachers = teachers;
											classroomTypes.add(classRoomType);
										}
									}
								}
								Set<ClassRoomType> set = new HashSet<ClassRoomType>(
										classroomTypes);
								classroomTypes.clear();
								classroomTypes.addAll(set);
								Collections.sort(classroomTypes, comparator2);
								subject_classroomType.classroomType = classroomTypes;
								subject_classroomTypes
										.add(subject_classroomType);
							}
							Set<Subject_classroomType> set = new HashSet<Subject_classroomType>(
									subject_classroomTypes);
							subject_classroomTypes.clear();
							subject_classroomTypes.addAll(set);
							Collections.sort(subject_classroomTypes,
									comparator1);
							grade_Subject_classroomType.subject_classroomtype = subject_classroomTypes;
						}
					}
					grade_Subject_classroomTypes
							.add(grade_Subject_classroomType);
				}
			}
			Set<Grade_Subject_classroomType> set = new HashSet<Grade_Subject_classroomType>(
					grade_Subject_classroomTypes);
			grade_Subject_classroomTypes.clear();
			grade_Subject_classroomTypes.addAll(set);
			Collections.sort(grade_Subject_classroomTypes, comparator);
			for (Grade_Subject_classroomType a : grade_Subject_classroomTypes) {
				System.out.println("四级结构数据 年级-学科-班型-教师:" + a.toString());
			}
		}
	}

	public static List<Grade_Subject_classroomType> user_grade_Subject_classroomTypes = new ArrayList<Grade_Subject_classroomType>();

	/*
	 * 获取用户自己的四级结构数据 年级-学科-班型-自己
	 */
	public static void getTree2() {
		user_grade_Subject_classroomTypes.clear();
		if (MyApplication.userInfo != null) {
			List<TeacherAssign> teacherAssign_1 = MyApplication.userInfo.teacherAssigns;
			// List<TeacherAssign> teacherAssign0 =
			// MyApplication.userInfo.teacherAssigns;
			// List<TeacherAssign> teacherAssign =
			// MyApplication.userInfo.teacherAssigns;
			// List<TeacherAssign> teacherAssign2 =
			// MyApplication.userInfo.teacherAssigns;
			if (!MyApplication.listIsEmpty(teacherAssign_1)) {
				for (int i = 0; i < teacherAssign_1.size(); i++) {
					if (teacherAssign_1.get(i).user_id == userInfo.id) {
						Grade_Subject_classroomType grade_Subject_classroomType = new Grade_Subject_classroomType();
						grade_Subject_classroomType.id = teacherAssign_1.get(i).grade_id;
						grade_Subject_classroomType.name = MyApplication
								.getGradeNameForId(teacherAssign_1.get(i).grade_id);
						List<Subject_classroomType> subject_classroomTypes = new ArrayList<Subject_classroomType>();
						List<TeacherAssign> teacherAssign0 = LoginDbHelper
								.getInstance(
										getInstance(),
										SharedPreferenceTool
												.getAppRunTimes(getInstance()))
								.getGradeIdSubjectId(
										teacherAssign_1.get(i).grade_id);
						if (!MyApplication.listIsEmpty(teacherAssign0)) {
							for (int j = 0; j < teacherAssign0.size(); j++) {
								if (grade_Subject_classroomType.id == teacherAssign0
										.get(j).grade_id
										&& teacherAssign0.get(j).user_id == userInfo.id) {
									Subject_classroomType subject_classroomType = new Subject_classroomType();
									subject_classroomType.id = teacherAssign0
											.get(j).subject_id;
									subject_classroomType.name = MyApplication
											.getSubjectNameForId(teacherAssign0
													.get(j).subject_id);

									List<ClassRoomType> classroomTypes = new ArrayList<ClassRoomType>();
									List<TeacherAssign> teacherAssign = LoginDbHelper
											.getInstance(
													getInstance(),
													SharedPreferenceTool
															.getAppRunTimes(getInstance()))
											.getGradeIdSubjectIdClassroomType(
													teacherAssign_1.get(i).grade_id,
													teacherAssign0.get(j).subject_id);
									if (!MyApplication
											.listIsEmpty(teacherAssign)) {
										for (int k = 0; k < teacherAssign
												.size(); k++) {
											if (grade_Subject_classroomType.id == teacherAssign
													.get(k).grade_id
													&& subject_classroomType.id == teacherAssign
															.get(k).subject_id
													&& teacherAssign.get(k).user_id == userInfo.id) {
												ClassRoomType classRoomType = new ClassRoomType();
												classRoomType.id = teacherAssign
														.get(k).classroom_type;
												classRoomType.name = MyApplication
														.getClassroomTypeNameForId(teacherAssign
																.get(k).classroom_type);

												List<IdName> teachers = new ArrayList<IdName>();
												List<TeacherAssign> teacherAssign2 = LoginDbHelper
														.getInstance(
																getInstance(),
																SharedPreferenceTool
																		.getAppRunTimes(getInstance()))
														.getGradeIdSubjectIdClassroomTypeUserId(
																teacherAssign_1
																		.get(i).grade_id,
																teacherAssign0
																		.get(j).subject_id,
																teacherAssign
																		.get(k).classroom_type);
												if (!MyApplication
														.listIsEmpty(teacherAssign2)) {
													for (int l = 0; l < teacherAssign2
															.size(); l++) {
														if (grade_Subject_classroomType.id == teacherAssign2
																.get(l).grade_id
																&& subject_classroomType.id == teacherAssign2
																		.get(l).subject_id
																&& classRoomType.id == teacherAssign2
																		.get(l).classroom_type
																&& teacherAssign2
																		.get(l).user_id == userInfo.id) {
															IdName teacher = new IdName();
															teacher.id = teacherAssign2
																	.get(l).user_id;
															teacher.name = MyApplication
																	.getTeacherNameForId(teacherAssign2
																			.get(l).user_id);
															teachers.add(teacher);
														}
													}
												}
												Set<IdName> set = new HashSet<IdName>(
														teachers);
												teachers.clear();
												teachers.addAll(set);
												Collections.sort(teachers,
														comparator3);
												classRoomType.teachers = teachers;
												classroomTypes
														.add(classRoomType);
											}
										}
									}
									Set<ClassRoomType> set = new HashSet<ClassRoomType>(
											classroomTypes);
									classroomTypes.clear();
									classroomTypes.addAll(set);
									Collections.sort(classroomTypes,
											comparator2);
									subject_classroomType.classroomType = classroomTypes;
									subject_classroomTypes
											.add(subject_classroomType);
								}
								Set<Subject_classroomType> set = new HashSet<Subject_classroomType>(
										subject_classroomTypes);
								subject_classroomTypes.clear();
								subject_classroomTypes.addAll(set);
								Collections.sort(subject_classroomTypes,
										comparator1);
								grade_Subject_classroomType.subject_classroomtype = subject_classroomTypes;
							}
						}
						user_grade_Subject_classroomTypes
								.add(grade_Subject_classroomType);
					}
				}
			}
			Set<Grade_Subject_classroomType> set = new HashSet<Grade_Subject_classroomType>(
					user_grade_Subject_classroomTypes);
			user_grade_Subject_classroomTypes.clear();
			user_grade_Subject_classroomTypes.addAll(set);
			Collections.sort(user_grade_Subject_classroomTypes, comparator);
			for (Grade_Subject_classroomType a : user_grade_Subject_classroomTypes) {
				System.out.println("获取用户自己的四级结构数据 年级-学科-班型-自己:" + a.toString());
			}
		}
	}

	public static List<Grade_Subject_classroomType> jy_grade_Subject_classroomTypes = new ArrayList<Grade_Subject_classroomType>();

	/*
	 * 获取用户教研的四级结构数据 年级-学科-班型-老师
	 */
	public static void getTree3() {
		if (MyApplication.userInfo != null) {
			List<TeacherAssign> teacherAssign_1 = MyApplication.userInfo.gradeSubjectResearchers;
			if (!MyApplication.listIsEmpty(teacherAssign_1)) {
				for (int i = 0; i < teacherAssign_1.size(); i++) {
					Grade_Subject_classroomType grade_Subject_classroomType = new Grade_Subject_classroomType();
					grade_Subject_classroomType.id = teacherAssign_1.get(i).grade_id;
					grade_Subject_classroomType.name = MyApplication
							.getGradeNameForId(teacherAssign_1.get(i).grade_id);
					List<Subject_classroomType> subject_classroomTypes = new ArrayList<Subject_classroomType>();
					List<TeacherAssign> teacherAssign0 = get2(teacherAssign_1
							.get(i).grade_id);
					if (!MyApplication.listIsEmpty(teacherAssign0)) {
						for (int j = 0; j < teacherAssign0.size(); j++) {
							if (grade_Subject_classroomType.id == teacherAssign0
									.get(j).grade_id
									&& teacherAssign0.get(j).subject_id == teacherAssign_1
											.get(i).subject_id) {
								Subject_classroomType subject_classroomType = new Subject_classroomType();
								subject_classroomType.id = teacherAssign0
										.get(j).subject_id;
								subject_classroomType.name = MyApplication
										.getSubjectNameForId(teacherAssign0
												.get(j).subject_id);
								List<ClassRoomType> classroomTypes = new ArrayList<ClassRoomType>();
								List<TeacherAssign> teacherAssign = get3(
										teacherAssign_1.get(i).grade_id,
										teacherAssign0.get(j).subject_id);
								if (!MyApplication.listIsEmpty(teacherAssign)) {
									for (int k = 0; k < teacherAssign.size(); k++) {
										if (grade_Subject_classroomType.id == teacherAssign
												.get(k).grade_id
												&& subject_classroomType.id == teacherAssign
														.get(k).subject_id) {
											ClassRoomType classRoomType = new ClassRoomType();
											classRoomType.id = teacherAssign
													.get(k).classroom_type;
											classRoomType.name = MyApplication
													.getClassroomTypeNameForId(teacherAssign
															.get(k).classroom_type);

											List<IdName> teachers = new ArrayList<IdName>();
											List<TeacherAssign> teacherAssign2 = get4(
													teacherAssign_1.get(i).grade_id,
													teacherAssign0.get(j).subject_id,
													teacherAssign.get(k).classroom_type);
											if (!MyApplication
													.listIsEmpty(teacherAssign2)) {
												for (int l = 0; l < teacherAssign2
														.size(); l++) {
													if (grade_Subject_classroomType.id == teacherAssign2
															.get(k).grade_id
															&& subject_classroomType.id == teacherAssign2
																	.get(k).subject_id
															&& classRoomType.id == teacherAssign2
																	.get(k).classroom_type) {
														IdName teacher = new IdName();
														teacher.id = teacherAssign2
																.get(l).user_id;
														teacher.name = MyApplication
																.getTeacherNameForId(teacherAssign2
																		.get(l).user_id);
														teachers.add(teacher);
													}
												}
											}
											Set<IdName> set = new HashSet<IdName>(
													teachers);
											teachers.clear();
											teachers.addAll(set);
											Collections.sort(teachers,
													comparator3);
											classRoomType.teachers = teachers;
											classroomTypes.add(classRoomType);
										}
									}
								}
								Set<ClassRoomType> set = new HashSet<ClassRoomType>(
										classroomTypes);
								classroomTypes.clear();
								classroomTypes.addAll(set);
								Collections.sort(classroomTypes, comparator2);
								subject_classroomType.classroomType = classroomTypes;
								subject_classroomTypes
										.add(subject_classroomType);
							}
							Set<Subject_classroomType> set = new HashSet<Subject_classroomType>(
									subject_classroomTypes);
							subject_classroomTypes.clear();
							subject_classroomTypes.addAll(set);
							Collections.sort(subject_classroomTypes,
									comparator1);
							grade_Subject_classroomType.subject_classroomtype = subject_classroomTypes;
						}
					}
					jy_grade_Subject_classroomTypes
							.add(grade_Subject_classroomType);
				}
			}
			Set<Grade_Subject_classroomType> set = new HashSet<Grade_Subject_classroomType>(
					jy_grade_Subject_classroomTypes);
			jy_grade_Subject_classroomTypes.clear();
			jy_grade_Subject_classroomTypes.addAll(set);
			Collections.sort(jy_grade_Subject_classroomTypes, comparator);
			for (Grade_Subject_classroomType a : jy_grade_Subject_classroomTypes) {
				System.out.println("获取用户教研的四级结构数据 年级-学科-班型-老师:" + a.toString());
			}
		}
	}

//	public static List<ClassRoom> grade_Subject_editionVersion = new ArrayList<ClassRoom>();

	/*
	 * 获取两级结构数据 年级-学科（出版社）
	 */
//	public static void getTree4() {
//		if (MyApplication.userInfo != null) {
//			List<ClassRoom> ClassRoom1 = MyApplication.userInfo.gradeSubjectArr;
//			List<ClassRoom> ClassRoom2 = MyApplication.userInfo.gradeSubjectArr;
//			if (!MyApplication.listIsEmpty(ClassRoom1)) {
//				for (int i = 0; i < ClassRoom1.size(); i++) {
//					if (ClassRoom1.get(i).grade != 0) {
//						ClassRoom ClassRoom_one = new ClassRoom();
//						ClassRoom_one.id = ClassRoom1.get(i).grade;
//						List<ClassRoom> ClassRoom_one_evs = new ArrayList<ClassRoom>();
//						for (int j = 0; j < ClassRoom2.size(); j++) {
//							if (ClassRoom2.get(j).subject != 0
//									&& ClassRoom_one.id == ClassRoom2.get(j).grade) {
//								ClassRoom ClassRoom_two = new ClassRoom();
//								ClassRoom_two.id = ClassRoom2.get(j).subject;
//								ClassRoom_two.editionVersion = ClassRoom2
//										.get(j).editionVersion;
//								ClassRoom_one_evs.add(ClassRoom_two);
//							}
//						}
//						Set<ClassRoom> set = new HashSet<ClassRoom>(
//								ClassRoom_one_evs);
//						ClassRoom_one_evs.clear();
//						ClassRoom_one_evs.addAll(set);
//						Collections.sort(ClassRoom_one_evs, comparator4);
//						ClassRoom_one.subs = ClassRoom_one_evs;
//						grade_Subject_editionVersion.add(ClassRoom_one);
//					}
//				}
//				Set<ClassRoom> set = new HashSet<ClassRoom>(
//						grade_Subject_editionVersion);
//				grade_Subject_editionVersion.clear();
//				grade_Subject_editionVersion.addAll(set);
//				Collections.sort(grade_Subject_editionVersion, comparator4);
//			}
//		}
//	}

	private static Comparator<Grade_Subject_classroomType> comparator = new Comparator<Grade_Subject_classroomType>() {
		public int compare(Grade_Subject_classroomType s1,
				Grade_Subject_classroomType s2) {
			return s1.id - s2.id;
		}
	};
	private static Comparator<Subject_classroomType> comparator1 = new Comparator<Subject_classroomType>() {
		public int compare(Subject_classroomType s1, Subject_classroomType s2) {
			return s1.id - s2.id;
		}
	};
	private static Comparator<ClassRoomType> comparator2 = new Comparator<ClassRoomType>() {
		public int compare(ClassRoomType s1, ClassRoomType s2) {
			return s1.id - s2.id;
		}
	};
	private static Comparator<IdName> comparator3 = new Comparator<IdName>() {
		public int compare(IdName s1, IdName s2) {
			return s1.id - s2.id;
		}
	};
	private static Comparator<ClassRoom> comparator4 = new Comparator<ClassRoom>() {
		public int compare(ClassRoom s1, ClassRoom s2) {
			return s1.id - s2.id;
		}
	};
	public static List<ClassRoom> classRooms = new ArrayList<ClassRoom>();

	/*
	 * 处理两级结构数据 年级-班级
	 */
	public static void getTree1() {
		if (MyApplication.userInfo != null) {
			List<IdName> gradeDic = MyApplication.userInfo.gradeDic;
			List<ClassRoom> ClassRooms1 = MyApplication.userInfo.classroomDic;
			if (!MyApplication.listIsEmpty(gradeDic)) {
				for (int i = 0; i < gradeDic.size(); i++) {
					if (gradeDic.get(i).id != 0) {
						ClassRoom grade = new ClassRoom();
						grade.id = gradeDic.get(i).id;
						grade.name = gradeDic.get(i).name;
						List<ClassRoom> classrooms = new ArrayList<ClassRoom>();
						for (int j = 0; j < ClassRooms1.size(); j++) {
							if (grade.id == ClassRooms1.get(j).grade) {
								ClassRoom classroom = new ClassRoom();
								classroom.id = ClassRooms1.get(j).id;
								classroom.name = ClassRooms1.get(j).name;
								classrooms.add(classroom);
							}
						}
						Set<ClassRoom> set = new HashSet<ClassRoom>(classrooms);
						classrooms.clear();
						classrooms.addAll(set);
						grade.classrooms = classrooms;
						classRooms.add(grade);
					}
				}
				Set<ClassRoom> set = new HashSet<ClassRoom>(classRooms);
				classRooms.clear();
				classRooms.addAll(set);
			}
			for (ClassRoom c : classRooms) {
				System.out.println(c.toString());
			}
		}
	}

	public static List<IdName> names = new ArrayList<IdName>();

	/*
	 * 处理两级结构数据 教师-学科
	 */
	public static void getTree5() {
		if (MyApplication.userInfo != null) {
			List<TeacherAssign> teacher = MyApplication.userInfo.teacherAssigns;
			if (!MyApplication.listIsEmpty(teacher)) {
				for (int i = 0; i < teacher.size(); i++) {
					if (teacher.get(i).user_id == userInfo.id) {
						IdName idName = new IdName();
						idName.id = teacher.get(i).subject_id;
						names.add(idName);
					}
				}
				Set<IdName> set = new HashSet<IdName>(names);
				names.clear();
				names.addAll(set);
			}
			for (IdName c : names) {
				System.out.println(" 教师-学科" + c.toString());
			}
		}
	}

	public static List<IdName> names2 = new ArrayList<IdName>();

	/*
	 * 处理两级结构数据 教研-学科
	 */
	public static void getTree6() {
		if (MyApplication.userInfo != null) {
			List<TeacherAssign> teacher = MyApplication.userInfo.gradeSubjectResearchers;
			if (!MyApplication.listIsEmpty(teacher)) {
				for (int i = 0; i < teacher.size(); i++) {
					if (teacher.get(i).user_id == userInfo.id) {
						IdName idName = new IdName();
						idName.id = teacher.get(i).subject_id;
						names2.add(idName);
					}
				}
				Set<IdName> set = new HashSet<IdName>(names2);
				names2.clear();
				names2.addAll(set);
			}
			for (IdName c : names2) {
				System.out.println("教研-学科" + c.toString());
			}
		}
	}

	private static List<TeacherAssign> get2(int gradeid) {
		// TODO Auto-generated method stub
		List<TeacherAssign> teacherresult = new ArrayList<TeacherAssign>();
		List<TeacherAssign> teacher = userInfo.teacherAssigns;
		if (!MyApplication.listIsEmpty(teacher)) {
			for (int i = 0; i < teacher.size(); i++) {
				if (teacher.get(i).grade_id == gradeid) {
					teacherresult.add(teacher.get(i));
				}
			}
		}
		return teacherresult;
	}

	private static List<TeacherAssign> get3(int gradeid, int subject_id) {
		// TODO Auto-generated method stub
		List<TeacherAssign> teacherresult = new ArrayList<TeacherAssign>();
		List<TeacherAssign> teacher = userInfo.teacherAssigns;
		if (!MyApplication.listIsEmpty(teacher)) {
			for (int i = 0; i < teacher.size(); i++) {
				if (teacher.get(i).grade_id == gradeid
						&& teacher.get(i).subject_id == subject_id) {
					teacherresult.add(teacher.get(i));
				}
			}
		}
		return teacherresult;
	}

	private static List<TeacherAssign> get4(int gradeid, int subject_id,
			int classroom_type) {
		// TODO Auto-generated method stub
		List<TeacherAssign> teacherresult = new ArrayList<TeacherAssign>();
		List<TeacherAssign> teacher = userInfo.teacherAssigns;
		if (!MyApplication.listIsEmpty(teacher)) {
			for (int i = 0; i < teacher.size(); i++) {
				if (teacher.get(i).grade_id == gradeid
						&& teacher.get(i).subject_id == subject_id
						&& teacher.get(i).classroom_type == classroom_type) {
					teacherresult.add(teacher.get(i));
				}
			}
		}
		return teacherresult;
	}
}
