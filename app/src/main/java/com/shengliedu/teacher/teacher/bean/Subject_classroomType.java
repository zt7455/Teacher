package com.shengliedu.teacher.teacher.bean;

import java.util.ArrayList;
import java.util.List;

public class Subject_classroomType {
	public String name;
	public List<ClassRoomType> classroomType=new ArrayList<ClassRoomType>();
	public int id;
	
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
		Subject_classroomType other = (Subject_classroomType) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Subject_classroomType {name=" + name + ", classroomType="
				+ classroomType + ", id=" + id + "}";
	}
}
