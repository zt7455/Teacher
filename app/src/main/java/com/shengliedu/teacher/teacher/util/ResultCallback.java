package com.shengliedu.teacher.teacher.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public interface ResultCallback {
	 public void onResponse(Call call, Response response, String json);
	public void onFailure(Call call, IOException exception) ;
}
