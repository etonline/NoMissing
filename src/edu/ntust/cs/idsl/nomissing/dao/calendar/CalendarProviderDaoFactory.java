package edu.ntust.cs.idsl.nomissing.dao.calendar;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.IEventDao;
import android.content.Context;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class CalendarProviderDaoFactory extends DaoFactory {
	
	@Override
	public IEventDao createEventDao(Context context) {
		return new EventDao(context);
	}	
	
	public CalendarDao createCalendarDao(Context context) {
		return new CalendarDao(context);
	}

	public EventDao creatEventDao(Context context) {
		return new EventDao(context);
	}

}
