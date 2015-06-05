package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



//和服务器交响交互时的连接类
public class HttpUtil {

	public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
		
		new Thread(new Runnable() {	
			@Override
			public void run() {
				
				/*
				 * 我艹这里开始的时候写成HttpsURLConnection了！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
				 * 
				 * */
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder reponse = new StringBuilder();
					String line;
					while((line=reader.readLine()) != null) {
						reponse.append(line);
					}
					if(listener != null) {
						listener.onFinish(reponse.toString());
					}
					
				} catch(Exception e) {
					if(listener != null) {
						listener.onError(e);
					}
				} finally {
					if(connection != null) {
						connection.disconnect();
					}
				}
				
			}
		}).start();
	}
	
}
