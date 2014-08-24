package edu.ntust.cs.idsl.nomissing.constant;

import java.util.ArrayList;
import java.util.List;

import edu.ntust.cs.idsl.nomissing.R;

public enum EventReminder {
	
	ZERO_MINUTE(0, R.string.event_reminder_zero_minute, 0),
	ONE_MINUTE(1, R.string.event_reminder_one_minute, 1),
	FIVE_MINUTES(2, R.string.event_reminder_five_minutes, 5),
	TEN_MINUTES(3, R.string.event_reminder_ten_minutes, 10),
	FIFTEEN_MINUTES(4, R.string.event_reminder_fifteen_minutes, 15),
	TWENTY_MINUTES(5, R.string.event_reminder_twenty_minutes, 20),
	TWENTY_FIVE_MINUTES(6, R.string.event_reminder_twenty_five_minutes, 25),
	THIRTY_MINUTES(7, R.string.event_reminder_thirty_minutes, 30),
	FORTY_FIVE_MINUTES(8, R.string.event_reminder_forty_five_minutes, 45),
	ONE_HOUR(9, R.string.event_reminder_one_hour, 60),
	TWO_HOURS(10, R.string.event_reminder_two_hours, 120),
	THREE_HOURS(11, R.string.event_reminder_three_hours, 180),
	HALF_DAY(12, R.string.event_reminder_half_day, 720),
	ONE_DAY(13, R.string.event_reminder_one_day, 1440),
	TWO_DAY(14, R.string.event_reminder_two_days, 2880),
	ONE_WEEK(15, R.string.event_reminder_one_week, 10080)	;

	private int id;
	private int name;
	private int minutes;
	
	private EventReminder(int id, int name, int minutes) {
		this.id = id;
		this.name = name;
		this.minutes = minutes;
	}

	public int getId() {
		return id;
	}
	
	public int getName() {
		return name;
	}

	public int getMinutes() {
		return minutes;
	}
	
	public static List<EventReminder> getEventReminders() {
		List<EventReminder> eventReminders = new ArrayList<EventReminder>();
		for (EventReminder eventReminder : EventReminder.values()) {
			eventReminders.add(eventReminder);
		}
		return eventReminders;
	}
	
	public static int getIdByMinutes(int minutes) {
		int id = 0;
		for (EventReminder eventReminder : EventReminder.values()) {
			if (minutes == eventReminder.getMinutes()) {
				id = eventReminder.getId();
				break;
			}
		}
		return id;		
	}
	
}
