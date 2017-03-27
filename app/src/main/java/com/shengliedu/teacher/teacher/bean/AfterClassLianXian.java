package com.shengliedu.teacher.teacher.bean;

import java.util.List;

public class AfterClassLianXian {
	public String id;
	public Afterquestion question;
	public AfteranswerLianXian answer;
	public String type;
	public String explanation;
	public String user;
	public String created_at;
	public String updated_at;
	public List<Correct> correct;
	public String ident;
	public String order;
	public String typeName;
	public List<StudentAnswer> studentAnswer;
	public String rightWrong;
	public String percentage;
}
