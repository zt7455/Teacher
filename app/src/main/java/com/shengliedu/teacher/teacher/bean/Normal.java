package com.shengliedu.teacher.teacher.bean;

import java.io.Serializable;
import java.util.List;

public class Normal implements Serializable{
	public int id;
	public String name;
	public String data;
	public String realname;
	public int questionType;
	public int content_type;
	public String questionTypeName;
	public String teaching;
	public int submitType;
	// rCount: 0,
	// wCount: 0,
	// isFinish: 0,
	// lCount: 59
	public int rCount;
	public int wCount;
	public int isFinish;
	public int lCount;
	public int submitCount;
	public int rightCount;
	public int wrongCount;

	
	public int quality;
	public String studentAnswer;
	public String title;
	public String link;
	public String activityTitle;
	
//	 id: 6,
//     stime: 1460044800,
//     etime: 1460044800,
//     type: 1,
//     content: "我歪歪吾问无为谓吾问无为谓呜呜呜",
//     classroom: 21,
//     child: 119,
//     parent: 70,
//     addTime: 1460103246,
//     teacher: 70,
//     approve: 1,
//     approveTime: 1460103275,
//     part: 2,
//     scopeList: "[]",
//     semester: 3,
//     classroomName: "三年1班",
//     studentName: "郭佳旺"
	
	public int teacher;
	public Long stime;
	public Long etime;
	public int type;
	public int classroom;
	public String content;
	public String classroomName;
	public String subjectName;
	public Long addTime;
	public int approve;
	public Long approveTime;
	public int part;
	public String semester;
	public String studentName;
	
	
	public Normal activity;
	public List<IdName> studentArr;
	public List<ImageInfo> imageArr;
	
}
