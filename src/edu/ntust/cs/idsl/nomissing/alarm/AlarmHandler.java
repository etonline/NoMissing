package edu.ntust.cs.idsl.nomissing.alarm;

import java.util.Calendar;

import android.app.PendingIntent;
import android.content.Context;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public abstract class AlarmHandler<T> {

    protected static final long ONE_DAY_MILLIS = 86400000;
    protected Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }

    public abstract void setAlarm(T entity);

    public abstract void cancelAlarm(T entity);

    protected abstract PendingIntent getPendingIntent(T entity);

    protected static Calendar calculateAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);

        boolean isAlarmBehindCurrentTime = (hour < nowHour) || (hour == nowHour && minute <= nowMinute);
        if (isAlarmBehindCurrentTime)
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
