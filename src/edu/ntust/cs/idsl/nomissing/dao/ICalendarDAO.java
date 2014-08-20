package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

public interface ICalendarDAO<T> {
	
	public long insert(T entity);

	public long update(T entity);

	public long delete(long id);

	public List<T> find(long calendarID, long startMillis, long endMillis);

	public T find(long calendarID, long id, long startMillis, long endMillis);
	
}
