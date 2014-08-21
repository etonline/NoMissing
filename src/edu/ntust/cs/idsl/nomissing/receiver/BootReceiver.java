package edu.ntust.cs.idsl.nomissing.receiver;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDao;
import edu.ntust.cs.idsl.nomissing.dao.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.pref.SettingsManager;
import edu.ntust.cs.idsl.nomissing.util.AlarmUtil;

public class BootReceiver extends BroadcastReceiver {
	
	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		setChimeAlarms(context);
		setWeatherAlarm(context);
	}
	
	private void setChimeAlarms(Context context) {
	    List<Chime> chimes = SQLiteDaoFactory.getChimeDao(context).findAll();
		for(Chime chime : chimes) {
			AlarmUtil.cancelChimeAlarm(context, chime);
			if (chime.isEnabled() && !chime.isTriggered())
				AlarmUtil.setChimeAlarm(context, chime);
		}		
	}
	
	private void setWeatherAlarm(Context context) {
		AlarmUtil.cancelWeatherAlarm(context);
		AlarmUtil.setWeatherAlarm(context);
	}
	
}
