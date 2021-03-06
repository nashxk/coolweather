package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;


//解析服务器返回的数据
public class Utility {

	/*
	 * 解析和处理服务器返回的省级数据
	 * 01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|陕西,12|山东,
	 * 13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,
	 * 25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾
	 * */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
		if(!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if(allProvinces!=null && allProvinces.length>0) {
				for(String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 解析和处理服务器返回的市级数据
	 * 1201|济南,1202|青岛,1203|淄博,1204|德州,1205|烟台,1206|潍坊,1207|济宁,1208|泰安,1209|临沂,
	 * 1210|菏泽,1211|滨州,1212|东营,1213|威海,1214|枣庄,1215|日照,1216|莱芜,1217|聊城
	 * */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if(!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if(allCities!=null && allCities.length>0) {
				for(String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();				
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 解析和处理服务器返回的县级数据
	 * 120601|潍坊,120602|青州,120603|寿光,120604|临朐,120605|昌乐,120606|昌邑,120607|安丘,120608|高密,120609|诸城
	 * */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String reponse, int cityId) {
		if(!TextUtils.isEmpty(reponse)) {
			String[] allCounties = reponse.split(",");
			if(allCounties!=null && allCounties.length>0) {
				for(String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
	 * 
	 * {
	 *    "weatherinfo":{
	 *       "city":"安丘",
	 *       "cityid":"101120607",
	 *       "temp1":"12℃",
	 *       "temp2":"3℃",
	 *       "weather":"小雨转多云",
	 *       "img1":"d7.gif",
	 *       "img2":"n1.gif",
	 *       "ptime":"08:00"
	 *    }
	 * }
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中
	 */
	public static void saveWeatherInfo(Context context, String cityName, 
			String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.US);
		
		/**
		 * 突然发现，在真机上日期显示正常，但是虚拟机上显示不正常。
		 * 
		 * 这样才能正常显示本地日期。
		 * */
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Date date = new Date();
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		//editor.putString("current_date", sdf.format(new Date()));			这样显示的时间是格林尼治时间。
		editor.putString("current_date", sdf.format(date));
		Log.d("WeatherActivity", date.toString());
		editor.commit();
	}
	
}
