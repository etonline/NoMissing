package edu.ntust.cs.idsl.nomissing.receiver;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.util.AlarmUtil;

public class BootReceiver extends BroadcastReceiver {
	
	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		ChimeDAO chimeDAO = ChimeDAO.getInstance(context);
	    List<Chime> chimes = chimeDAO.findAll();
		
		for(Chime chime : chimes) {
			AlarmUtil.cancelChimeAlarm(context, chime);
			AlarmUtil.setChimeAlarm(context, chime);
		}
		
		
//		Intent mBootintent = new Intent(context, ChimeAlarmInitService.class);
//		mBootintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		context.startService(mBootintent);
	}
	
}
