package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;


//�������������ص�����
public class Utility {

	/*
	 * �����ʹ�����������ص�ʡ������
	 * 01|����,02|�Ϻ�,03|���,04|����,05|������,06|����,07|����,08|���ɹ�,09|�ӱ�,10|ɽ��,11|����,12|ɽ��,
	 * 13|�½�,14|����,15|�ຣ,16|����,17|����,18|����,19|����,20|����,21|�㽭,22|����,23|����,24|����,
	 * 25|����,26|����,27|�Ĵ�,28|�㶫,29|����,30|����,31|����,32|���,33|����,34|̨��
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
	 * �����ʹ�����������ص��м�����
	 * 1201|����,1202|�ൺ,1203|�Ͳ�,1204|����,1205|��̨,1206|Ϋ��,1207|����,1208|̩��,1209|����,
	 * 1210|����,1211|����,1212|��Ӫ,1213|����,1214|��ׯ,1215|����,1216|����,1217|�ĳ�
	 * */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if(!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if(allCities!=null && allCities.length>0) {
				for(String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setProvinceId(provinceId);
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * �����ʹ�����������ص��ؼ�����
	 * 120601|Ϋ��,120602|����,120603|�ٹ�,120604|����,120605|����,120606|����,120607|����,120608|����,120609|���
	 * */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String reponse, int cityId) {
		if(!TextUtils.isEmpty(reponse)) {
			String[] allCounties = reponse.split(",");
			if(allCounties!=null && allCounties.length>0) {
				for(String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCityId(cityId);
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
}
