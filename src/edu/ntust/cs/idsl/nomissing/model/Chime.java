package edu.ntust.cs.idsl.nomissing.model;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressLint("SimpleDateFormat")
public class Chime {
	
	private int id;
	private int hour;
	private int minute;
	private boolean isEnabled;
	private boolean isRepeating;
	private boolean isTriggered;
	private String audio;
	private long createdAt;
	private long updatedAt;
	private Calendar calendarCreatedAt;
	private Calendar calendarUpdatedAt;

	public Chime() {}
	
	public Chime(int id, int hour, int minute, boolean isEnabled,
			boolean isRepeating, boolean isTriggered, String audio,
			long createdAt, long updatedAt) {
		super();
		this.id = id;
		this.hour = hour;
		this.minute = minute;
		this.isEnabled = isEnabled;
		this.isRepeating = isRepeating;
		this.isTriggered = isTriggered;
		this.audio = audio;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		
		updateCalendarCreatedAt();
		updateCalendarUpdatedAt();
	}

	private void updateCalendarCreatedAt() {
		if (calendarCreatedAt == null)
			calendarCreatedAt = new GregorianCalendar();

		calendarCreatedAt.setTimeInMillis(createdAt);
	}
	
	private void updateCalendarUpdatedAt() {
		if (calendarUpdatedAt == null)
			calendarUpdatedAt = new GregorianCalendar();

		calendarUpdatedAt.setTimeInMillis(updatedAt);		
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isRepeating() {
		return isRepeating;
	}

	public void setRepeating(boolean isRepeating) {
		this.isRepeating = isRepeating;
	}

	public boolean isTriggered() {
		return isTriggered;
	}

	public void setTriggered(boolean isTriggered) {
		this.isTriggered = isTriggered;
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
		updateCalendarCreatedAt();
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
		updateCalendarUpdatedAt();
	}
	
	public String getTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
		String timeString = simpleDateFormat.format(calendar.getTime());
		
		return timeString;
	}
	
	public String getRepeating() {
		return isRepeating ? "每天" : "一次";
	}
	
	public String getStringForTTS() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a h:mm");
		String timeString = simpleDateFormat.format(calendar.getTime());
		String tts = "現在時間：" + timeString;
		
		return tts;
	}

}
