package com.shengliedu.teacher.teacher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shengliedu.teacher.teacher.bean.TeacherAssign;
import com.shengliedu.teacher.teacher.chat.constant.Constants;

import java.util.ArrayList;
import java.util.List;

public class LoginDbHelper {
	private static LoginDbHelper instance = null;

	private SqlLiteHelper helper;
	private SQLiteDatabase db; // 我的最新登录信息
	private final int SHOW_MSG_COUNT = 15;
	private final int MORE_MSG_COUNT = 10;

	public LoginDbHelper(Context context,int DB_VERSION) {
		helper = new SqlLiteHelper(context,DB_VERSION);
		db = helper.getWritableDatabase();
	}

	public void closeDb() {
		db.close();
		helper.close();
	}

	public static LoginDbHelper getInstance(Context context,int DB_VERSION) {
		if (instance == null) {
			instance = new LoginDbHelper(context,DB_VERSION);
		}
		return instance;
	}

	private class SqlLiteHelper extends SQLiteOpenHelper {

		private static final String DB_NAME = "tlogin";

		public SqlLiteHelper(Context context ,int DB_VERSION) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * "user_id": 550372, 老师 "subject_id": 14, 学科 "classroom_id": 1087, 班级
		 * "grade_id": 8, 年纪 "classroom_type": 1 班型
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE "
					+ DB_NAME
					+ "( id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,subject_id INTEGER,"
					+ "classroom_id INTEGER , grade_id INTEGER ,classroom_type INTEGER )";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.v("TAG", "LOGIN:oldVersion="+oldVersion+",newVersion"+newVersion);
			dropTable(db);
			onCreate(db);
		}

		private void dropTable(SQLiteDatabase db) {
			String sql = "DROP TABLE IF EXISTS " + DB_NAME;
			db.execSQL(sql);
		}

	}

	public void saveAllTeacherTeacherAssign(TeacherAssign teacherAssigns) {
		ContentValues values = new ContentValues();
		values.put("user_id", teacherAssigns.user_id);
		values.put("subject_id", teacherAssigns.subject_id);
		values.put("classroom_id", teacherAssigns.classroom_id);
		values.put("grade_id", teacherAssigns.grade_id);
		values.put("classroom_type", teacherAssigns.classroom_type);
		db.insert(helper.DB_NAME, "id", values);
	}

	/**
	 * 获取TeacherTeacherAssign里面的所有GradeId
	 * 
	 */
	public List<Integer> getGradeId() {
		List<Integer> datas = new ArrayList<Integer>();
		String sql = "select grade_id from " + helper.DB_NAME + " GROUP BY grade_id ";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			datas.add(cursor.getInt(0));
		}
		cursor.close();
		return datas;
	}
	/**
	 * 获取TeacherTeacherAssign里面的所有GradeId  SubjectId 
	 * 
	 */
	public List<TeacherAssign> getGradeIdSubjectId(int grade_id) {
		List<TeacherAssign> datas = new ArrayList<TeacherAssign>();
		TeacherAssign teacherAssign;
		String sql = "select * from " + helper.DB_NAME +  " where grade_id = ? ";
		Cursor cursor = db.rawQuery(sql, new String[] { grade_id+""});
		while (cursor.moveToNext()) {
			teacherAssign=new TeacherAssign(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
			datas.add(teacherAssign);
		}
		cursor.close();
//		for (TeacherAssign a : datas) {
//			System.out.println("LoginDbHelperGrade:"+a.toString());
//		}
		return datas;
	}
	/**
	 * 获取TeacherTeacherAssign里面的所有GradeId  SubjectId ClassroomType
	 * 
	 */
	public List<TeacherAssign> getGradeIdSubjectIdClassroomType(int grade_id,int subject_id) {
		List<TeacherAssign> datas = new ArrayList<TeacherAssign>();
		TeacherAssign teacherAssign;
		String sql = "select * from " + helper.DB_NAME +  " where grade_id = ? and  subject_id = ?";
		Cursor cursor = db.rawQuery(sql, new String[] {grade_id+"",subject_id+""});
		while (cursor.moveToNext()) {
			teacherAssign=new TeacherAssign(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
			datas.add(teacherAssign);
		} 
		cursor.close();
		return datas;
	}
	/**
	 * 获取TeacherTeacherAssign里面的所有GradeId  SubjectId ClassroomType UserId
	 * 
	 */
	public List<TeacherAssign> getGradeIdSubjectIdClassroomTypeUserId(int grade_id,int subject_id,int classroom_type) {
		List<TeacherAssign> datas = new ArrayList<TeacherAssign>();
		TeacherAssign teacherAssign;
		String sql = "select * from " + helper.DB_NAME +  " where grade_id = ? and  subject_id = ? and  classroom_type = ?";
		Cursor cursor = db.rawQuery(sql, new String[] {grade_id+"",subject_id+"",classroom_type+""});
		while (cursor.moveToNext()) {
			teacherAssign=new TeacherAssign(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
			datas.add(teacherAssign);
		} 
		cursor.close();
		return datas;
	}
	/**
	 * 获取所有TeacherTeacherAssign
	 * 
	 */
	public List<TeacherAssign> getAllTeacherTeacherAssign() {
		List<TeacherAssign> datas = new ArrayList<TeacherAssign>();
		TeacherAssign teacherAssign;
		String sql = "select * from " + helper.DB_NAME +  " GROUP BY grade_id";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			teacherAssign=new TeacherAssign(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
			datas.add(teacherAssign);
		} 
		cursor.close();
		return datas;
	}
	
//	/**
//	 * 取当前会话窗口的聊天记录，限量count
//	 * 
//	 * @param friendName
//	 */
//	public List<ChatItem> getChatMsg(String chatName) {
//		List<ChatItem> chatItems = new ArrayList<ChatItem>();
//		ChatItem msg;
//		String sql = "select a.chatType,a.chatName,a.username,a.head,a.msg,a.sendDate,a.inOrOut "
//				+ " from(select * from "
//				+ helper.DB_NAME
//				+ " where chatName = ? and whos = ? order by id desc LIMIT "
//				+ SHOW_MSG_COUNT + ")a order by a.id";
//		Cursor cursor = db.rawQuery(sql, new String[] { chatName,
//				Constants.USER_NAME });
//		while (cursor.moveToNext()) {
//			msg = new ChatItem(cursor.getInt(0), cursor.getString(1),
//					cursor.getString(2), cursor.getString(3),
//					cursor.getString(4), cursor.getString(5), cursor.getInt(6));
//			chatItems.add(msg);
//			msg = null;
//		}
//		cursor.close();
//		return chatItems;
//	}
//
//	/**
//	 * 获取更多好友聊天记录,显示多5条
//	 * 
//	 * @param count
//	 * @param friendName
//	 */
//	public List<ChatItem> getChatMsgMore(int startIndex, String chatName) {
//		List<ChatItem> chatItems = new ArrayList<ChatItem>();
//		ChatItem msg;
//		String sql = "select a.chatType,a.chatName,a.username,a.head,a.msg,a.sendDate,a.inOrOut "
//				+ " from(select * from "
//				+ helper.DB_NAME
//				+ " where chatName = ? and whos = ? order by id desc LIMIT "
//				+ MORE_MSG_COUNT + " offset " + startIndex + ")a order by a.id";
//		Cursor cursor = db.rawQuery(sql, new String[] { chatName,
//				Constants.USER_NAME });
//		while (cursor.moveToNext()) {
//			msg = new ChatItem(cursor.getInt(0), cursor.getString(1),
//					cursor.getString(2), cursor.getString(3),
//					cursor.getString(4), cursor.getString(5), cursor.getInt(6));
//			chatItems.add(msg);
//			msg = null;
//		}
//		cursor.close();
//		return chatItems;
//	}
//
//	/**
//	 * 取得我的的最新消息，显示在好友表
//	 */
//	@SuppressWarnings("unused")
//	public List<ChatItem> getLastMsg() {
//		List<ChatItem> chatItems = new ArrayList<ChatItem>();
//		ChatItem msg;
//		String sql = "select chatType,chatName,username,head,msg,sendDate,inOrOut from  "
//				+ helper.DB_NAME
//				+ " where whos = ? "
//				+ " GROUP BY chatName "
//				+ "order by id desc";
//		final Cursor cursor = db.rawQuery(sql,
//				new String[] { Constants.USER_NAME });
//		while (cursor.moveToNext()) {
//			msg = new ChatItem(cursor.getInt(0), cursor.getString(1),
//					cursor.getString(2), cursor.getString(3),
//					cursor.getString(4), cursor.getString(5), cursor.getInt(6));
//			chatItems.add(msg);
//			msg = null;
//		}
//		cursor.close();
//		return chatItems;
//	}
//
//	/**
//	 * 取得我的的最新消息，模糊搜索,显示在好友表
//	 */
//	@SuppressWarnings("unused")
//	public List<ChatItem> getLastMsg(String keywords) {
//		List<ChatItem> chatItems = new ArrayList<ChatItem>();
//		ChatItem msg;
//		String sql = "select chatType,chatName,username,head,msg,sendDate,inOrOut from  "
//				+ helper.DB_NAME
//				+ " where username like ? and whos = ? "
//				+ " GROUP BY chatName " + " order by id desc";
//		final Cursor cursor = db.rawQuery(sql, new String[] {
//				"%" + keywords + "%", Constants.USER_NAME });
//		while (cursor.moveToNext()) {
//			msg = new ChatItem(cursor.getInt(0), cursor.getString(1),
//					cursor.getString(2), cursor.getString(3),
//					cursor.getString(4), cursor.getString(5), cursor.getInt(6));
//			chatItems.add(msg);
//			msg = null;
//		}
//		cursor.close();
//		return chatItems;
//	}

	public void delChatMsg(String msgId) {
		db.delete(helper.DB_NAME, "chatName=? and whos=?", new String[] {
				msgId, Constants.USER_NAME });
	}

	public void clear() {
		db.delete(helper.DB_NAME, "id>?", new String[] { "0" });
	}
}
