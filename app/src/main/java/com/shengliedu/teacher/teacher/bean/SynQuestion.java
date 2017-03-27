package com.shengliedu.teacher.teacher.bean;

import java.util.List;

public class SynQuestion {
	public int id;
	public SynQuestionContent question;
	public SynAnswer answer;
	public String type;
	public String explanation;
	public String user;
	public String created_at;
	public String updated_at;
	public List<SynCorrect> correct;
	public String ident;
	public String order;
	public List<SynCorrect> studentAnswer;
	public String rightwrong;
	public String percentage;
}
