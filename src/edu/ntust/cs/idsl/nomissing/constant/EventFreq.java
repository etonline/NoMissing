package edu.ntust.cs.idsl.nomissing.constant;

import java.util.ArrayList;
import java.util.List;

import edu.ntust.cs.idsl.nomissing.R;

public enum EventFreq {
	SINGLE(0, R.string.event_frequency_single, ""), 
	DAILY(1, R.string.event_frequency_daily, "FREQ=DAILY"), 
	WEEKLY(2, R.string.event_frequency_weekly, "FREQ=WEEKLY"), 
	MONTHLY(3, R.string.event_frequency_monthly, "FREQ=MONTHLY"), 
	YEARLY(4, R.string.event_frequency_yearly, "FREQ=YEARLY");

	private int id;
	private int name;
	private String rrule;

	private EventFreq(int id, int name, String rrule) {
		this.id = id;
		this.name = name;
		this.rrule = rrule;
	}

	public int getId() {
		return id;
	}
	
	public int getName() {
		return name;
	}

	public String getRrule() {
		return rrule;
	}
	
	public static List<EventFreq> getEventFreqs() {
		List<EventFreq> eventFreqs = new ArrayList<EventFreq>();
		for (EventFreq eventFreq : EventFreq.values()) {
			eventFreqs.add(eventFreq);
		}
		return eventFreqs;
	}	
	
	public static int getIdByRrule(String rrule) {
		int id = 0;
		if (rrule != null) {
			for (EventFreq freq : EventFreq.values()) {
				if (freq.id == 0) continue;
				if (rrule.contains(freq.getRrule())) {
					id = freq.getId();
					break;
				}
			}
		}
		return id;
	}
}
