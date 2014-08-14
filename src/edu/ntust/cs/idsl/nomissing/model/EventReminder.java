package edu.ntust.cs.idsl.nomissing.model;

public enum EventReminder {
	
	ZERO_MINUTE(0, 0),
	ONE_MINUTE(1, 1),
	FIVE_MINUTES(2, 5),
	TEN_MINUTES(3, 10),
	FIFTEEN_MINUTES(4, 15),
	TWENTY_MINUTES(5, 20),
	TWENTY_FIVE_MINUTES(6, 25),
	THIRTY_MINUTES(7, 30),
	FORTY_FIVE_MINUTES(8, 45),
	ONE_HOUR(9, 60),
	TWO_HOURS(10, 120),
	THREE_HOURS(11, 180),
	HALF_DAY(12, 720),
	ONE_DAY(13, 1440),
	TWO_DAY(14, 2880),
	ONE_WEEK(15, 10080)	;

	private int id;
	private int minutes;
	
	private EventReminder(int id, int minutes) {
		this.id = id;
		this.minutes = minutes;
	}

	public int getId() {
		return id;
	}

	public int getMinutes() {
		return minutes;
	}
	
}
