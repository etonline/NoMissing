package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.ntust.cs.idsl.nomissing.sqlite.NoMissingDB;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
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
