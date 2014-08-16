package edu.ntust.cs.idsl.nomissing.model;

import edu.ntust.cs.idsl.nomissing.R;

public enum City {
	
	TAIPEI_CITY(63000, R.string.taipei_city),
	NEW_TAIPEI_CITY(65000, R.string.new_taipei_city),
	KEELUNG_CITY(10017, R.string.keelung_city),
	HUALIEN_COUNTY(10015, R.string.hualien_county),
	YILAN_COUNTY(10002, R.string.yilan_county),
	KINMEN_COUNTY(9020, R.string.kinmen_county),	
	PENGHU_COUNTY(10016, R.string.penghu_county),
	TAINAN_CITY(67000, R.string.tainan_city),
	KAOHSIUNG_CITY(64000, R.string.kaohsiung_city),
	CHIAYI_COUNTY(10010, R.string.chiayi_county),
	CHIAYI_CITY(10020, R.string.chiayi_city),
	MIAOLI_COUNTY(10005, R.string.miaoli_county),
	TAICHUNG_CITY(66000, R.string.taichung_city),
	TAOYUAN_COUNTY(10003, R.string.taoyuan_county),
	HSINCHU_COUNTY(10004, R.string.hsinchu_county),		
	HSINCHU_CITY(10018, R.string.hsinchu_city),	
	PINGTUNG_COUNTY(10013, R.string.pingtung_county),
	NANTOU_COUNTY(10008, R.string.nantou_county),
	TAITUNG_COUNTY(10014, R.string.taitung_county),
	CHANGHUA_COUNTY(10007, R.string.changhua_county),
	YUNLIN_COUNTY(10009, R.string.yunlin_county),
	LIENCHIANG_COUNTY(9007, R.string.lienchiang_county); 		
	
	private int cityID;
	private int cityName;
	
	private City(int cityID, int cityName) {
		this.cityID = cityID;
		this.cityName = cityName;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public int getCityName() {
		return cityName;
	}

	public void setCityName(int cityName) {
		this.cityName = cityName;
	}
	
	public static City getCity(int cityID) {
		City city = null;
		for (City c : City.values()) {
			if (c.getCityID() == cityID) {
				city = c;
				break;
			}
		}
		return city;
	}
	
}
