package com.shengliedu.teacher.teacher.bean;

import java.util.ArrayList;
import java.util.List;

public class Grade_Subject_classroomType {
	public int id;
	public String name;
	public List<Subject_classroomType> subject_classroomtype=new ArrayList<Subject_classroomType>();
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grade_Subject_classroomType other = (Grade_Subject_classroomType) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Grade_Subject_classroomType {id=" + id + ", name=" + name
				+ ", subject_classroomtype=" + subject_classroomtype + "}";
	}
	
	
}
