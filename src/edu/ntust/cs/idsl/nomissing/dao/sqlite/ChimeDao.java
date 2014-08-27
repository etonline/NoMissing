package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.ntust.cs.idsl.nomissing.dao.ISimpleDao;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;
import edu.ntust.cs.idsl.nomissing.model.Chime;

public class ChimeDao extends SQLiteDao implements ISimpleDao<Chime> {

	private static final String[] CHIMES_COLUMNS = new String[] {
		NoMissingDB.CHIMES_KEY_ID,
		NoMissingDB.CHIMES_KEY_HOUR,
		NoMissingDB.CHIMES_KEY_MINUTE,
		NoMissingDB.CHIMES_KEY_IS_ENABLED,
		NoMissingDB.CHIMES_KEY_IS_REPEATING,
		NoMissingDB.CHIMES_KEY_IS_TRIGGERED,
		NoMissingDB.CHIMES_KEY_AUDIO,
		NoMissingDB.CHIMES_KEY_CREATED_AT,
		NoMissingDB.CHIMES_KEY_UPDATED_AT
	};
	
	public ChimeDao(Context context) {
		super(context);
	}

	@Override
	public int insert(Chime chime) {
		ContentValues values = new ContentValues();
		values.put(NoMissingDB.CHIMES_KEY_HOUR, chime.getHour());
		values.put(NoMissingDB.CHIMES_KEY_MINUTE, chime.getMinute());
		values.put(NoMissingDB.CHIMES_KEY_IS_ENABLED, chime.isEnabled());
		values.put(NoMissingDB.CHIMES_KEY_IS_REPEATING, chime.isRepeating());
		values.put(NoMissingDB.CHIMES_KEY_IS_TRIGGERED, chime.isTriggered());
		values.put(NoMissingDB.CHIMES_KEY_AUDIO, chime.getAudio());
		values.put(NoMissingDB.CHIMES_KEY_CREATED_AT, chime.getCreatedAt());
		values.put(NoMissingDB.CHIMES_KEY_UPDATED_AT, chime.getUpdatedAt());

		open();
		int row = (int) db.insert(NoMissingDB.TABLE_CHIMES, null, values);
		close();

		return row;
	}

	@Override
	public int update(Chime chime) {
		String whereClause = NoMissingDB.CHIMES_KEY_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(chime.getId()) };

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.CHIMES_KEY_HOUR, chime.getHour());
		values.put(NoMissingDB.CHIMES_KEY_MINUTE, chime.getMinute());
		values.put(NoMissingDB.CHIMES_KEY_IS_ENABLED, chime.isEnabled());
		values.put(NoMissingDB.CHIMES_KEY_IS_REPEATING, chime.isRepeating());
		values.put(NoMissingDB.CHIMES_KEY_IS_TRIGGERED, chime.isTriggered());
		values.put(NoMissingDB.CHIMES_KEY_AUDIO, chime.getAudio());
		values.put(NoMissingDB.CHIMES_KEY_CREATED_AT, chime.getCreatedAt());
		values.put(NoMissingDB.CHIMES_KEY_UPDATED_AT, chime.getUpdatedAt());		

		open();
		int row =  db.update(NoMissingDB.TABLE_CHIMES, values, whereClause, whereArgs);
		close();
		
		return row;
	}

	@Override
	public int delete(int id) {
		String whereClause = NoMissingDB.CHIMES_KEY_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		
		open();
		int row = db.delete(NoMissingDB.TABLE_CHIMES, whereClause, whereArgs);
		close();
		
		return row;
	}

	@Override
	public List<Chime> findAll() {
		List<Chime> chimes = new ArrayList<Chime>();
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_CHIMES, CHIMES_COLUMNS, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Chime chime = new Chime(
					cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_ID)), 
					cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_HOUR)),
					cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_MINUTE)), 
					cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_IS_ENABLED)) > 0,
					cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_IS_REPEATING)) > 0, 
					cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_IS_TRIGGERED)) > 0,
					cursor.getString(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_AUDIO)),
					cursor.getLong(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_CREATED_AT)), 
					cursor.getLong(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_UPDATED_AT)));
			chimes.add(chime);
		}
		cursor.close();
		close();
		
		return chimes;
	}

	@Override
	public Chime find(int id) {
		Chime chime = new Chime();
		String selection = NoMissingDB.CHIMES_KEY_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(id) };		
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_CHIMES, CHIMES_COLUMNS, selection, selectionArgs, null, null, null, null);
		if (cursor.moveToFirst()) {   
			chime.setId(cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_ID)));
			chime.setHour(cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_HOUR)));
			chime.setMinute(cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_MINUTE)));
			chime.setEnabled(cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_IS_ENABLED)) > 0);
			chime.setRepeating(cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_IS_REPEATING)) > 0);
			chime.setTriggered(cursor.getInt(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_IS_TRIGGERED)) > 0);
			chime.setAudio(cursor.getString(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_AUDIO)));
			chime.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_CREATED_AT)));
			chime.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.CHIMES_KEY_UPDATED_AT)));			
		}
		cursor.close();
		close();
		
		return chime;
	}

}
