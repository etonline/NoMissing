package edu.ntust.cs.idsl.nomissing.util;

import java.util.Calendar;

public final class CalendarHelper {
    
    public static Calendar setCurrentTime(Calendar calendar) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar;
    }
  
    public static Calendar setStartOfDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Calendar setEndOfDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }    
    
    public static Calendar setPrevMonth(Calendar calendar) {
        calendar.add(Calendar.MONTH, -1);
        return calendar;
    }
    
    public static Calendar setNextMonth(Calendar calendar) {
        calendar.add(Calendar.MONTH, 1);
        return calendar;       
    }
    
    public static Calendar setStartOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar = setStartOfDate(calendar);
        return calendar;  
    }

    public static Calendar setEndOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar = setEndOfDate(calendar);
        return calendar;  
    }
    
}
