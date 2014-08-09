package edu.ntust.cs.idsl.nomissing.model;

public class Event {
	private long eventID;
	private long calendarID;
	private String title;
	private String location;
	private String description;
	private long start;
	private long end;
	
	public Event() {}

	public Event(long eventID, long calendarID, String title, String location,
			String description, long start, long end) {
		this.eventID = eventID;
		this.calendarID = calendarID;
		this.title = title;
		this.location = location;
		this.description = description;
		this.start = start;
		this.end = end;
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

}
