package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.ntust.cs.idsl.nomissing.dao.ISimpleDao;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;
import edu.ntust.cs.idsl.nomissing.model.Weather;

public class WeatherDao extends SQLiteDao implements ISimpleDao<Weather> {
	
	private static final String[] WEATHER_COLUMNS = {
		NoMissingDB.WEATHER_KEY_CITYID,
		NoMissingDB.WEATHER_KEY_NAME,
		NoMissingDB.WEATHER_KEY_STNO,
		NoMissingDB.WEATHER_KEY_TIME,
		NoMissingDB.WEATHER_KEY_MEMO,
		NoMissingDB.WEATHER_KEY_AUDIO,
		NoMissingDB.WEATHER_KEY_CREATED_AT,
		NoMissingDB.WEATHER_KEY_UPDATED_AT
	};
	
	public WeatherDao(Context context) {
		super(context);
	}

	@Override
	public int insert(Weather weather) {
		ContentValues values = new ContentValues();
		values.put(NoMissingDB.WEATHER_KEY_CITYID, weather.getCityID());
		values.put(NoMissingDB.WEATHER_KEY_NAME, weather.getCity());
		values.put(NoMissingDB.WEATHER_KEY_STNO, weather.getStno());
		values.put(NoMissingDB.WEATHER_KEY_TIME, weather.getTime());
		values.put(NoMissingDB.WEATHER_KEY_MEMO, weather.getMemo());
		values.put(NoMissingDB.WEATHER_KEY_AUDIO, weather.getAudio());
		values.put(NoMissingDB.WEATHER_KEY_CREATED_AT, weather.getCreatedAt());
		values.put(NoMissingDB.WEATHER_KEY_UPDATED_AT, weather.getUpdatedAt());

		open();
		int row = (int) db.insert(NoMissingDB.TABLE_WEATHER, null, values);
		close();

		return row;
	}

	@Override
	public int update(Weather weather) {
		String whereClause = NoMissingDB.WEATHER_KEY_CITYID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(weather.getCityID()) };

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.WEATHER_KEY_CITYID, weather.getCityID());
		values.put(NoMissingDB.WEATHER_KEY_NAME, weather.getCity());
		values.put(NoMissingDB.WEATHER_KEY_STNO, weather.getStno());
		values.put(NoMissingDB.WEATHER_KEY_TIME, weather.getTime());
		values.put(NoMissingDB.WEATHER_KEY_MEMO, weather.getMemo());
		values.put(NoMissingDB.WEATHER_KEY_AUDIO, weather.getAudio());
		values.put(NoMissingDB.WEATHER_KEY_CREATED_AT, weather.getCreatedAt());
		values.put(NoMissingDB.WEATHER_KEY_UPDATED_AT, weather.getUpdatedAt());

		open();
		int row = db.update(NoMissingDB.TABLE_WEATHER, values, whereClause, whereArgs);
		close();

		return row;
	}

	@Override
	public int delete(int id) {
		String whereClause = NoMissingDB.WEATHER_KEY_CITYID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(id) };

		open();
		int row = db.delete(NoMissingDB.TABLE_WEATHER, whereClause, whereArgs);
		close();

		return row;
	}

	@Override
	public List<Weather> findAll() {
		List<Weather> weatherList = new ArrayList<Weather>();

		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_WEATHER, WEATHER_COLUMNS, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Weather weather = new Weather(
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_CITYID))), 
					cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_NAME)), 
					cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_STNO)), 
					cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_TIME)),
					cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_MEMO)), 
					cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_AUDIO)),
					cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_CREATED_AT)), 
					cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_UPDATED_AT)));
			weatherList.add(weather);
		}
		cursor.close();
		close();
		
		return weatherList;
	}

	@Override
	public Weather find(int cityID) {
		Weather weather = new Weather();
		String selection = NoMissingDB.WEATHER_KEY_CITYID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(cityID) };
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_WEATHER, WEATHER_COLUMNS, selection, selectionArgs, null, null, null, null);
		if (cursor.moveToFirst()) {   
			weather.setCityID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_CITYID)))); 
			weather.setCity(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_NAME))); 
			weather.setStno(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_STNO))); 
			weather.setMemo(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_MEMO)));
			weather.setTime(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_TIME)));
			weather.setAudio(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_AUDIO)));
			weather.setCreatedAt(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_CREATED_AT)));
			weather.setUpdatedAt(cursor.getString(cursor.getColumnIndex(NoMissingDB.WEATHER_KEY_UPDATED_AT)));		
		}
		cursor.close();
		close();
		
		return weather;
	}

}
