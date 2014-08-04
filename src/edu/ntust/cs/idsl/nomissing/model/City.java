package edu.ntust.cs.idsl.nomissing.model;

import android.R.integer;

public enum City {
	
	TAIPEI_CITY(63000, "�x�_��"),
	NEW_TAIPEI_CITY(65000, "�s�_��"),
	KEELUNG_CITY(10017, "�򶩥�"),
	HUALIEN_COUNTY(10015, "�Ὤ��"),
	YILAN_COUNTY(10002, "�y����"),
	KINMEN_COUNTY(9020, "������"),	
	PENGHU_COUNTY(10016, "���"),
	TAINAN_CITY(67000, "�x�n��"),
	KAOHSIUNG_CITY(64000, "������"),
	CHIAYI_COUNTY(10010, "�Ÿq��"),
	CHIAYI_CITY(10020, "�Ÿq��"),
	MIAOLI_COUNTY(10005, "�]�߿�"),
	TAICHUNG_CITY(66000, "�x����"),
	TAOYUAN_COUNTY(10003, "��鿤"),
	HSINCHU_COUNTY(10004, "�s�˿�"),		
	HSINCHU_CITY(10018, "�s�˥�"),	
	PINGTUNG_COUNTY(10013, "�̪F��"),
	NANTOU_COUNTY(10008, "�n�뿤"),
	TAITUNG_COUNTY(10014, "�x�F��"),
	CHANGHUA_COUNTY(10007, "���ƿ�"),
	YUNLIN_COUNTY(10009, "���L��"),
	LIENCHIANG_COUNTY(9007, "�s����"); 		
	
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
