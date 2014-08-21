package edu.ntust.cs.idsl.nomissing.dao;

import android.content.Context;

public class SQLiteDaoFactory {
	
	public static WeatherDao getWeatherDao(Context context) {
		return new WeatherDao(context);
	}
	
	public static ChimeDao getChimeDao(Context context) {
		return new ChimeDao(context);
	}		
	
}
