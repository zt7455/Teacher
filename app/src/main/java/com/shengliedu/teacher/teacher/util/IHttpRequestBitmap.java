package com.shengliedu.teacher.teacher.util;
import com.alibaba.fastjson.JSONObject;


public interface IHttpRequestBitmap {
	public void onFailure(Throwable t, int errorNo, String strMsg);
	public void onSuccess(JSONObject t, String code);
	public void onLoading(long count, long current);
}
