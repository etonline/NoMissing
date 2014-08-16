package edu.ntust.cs.idsl.nomissing.dao;

import android.content.Context;

public class SQLiteDAOFactory {
	
	public static WeatherDAO getWeatherDAO(Context context) {
		return WeatherDAO.getInstance(context);
	}
	
	public static ChimeDAO getChimeDAO(Context context) {
		return ChimeDAO.getInstance(context);
	}		
	
}
