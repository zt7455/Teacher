package com.shengliedu.teacher.teacher.bean;

import java.io.Serializable;

/**
 * Created by zt10 on 2017/2/20.
 */

public class courseware_content implements Serializable{
//    id: 892,
//    courseware_catalog: 331,
//    order: 1,
//    teaching: "{"name":"0ffda2c379310a554bf91344b14543a9802610e9.jpg","subType":1,"school":2,"localid":2929,"content":"","file":"data\/user\/40165\/res\/4080\/41146\/0ffda2c379310a554bf91344b14543a9802610e9.jpg","link":"data\/user\/40165\/res\/4080\/41146\/0ffda2c379310a554bf91344b14543a9802610e9.jpg","submitType":5}",
//    ebook: "{"showType":1,"sData":[],"sText":""}",
//    usertype: 1,
//    classhours: "[]",
//    content_type: 1,
//    content_id: 2929,
//    mode: 0,
//    from: 1,
//    fromSchool: 2,
//    quoteCount: 0,
//    school: 2,
//    submitType: 5,
//    part: 3,
//    subject: 3

    public int id;
    public String teaching;
    public String answer;
    public int submitType;
    public int wrong;
    public int content_id;
    public int content_type;
    public int part;
    public int subject;
    public int content=0;
    public int school;
    public int from;
    public int activity;
    public int evaluate;
}
