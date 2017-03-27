package com.shengliedu.teacher.teacher.bean;

import java.io.Serializable;
import java.util.List;

public class TeachBookContent implements Serializable{
	public int id;
	public String name;
	public int parent;
	public int page=-1;
	public int page0=-1;
	public List<TeachBookContent> items;
}
