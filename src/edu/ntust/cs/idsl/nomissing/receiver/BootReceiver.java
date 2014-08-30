package edu.ntust.cs.idsl.nomissing.receiver;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.alarm.AlarmHandlerFactory;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.preference.SettingsManager;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class BootReceiver extends BroadcastReceiver {

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        setChimeAlarms(context);
        setWeatherAlarm(context);
    }

    private void setChimeAlarms(Context context) {
        List<Chime> chimes = DaoFactory.getSQLiteDaoFactory().createChimeDao(context).findAll();
        for (Chime chime : chimes) {
            AlarmHandlerFactory.createChimeAlarmHandler(context).cancelAlarm(chime);
            if (chime.isEnabled() && !chime.isTriggered())
                AlarmHandlerFactory.createChimeAlarmHandler(context).setAlarm(chime);
        }
    }

    private void setWeatherAlarm(Context context) {
        SettingsManager settingsManager = SettingsManager.getInstance(context);
        int cityID = settingsManager.getWeatherReminderCity();
        Weather weather = DaoFactory.getSQLiteDaoFactory().createWeatherDao(context).find(cityID);

        AlarmHandlerFactory.createWeatherAlarmHandler(context).cancelAlarm(weather);
        AlarmHandlerFactory.createWeatherAlarmHandler(context).setAlarm(weather);
    }

}
