package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.dao.ISimpleDao;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;
import edu.ntust.cs.idsl.nomissing.model.Chime;

public class ChimeDao extends SQLiteDao implements ISimpleDao<Chime> {

	public ChimeDao(Context context) {
		super(context);
	}

	@Override
	public int insert(Chime chime) {
		open();

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.CHIMES_KEY_ID, chime.getId()); 
		values.put(NoMissingDB.CHIMES_KEY_HOUR, chime.getHour());
		values.put(NoMissingDB.CHIMES_KEY_MINUTE, chime.getMinute());
		values.put(NoMissingDB.CHIMES_KEY_IS_ENABLED, chime.isEnabled());
		values.put(NoMissingDB.CHIMES_KEY_IS_REPEATING, chime.isRepeating());
		values.put(NoMissingDB.CHIMES_KEY_IS_TRIGGERED, chime.isTriggered());
		values.put(NoMissingDB.CHIMES_KEY_AUDIO, chime.getAudio());
		values.put(NoMissingDB.CHIMES_KEY_CREATED_AT, chime.getCreatedAt());
		values.put(NoMissingDB.CHIMES_KEY_UPDATED_AT, chime.getUpdatedAt());

		int row = (int)db.insert(NoMissingDB.TABLE_CHIMES, null, values);
		close();
		
		return row;
	}

	@Override
	public int update(Chime chime) {
		open();

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.CHIMES_KEY_ID, chime.getId()); 
		values.put(NoMissingDB.CHIMES_KEY_HOUR, chime.getHour());
		values.put(NoMissingDB.CHIMES_KEY_MINUTE, chime.getMinute());
		values.put(NoMissingDB.CHIMES_KEY_IS_ENABLED, chime.isEnabled());
		values.put(NoMissingDB.CHIMES_KEY_IS_REPEATING, chime.isRepeating());
		values.put(NoMissingDB.CHIMES_KEY_IS_TRIGGERED, chime.isTriggered());
		values.put(NoMissingDB.CHIMES_KEY_AUDIO, chime.getAudio());
		values.put(NoMissingDB.CHIMES_KEY_CREATED_AT, chime.getCreatedAt());
		values.put(NoMissingDB.CHIMES_KEY_UPDATED_AT, chime.getUpdatedAt());		

		int row =  db.update(NoMissingDB.TABLE_CHIMES, values, NoMissingDB.CHIMES_KEY_ID + " = ?",
				new String[] { String.valueOf(chime.getId()) });
		
		close();
		
		return row;
	}

	@Override
	public int delete(int id) {
		open();
		int row = db.delete(NoMissingDB.TABLE_CHIMES, 
				NoMissingDB.CHIMES_KEY_ID + " = ?", new String[] { String.valueOf(id) });
		close();
		
		return row;
	}

	@Override
	public List<Chime> findAll() {
		List<Chime> chimeList = new ArrayList<Chime>();
		String selectQuery = "SELECT  * FROM " + NoMissingDB.TABLE_CHIMES;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Chime chime = new Chime(
						cursor.getInt(0), 
						cursor.getInt(1),
						cursor.getInt(2), 
						cursor.getInt(3) > 0,
						cursor.getInt(4) > 0, 
						cursor.getInt(5) > 0,
						cursor.getString(6),
						cursor.getLong(7), 
						cursor.getLong(8));
				
				chimeList.add(chime);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		
		return chimeList;
	}

	@Override
	public Chime find(int id) {
		open();

		Cursor cursor = db
				.query(NoMissingDB.TABLE_CHIMES, new String[] {
						NoMissingDB.CHIMES_KEY_ID,
						NoMissingDB.CHIMES_KEY_HOUR,
						NoMissingDB.CHIMES_KEY_MINUTE,
						NoMissingDB.CHIMES_KEY_IS_ENABLED,
						NoMissingDB.CHIMES_KEY_IS_REPEATING,
						NoMissingDB.CHIMES_KEY_IS_TRIGGERED,
						NoMissingDB.CHIMES_KEY_AUDIO,
						NoMissingDB.CHIMES_KEY_CREATED_AT,
						NoMissingDB.CHIMES_KEY_UPDATED_AT },
						NoMissingDB.CHIMES_KEY_ID + "=?",
						new String[] { String.valueOf(id) }, null, null,
						null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Chime chime = new Chime(
				cursor.getInt(0), 
				cursor.getInt(1),
				cursor.getInt(2), 
				cursor.getInt(3) > 0,
				cursor.getInt(4) > 0, 
				cursor.getInt(5) > 0,
				cursor.getString(6),
				cursor.getLong(7), 
				cursor.getLong(8));
		
		cursor.close();
		close();
		
		return chime;
	}
	
	/**
	 * Returns the next available ID to be assigned to a new task. This
	 * number is equal to the highest current ID + 1.
	 * 
	 * @return the next available task ID to be assigned to a new task
	 */
	public int getNextID() {

		String selectQuery = "SELECT MAX(" + NoMissingDB.CHIMES_KEY_ID +
				") FROM " + NoMissingDB.TABLE_CHIMES;
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()){
			int i = cursor.getInt(0) + 1;
			cursor.close();
			close();
			return i;
		}
		else{
			cursor.close();
			close();
			return 1;
		}
	}

}
