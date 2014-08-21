package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;

public abstract class SQLiteDao {
	
	protected SQLiteDatabase db;
	protected NoMissingDB handler;
	
	protected SQLiteDao(Context context) {
		handler = new NoMissingDB(context);
	}
	
	protected void open() throws SQLException {
		db = handler.getWritableDatabase();
	}

	protected void close() {
		handler.close();
	}	

}
