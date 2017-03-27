package com.shengliedu.teacher.teacher.bean;

public class TeacherAssign {
	
	public TeacherAssign(int user_id, int subject_id, int classroom_id, int grade_id, int classroom_type) {
		// TODO Auto-generated constructor stub
		this.user_id=user_id;
		this.subject_id=subject_id;
		this.classroom_id=classroom_id;
		this.grade_id=grade_id;
		this.classroom_type=classroom_type;
	}

	
	public TeacherAssign() {
		super();
	}


	// id: 528,
	// user_id: 70,
	// subject_id: 3,
	// classroom_id: 21,
	// semester_id: 3,
	// summary: null,
	// grade_id: 3,
	// school_id: 2
	public int id;
	public int user_id;
	public int subject_id;
	public int classroom_id;
	public int semester_id;
	public int grade_id;
	public int school_id;
	public int classroom_type;
	public String realname;
	@Override
	public String toString() {
		return "TeacherAssign [user_id=" + user_id + ", subject_id="
				+ subject_id + ", classroom_id=" + classroom_id + ", grade_id="
				+ grade_id + ", classroom_type=" + classroom_type + "]";
	}
	
	
}
