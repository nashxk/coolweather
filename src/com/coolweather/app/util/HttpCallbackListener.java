package com.coolweather.app.util;


//�ص����񷵻صĽ������HttpUtilʵ��
public interface HttpCallbackListener {

	void onFinish(String response);
	void onError(Exception e);
	
}
