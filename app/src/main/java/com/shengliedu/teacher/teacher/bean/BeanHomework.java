package com.shengliedu.teacher.teacher.bean;

import java.util.List;

public class BeanHomework {
//	id: 3000009,
//    name: "<p>这是一道连线题的题干</p> ",
//    isMultiple: false,
//    questionType: 5,
//    contentType: 2,
//    lineCount: 2,
//    alignType: 1
//	submitType: 2
	
	public BeanHomework teaching;
	public int id;
	public int order;
	public int questionType;
	public int contentType;
	public int lineCount;
	public int alignType;
	public int submitType;
	public int content_type;
	public int content_id;
	public int subType;
	public int from;
	public int school;
	public String  name;
	public String  title;
	public String  realname;
	public String  explanation;
	public String  content_host;
	public String  file;
	public String  link;
	public String  questionContentType;
	public int  bookId;
	
	public BeanHomework questionInfo;
	
	public BeanQuestion question;
	public List<Normal> studentArr;
	public List<BeanCorrect> correct;
	public SynAnswer answer;
	
//	 id: 1126,
//     name: "1126",
//     realname: "刘一贤",
//     studentAnswer: [ ],
//     quality: 0
}
