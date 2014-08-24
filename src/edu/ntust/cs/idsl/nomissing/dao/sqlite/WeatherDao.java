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
	
	public WeatherDao(Context context) {
		super(context);
	}

	@Override
	public int insert(Weather weather) {
		open();

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.WEATHER_KEY_CITYID, weather.getCityID()); 
		values.put(NoMissingDB.WEATHER_KEY_NAME, weather.getCity());
		values.put(NoMissingDB.WEATHER_KEY_STNO, weather.getStno());
		values.put(NoMissingDB.WEATHER_KEY_TIME, weather.getTime());
		values.put(NoMissingDB.WEATHER_KEY_MEMO, weather.getMemo());
		values.put(NoMissingDB.WEATHER_KEY_AUDIO, weather.getAudio());
		values.put(NoMissingDB.WEATHER_KEY_CREATED_AT, weather.getCreatedAt());
		values.put(NoMissingDB.WEATHER_KEY_UPDATED_AT, weather.getUpdatedAt());

		int row = (int)db.insert(NoMissingDB.TABLE_WEATHER, null, values);
		close();
		
		return row;
	}

	@Override
	public int update(Weather weather) {
		open();

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.WEATHER_KEY_CITYID, weather.getCityID()); 
		values.put(NoMissingDB.WEATHER_KEY_NAME, weather.getCity());
		values.put(NoMissingDB.WEATHER_KEY_STNO, weather.getStno());
		values.put(NoMissingDB.WEATHER_KEY_TIME, weather.getTime());
		values.put(NoMissingDB.WEATHER_KEY_MEMO, weather.getMemo());
		values.put(NoMissingDB.WEATHER_KEY_AUDIO, weather.getAudio());
		values.put(NoMissingDB.WEATHER_KEY_CREATED_AT, weather.getCreatedAt());
		values.put(NoMissingDB.WEATHER_KEY_UPDATED_AT, weather.getUpdatedAt());
		
		int row =  db.update(NoMissingDB.TABLE_WEATHER, values, NoMissingDB.WEATHER_KEY_CITYID + " = ?",
				new String[] { String.valueOf(weather.getCityID()) });
		
		close();
		
		return row;
	}

	@Override
	public int delete(int id) {
		open();
		int row = db.delete(NoMissingDB.TABLE_WEATHER, 
				NoMissingDB.WEATHER_KEY_CITYID + " = ?", new String[] { String.valueOf(id) });
		close();
		
		return row;
	}

	@Override
	public List<Weather> findAll() {
		List<Weather> weatherList = new ArrayList<Weather>();
		String selectQuery = "SELECT  * FROM " + NoMissingDB.TABLE_WEATHER;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Weather weather = new Weather(
						Integer.parseInt(cursor.getString(0)), 
						cursor.getString(1),
						cursor.getString(2), 
						cursor.getString(3),
						cursor.getString(4), 
						cursor.getString(5),
						cursor.getString(6), 
						cursor.getString(7));
				
				weatherList.add(weather);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		
		return weatherList;
	}

	@Override
	public Weather find(int cityID) {
		open();

		Cursor cursor = db
				.query(NoMissingDB.TABLE_WEATHER, new String[] {
						NoMissingDB.WEATHER_KEY_CITYID, 
						NoMissingDB.WEATHER_KEY_NAME,
						NoMissingDB.WEATHER_KEY_STNO, 
						NoMissingDB.WEATHER_KEY_TIME,
						NoMissingDB.WEATHER_KEY_MEMO, 
						NoMissingDB.WEATHER_KEY_AUDIO,
						NoMissingDB.WEATHER_KEY_CREATED_AT,
						NoMissingDB.WEATHER_KEY_UPDATED_AT },
						NoMissingDB.WEATHER_KEY_CITYID + "=?",
						new String[] { String.valueOf(cityID) }, null, null,
						null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Weather weather = new Weather(
				Integer.parseInt(cursor.getString(0)), 
				cursor.getString(1),
				cursor.getString(2), 
				cursor.getString(3),
				cursor.getString(4), 
				cursor.getString(5),
				cursor.getString(6), 
				cursor.getString(7));
		
		cursor.close();
		close();
		
		return weather;
	}

}
