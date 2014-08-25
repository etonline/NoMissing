package edu.ntust.cs.idsl.nomissing.model;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class Reminder {
	private long id;
	private long calendarID;
	private long eventID;
	private long reminderTime;
	private String audio;
	private long createdAt;
	private long updatedAt;

	public Reminder() {
	}

	public Reminder(long id, long calendarID, long eventID, long reminderTime,
			String audio, long createdAt, long updatedAt) {
		super();
		this.id = id;
		this.calendarID = calendarID;
		this.eventID = eventID;
		this.reminderTime = reminderTime;
		this.audio = audio;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCalendarID() {
		return calendarID;
	}

	public void setCalendarID(long calendarID) {
		this.calendarID = calendarID;
	}

	public long getEventID() {
		return eventID;
	}

	public void setEventID(long eventID) {
		this.eventID = eventID;
	}

	public long getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(long reminderTime) {
		this.reminderTime = reminderTime;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getStringForTTS(Event event) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getReminderTime());
		SimpleDateFormat formatter = new SimpleDateFormat("a h:mm");
		
		StringBuilder reminder = new StringBuilder();
		reminder.append("現在時間：" + formatter.format(calendar.getTime()) + "。");
		reminder.append(event.getTitle() + "將於" + formatter.format(event.getStartTime()) + "開始，");
		reminder.append("於" + formatter.format(event.getEndTime()) + "結束。");
		
		if (!event.getLocation().isEmpty())
			reminder.append("在" + event.getLocation());
		
		return reminder.toString();
	}
	
}
