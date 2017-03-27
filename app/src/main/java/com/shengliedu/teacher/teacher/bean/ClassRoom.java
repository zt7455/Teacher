package com.shengliedu.teacher.teacher.bean;

import java.util.List;

public class ClassRoom {
	// id: 18,
	// name: "中级",
	// schoolstage: 0,
	// order: 2,
	// show: 1
	public int id;
	public int grade;
	public int subject;
	public String name;
	public int classroom_type;
	public int type;
	public int editionVersion;
	public List<ClassRoom> classrooms;
	public List<ClassRoom> subjects;
	public List<IdName> teachers;
	public List<ClassRoomType> classroomType;
	
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
		ClassRoom other = (ClassRoom) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", name=" + name + ", classrooms="
				+ classrooms + "}";
	}

	
}
