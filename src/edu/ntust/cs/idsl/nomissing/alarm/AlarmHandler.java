package edu.ntust.cs.idsl.nomissing.alarm;

import java.util.Calendar;

import android.app.PendingIntent;
import android.content.Context;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public abstract class AlarmHandler<T> {
	protected Context context;

	public AlarmHandler(Context context) {
		this.context = context;
	}

	public abstract void setAlarm(T entity);

	public abstract void cancelAlarm(T entity);
	
	protected abstract PendingIntent getPendingIntent(String action, T entity);
	
	protected static String calculateDiffTime(long time) {
		long diff = time - System.currentTimeMillis();
		
		long day = diff / (24 * 60 * 60 * 1000);
		long hour = (diff / (60 * 60 * 1000) - day * 24);
		long minute = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long second = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		
		String diffTime = "已將語音報時設定在" + hour + "小時又" + minute + "分鐘後啟動";
		return diffTime;
	}
	
	protected static Calendar calculateAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        
        // if alarm is behind current time, advance one day
        if (hour < nowHour  ||
            hour == nowHour && minute <= nowMinute) {
        	calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);		

        return calendar;
	}
}
