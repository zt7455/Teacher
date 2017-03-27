package com.shengliedu.teacher.teacher.bean;

import java.io.Serializable;
import java.util.List;

public class HomeworkInfo implements Serializable{
	public Normal activity;
	public List<HomeworkshowStudent> studentArr;
	public List<ImageInfo> imageArr;
	public String activityTitle;
}
