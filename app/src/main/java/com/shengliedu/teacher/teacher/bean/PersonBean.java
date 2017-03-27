package com.shengliedu.teacher.teacher.bean;

import java.io.Serializable;

public class PersonBean implements Serializable{
	public int id;
	public int behavior;
	public int checkFinish;
	public int submitCount;
	public int evaluate;
	public String checkNote;
	public String comment;
	public String realname;
	public String behaviorNote;
	public String pinyin;
	public String submitTime;
	public String firstpinyin;
	@Override
	public String toString() {
		return "PersonBean [id=" + id + "]";
	}
	
}
