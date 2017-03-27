package com.shengliedu.teacher.teacher.util;

import com.alibaba.fastjson.JSONArray;

public interface ResultCallBack2 {
	
	public abstract void onSuccess(JSONArray result);
	public abstract void onFailure(String result);
}
