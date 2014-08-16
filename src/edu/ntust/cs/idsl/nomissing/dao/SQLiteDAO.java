package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;

public abstract class SQLiteDAO<T> {
	
	protected SQLiteDatabase db;
	protected NoMissingDB handler;
	
	protected SQLiteDAO(Context context) {
		handler = new NoMissingDB(context);
	}
	
	protected void open() throws SQLException {
		db = handler.getWritableDatabase();
	}

	protected void close() {
		handler.close();
	}
	
	public abstract int insert(T entity);

	public abstract int update(T entity);

	public abstract int delete(int id);

	public abstract List<T> findAll();

	public abstract T find(int id);	

}
