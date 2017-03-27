package com.shengliedu.teacher.teacher.bean;

import java.util.List;

public class UserInfo {
//	   "id": 70,
//	    "name": "sl70",
//	    "realname": "白小香",
//	    "idcard": "130203198001120342",
//	    "email": "",
//	    "birth": "1980-01-01",
//	    "sex": 1,
//	    "nation": "汉",
//	    "hometown": "美国加州",
//	    "type": 1,
//	    "image": "data/user/images/default.png",
//	    "votenumber": 0,
//	    "addjson": null,
//	    "nfcId": "0e190e4c",
//	    "tel": null,
//	    "inSchoolDate": "2014-09-01",
//	    "signature": "他啦啊了了",
//	    "nickname": "秘籍好好好了",
//	    "opassword": "123456",
//	    "uid": 0,
//	    "school": 2,
//	    "localid": 70,
//	    "token": null,
//	    "asset": 100,
	public int id;
	public String name;
	public String realname;
	public String image;
	public String idcard;
	public String email;
	public String birth;
	public int sex;
	public int type;
	public String nation;
	public String hometown;
	public String nfcId;
	public String signature;
	public String nickname;
	public String tel;
	public String currentDate;
	public List<IdName> roleArr;
	public List<IdName> gradeDic;
	public List<IdName> subjectDic;
	public IdName currentSemester;
	public List<ClassRoom> classroomDic;
	public List<Teacher> teacherDic;
	public List<IdName> classroomTypeDic;
	public List<TeacherAssign> gradeSubjectResearchers;
	public List<TeacherAssign> teacherAssigns;
	public List<IdName> leadGrades;
	public List<ClassRoom> leadClassrooms;
	public List<ClassRoom> gradeSubjectArr;
	public List<SchoolstageArr> schoolstageArr;
	public List<EditionVersionArr> editionVersionArr;
	public SchoolInfo schoolinfo;
}
