package edu.ntust.cs.idsl.nomissing.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;

public abstract class DatabaseDAO<T> implements IDatabaseDAO<T> {
	
	protected SQLiteDatabase db;
	protected NoMissingDB handler;
	
	protected DatabaseDAO(Context context) {
		handler = new NoMissingDB(context);
	}
	
	protected void open() throws SQLException {
		db = handler.getWritableDatabase();
	}

	protected void close() {
		handler.close();
	}

}
