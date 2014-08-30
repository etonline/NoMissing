package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import android.content.Context;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.IEventDao;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class SQLiteDaoFactory extends DaoFactory {

    @Override
    public IEventDao createEventDao(Context context) {
        return new EventDao(context);
    }

    public WeatherDao createWeatherDao(Context context) {
        return new WeatherDao(context);
    }

    public ChimeDao createChimeDao(Context context) {
        return new ChimeDao(context);
    }

    public ReminderDao createReminderDao(Context context) {
        return new ReminderDao(context);
    }

}
