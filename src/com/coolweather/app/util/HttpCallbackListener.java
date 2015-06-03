package com.coolweather.app.util;


//回调服务返回的结果，有HttpUtil实现
public interface HttpCallbackListener {

	void onFinish(String response);
	void onError(Exception e);
	
}
