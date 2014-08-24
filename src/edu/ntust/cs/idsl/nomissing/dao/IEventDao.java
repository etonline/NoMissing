package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

import edu.ntust.cs.idsl.nomissing.model.Event;

public interface IEventDao {
	
	public long insert(Event event);

	public long update(Event event);

	public long delete(long eventID);

	public List<Event> findAll();
	
	public List<Event> find(long calendarID, long startMillis, long endMillis);

	public Event find(long calendarID, long eventID, long startMillis, long endMillis);
	
}
