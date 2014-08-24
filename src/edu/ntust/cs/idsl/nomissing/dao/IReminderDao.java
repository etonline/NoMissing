package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

import edu.ntust.cs.idsl.nomissing.model.Reminder;

public interface IReminderDao {
	
	public List<Reminder> findAll();
	
	public Reminder find(long id);
	
	public Reminder find(long calendarID, long eventID);

	public long insert(Reminder reminder);

	public long update(Reminder reminder);

	public long delete(long id);
	
}
