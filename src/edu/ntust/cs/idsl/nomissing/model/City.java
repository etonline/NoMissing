package edu.ntust.cs.idsl.nomissing.model;

import android.R.integer;

public enum City {
	
	TAIPEI_CITY(63000, "台北市"),
	NEW_TAIPEI_CITY(65000, "新北市"),
	KEELUNG_CITY(10017, "基隆市"),
	HUALIEN_COUNTY(10015, "花蓮縣"),
	YILAN_COUNTY(10002, "宜蘭縣"),
	KINMEN_COUNTY(9020, "金門縣"),	
	PENGHU_COUNTY(10016, "澎湖縣"),
	TAINAN_CITY(67000, "台南市"),
	KAOHSIUNG_CITY(64000, "高雄市"),
	CHIAYI_COUNTY(10010, "嘉義縣"),
	CHIAYI_CITY(10020, "嘉義市"),
	MIAOLI_COUNTY(10005, "苗栗縣"),
	TAICHUNG_CITY(66000, "台中市"),
	TAOYUAN_COUNTY(10003, "桃園縣"),
	HSINCHU_COUNTY(10004, "新竹縣"),		
	HSINCHU_CITY(10018, "新竹市"),	
	PINGTUNG_COUNTY(10013, "屏東縣"),
	NANTOU_COUNTY(10008, "南投縣"),
	TAITUNG_COUNTY(10014, "台東縣"),
	CHANGHUA_COUNTY(10007, "彰化縣"),
	YUNLIN_COUNTY(10009, "雲林縣"),
	LIENCHIANG_COUNTY(9007, "連江縣"); 		
	
	private int cityID;
	private String cityName;
	
	private City(int cityID, String cityName) {
		this.cityID = cityID;
		this.cityName = cityName;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public static String getCityNameByCityID(int cityID) {
		String CityName = new String();
		for (City city : City.values()) {
			if (city.getCityID() == cityID) {
				CityName = city.getCityName();
				break;
			}
		}
		return CityName;
	}
	
}
