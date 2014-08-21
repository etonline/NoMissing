package edu.ntust.cs.idsl.nomissing.model;

public class Event {
	private long eventID;
	private long calendarID;
	private String title;
	private String location;
	private String description;
	private long startTime;
	private long endTime;
	private boolean allDay;
	private String rrule;
	private int reminder;

	public Event() {
	}

	public Event(long eventID, long calendarID, String title, String location,
			String description, long startTime, long endTime, boolean allDay,
			String rrule, int reminder) {
		super();
		this.eventID = eventID;
		this.calendarID = calendarID;
		this.title = title;
		this.location = location;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.allDay = allDay;
		this.rrule = rrule;
		this.reminder = reminder;
	}

	public long getEventID() {
		return eventID;
	}

	public void setEventID(long eventID) {
		this.eventID = eventID;
	}

	public long getCalendarID() {
		return calendarID;
	}

	public void setCalendarID(long calendarID) {
		this.calendarID = calendarID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public String getRrule() {
		return rrule;
	}

	public void setRrule(String rrule) {
		this.rrule = rrule;
	}

	public int getReminder() {
		return reminder;
	}

	public void setReminder(int reminder) {
		this.reminder = reminder;
	}

}
