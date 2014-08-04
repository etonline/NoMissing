package edu.ntust.cs.idsl.nomissing.service;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.util.AlarmUtil;

public class ChimeAlarmInitService extends IntentService {

	private ChimeDAO chimeDAO;
	private List<Chime> chimes;
	
	public ChimeAlarmInitService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		chimeDAO = ChimeDAO.getInstance(this);
		chimes = chimeDAO.findAll();
		
		for(Chime chime : chimes) {
			AlarmUtil.cancelChimeAlarm(this, chime);
			AlarmUtil.setChimeAlarm(this, chime);
		}
		
	}

}
