package com.shengliedu.teacher.teacher.bean;

import java.util.List;

/**
 * Created by zt10 on 2016/12/16.
 */

public class HomeworkshowStudent {
    public int type;
    public int id;
    public int imageCount;
    public String name;
    public String realname;
    public String describe;
    public HomeworkshowHistory homeworkshowHistory;
    public List<ImageInfo> images;
    public List<IdName> students;
    public int activity;
}
