package com.shengliedu.teacher.teacher.bean;

import java.util.List;

public class ClassRoomType {
	// id: 18,
	// name: "中级",
	// schoolstage: 0,
	// order: 2,
	// show: 1
	public String name;
	public int id;
	public List<IdName> teachers;
	
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
		ClassRoomType other = (ClassRoomType) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClassRoomType {name=" + name + ", id=" + id + ", teachers="
				+ teachers + "}";
	}
}
