package com.shengliedu.teacher.teacher.bean;

import java.io.Serializable;

public class MessageList implements Serializable{
//	id: 30,
//    title: "通知标题",
//    content: "<p>22222222222222222222222222222222222222222222222222</p> ",
//    user: 70,
//    attach: "http://192.168.1.207/data/notice/Hydrangeas.jpg",
//    publish: 1,
//    source: 0,
//    created_at: "2016-03-31 14:50:35",
//    updated_at: "2016-03-31 14:50:48",
//    type: 1,
//    typeName: "公告",
//    date: "2016-03-31"
	
//	"id": 152,
//  "title": "放假通知1",
//  "content": "<p>十一放假七天，从一号到七号，请各位老师和家长做好放假前的准备</p>",
//  "addTime": 1438566401,
//  "addTimeStr": "2015-08-03 09:46:41",
//  "addUser": 1,
//  "schoolName": "中国星火教育学校"
	public int id;
	public String title;
	public String content;
	public String date;
	public String attach;
	public String typeName;
	public String type;
}
