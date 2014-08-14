package edu.ntust.cs.idsl.nomissing.model;

import android.annotation.SuppressLint;

public enum EventFreq {
	SINGLE(0, ""), 
	DAILY(1, "FREQ=DAILY"), 
	WEEKLY(2, "FREQ=WEEKLY"), 
	MONTHLY(3, "FREQ=MONTHLY"), 
	YEARLY(4, "FREQ=YEARLY");

	private int id;
	private String rrule;

	private EventFreq(int id, String rrule) {
		this.id = id;
		this.rrule = rrule;
	}

	public int getId() {
		return id;
	}

	public String getRrule() {
		return rrule;
	}
	
	@SuppressLint("NewApi")
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
