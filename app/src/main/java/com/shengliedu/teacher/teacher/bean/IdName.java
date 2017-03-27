package com.shengliedu.teacher.teacher.bean;

import java.io.Serializable;
import java.util.List;

public class IdName implements Serializable{
	// id: 18,
	// name: "中级",
	// schoolstage: 0,
	// order: 2,
	// show: 1
	public int id;
    public int count;
    public int student;
    public int parent;
    public int imageCount;
	public String name;
	public String realname;
	public List<IdName> items;
	public List<IdName> fileArr;
	
	
//	location: "data/aaa/aaa.doc",
//    user: 70,
//    type: 1,
//    addTime: 123456789
	public String location;
	public String updated_at;
	public int user;
	public long size;
	public int type;
	public long addTime;
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
		IdName other = (IdName) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IdName {id=" + id + ", name=" + name + "}";
	}
}
