package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.ntust.cs.idsl.nomissing.dao.ISimpleDao;
import edu.ntust.cs.idsl.nomissing.model.SMSMessage;
import edu.ntust.cs.idsl.nomissing.sqlite.NoMissingDB;

public class SMSMessageDao extends SQLiteDao implements ISimpleDao<SMSMessage> {

    private static final String[] SMSMESSAGES_COLUMNS = new String[] {
            NoMissingDB.SMSMESSAGES_KEY_ID, 
            NoMissingDB.SMSMESSAGES_KEY_FROM,
            NoMissingDB.SMSMESSAGES_KEY_MESSAGE,
            NoMissingDB.SMSMESSAGES_KEY_TIME, 
            NoMissingDB.SMSMESSAGES_KEY_AUDIO };

    protected SMSMessageDao(Context context) {
        super(context);
    }

    @Override
    public int insert(SMSMessage smsMessage) {
        ContentValues values = new ContentValues();
        values.put(NoMissingDB.SMSMESSAGES_KEY_FROM, smsMessage.getFrom());
        values.put(NoMissingDB.SMSMESSAGES_KEY_MESSAGE, smsMessage.getMessage());
        values.put(NoMissingDB.SMSMESSAGES_KEY_TIME, smsMessage.getTime());
        values.put(NoMissingDB.SMSMESSAGES_KEY_AUDIO, smsMessage.getAudio());

        open();
        int row = (int) db.insert(NoMissingDB.TABLE_SMSMESSAGES, null, values);
        close();

        return row;
    }

    @Override
    public int update(SMSMessage smsMessage) {
        String whereClause = NoMissingDB.SMSMESSAGES_KEY_ID + " = ?";
        String[] whereArgs = new String[] { String.valueOf(smsMessage.getId()) };

        ContentValues values = new ContentValues();
        values.put(NoMissingDB.SMSMESSAGES_KEY_FROM, smsMessage.getFrom());
        values.put(NoMissingDB.SMSMESSAGES_KEY_MESSAGE, smsMessage.getMessage());
        values.put(NoMissingDB.SMSMESSAGES_KEY_TIME, smsMessage.getTime());
        values.put(NoMissingDB.SMSMESSAGES_KEY_AUDIO, smsMessage.getAudio());

        open();
        int row = db.update(NoMissingDB.TABLE_SMSMESSAGES, values, whereClause, whereArgs);
        close();

        return row;
    }

    @Override
    public int delete(int id) {
        String whereClause = NoMissingDB.SMSMESSAGES_KEY_ID + " = ?";
        String[] whereArgs = new String[] { String.valueOf(id) };

        open();
        int row = db.delete(NoMissingDB.TABLE_SMSMESSAGES, whereClause, whereArgs);
        close();

        return row;
    }

    @Override
    public List<SMSMessage> findAll() {
        List<SMSMessage> smsMessages = new ArrayList<SMSMessage>();

        open();
        Cursor cursor = db.query(NoMissingDB.TABLE_SMSMESSAGES, SMSMESSAGES_COLUMNS, null, null, null, null, null);
        while (cursor.moveToNext()) {
            SMSMessage smsMessage = new SMSMessage(
                    cursor.getInt(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_FROM)),
                    cursor.getString(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_MESSAGE)),
                    cursor.getLong(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_TIME)),
                    cursor.getString(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_AUDIO)));
            smsMessages.add(smsMessage);
        }
        cursor.close();
        close();

        return smsMessages;
    }

    @Override
    public SMSMessage find(int id) {
        SMSMessage smsMessage = new SMSMessage();
        String selection = NoMissingDB.SMSMESSAGES_KEY_ID + " = ?";
        String[] selectionArgs = new String[] { String.valueOf(id) };

        open();
        Cursor cursor = db.query(NoMissingDB.TABLE_SMSMESSAGES, SMSMESSAGES_COLUMNS, selection, selectionArgs, null, null, null, null);
        if (cursor.moveToFirst()) {
            smsMessage.setId(cursor.getInt(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_ID)));
            smsMessage.setFrom(cursor.getString(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_FROM)));
            smsMessage.setMessage(cursor.getString(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_MESSAGE)));
            smsMessage.setTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_TIME)));
            smsMessage.setAudio(cursor.getString(cursor.getColumnIndex(NoMissingDB.SMSMESSAGES_KEY_AUDIO)));
        }
        cursor.close();
        close();

        return smsMessage;
    }

}
