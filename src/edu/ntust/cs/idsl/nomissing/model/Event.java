package edu.ntust.cs.idsl.nomissing.model;

public class Event {
	private long eventID;
	private String title;
	private String location;
	private String description;
	private long start;
	private long end;
	private String timezone;
	private long calendarID;
	
	public Event() {}
	
	public Event(long eventID, String title, String description, long start,
			long end, String timezone, long calendarID) {
		super();
		this.eventID = eventID;
		this.title = title;
		this.description = description;
		this.start = start;
		this.end = end;
		this.timezone = timezone;
		this.calendarID = calendarID;
	}

	public long getEventID() {
		return eventID;
	}

	public void setEventID(long eventID) {
		this.eventID = eventID;
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

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public long getCalendarID() {
		return calendarID;
	}

	public void setCalendarID(long calendarID) {
		this.calendarID = calendarID;
	}
	
	
}
