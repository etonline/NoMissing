package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

import edu.ntust.cs.idsl.nomissing.model.Calendar;

public interface ICalendarDao {
	public List<Calendar> findAll();

	public List<Calendar> findByAccessLevel(int accessLevel);
}
