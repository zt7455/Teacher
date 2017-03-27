package com.shengliedu.teacher.teacher.fragment.homeworkdetail;

import java.util.List;

/**
 * Created by zt10 on 2017/2/23.
 */

public class NewHomeworkDetail {
//    id: 62,
//    question: "{"main":"","question":"<p>\u5355\u9009\u9898<\/p>\n"}",
//    answer: "{"lineCount":1,"alignType":1,"answers":[[{"title":"A","content":"a","image":""}],[{"title":"B","content":"b","image":""}],[{"title":"C","content":"c","image":""}]]}",
//    type: 1,
//    explanation: "",
//    user: 40165,
//    correct: "[{"title":"A","content":"a","image":""}]",
//    ident: 0,
//    order: 1,
//    hard: 0,
//    analysis: "",
//    user_realname: null,
//    from_school: 2,
//    from_school_name: null,
//    quote_count: 3,
//    score: 0,
//    delete: 0,
//    contentType: 0
    public int id;
    public int user;
    public int type;
    public String question;
    public String answer;
    public String explanation;
    public String correct;


//    {"main":"","question":"<p>\u5355\u9009\u9898<\/p>\n"}",
    public String main;
    public String contentLink;
//    answer: "{"lineCount":1,"alignType":1,"answers":[[{"title":"A","content":"a","image":""}],[{"title":"B","content":"b","image":""}],[{"title":"C","content":"c","image":""}]]}",
    public List<List<NewHomeworkDetail>> answers;
    public String title;
    public String content;

//    {type: 1, data: 'A'},   // 单选
    public String data;
}
