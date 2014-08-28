package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

import edu.ntust.cs.idsl.nomissing.model.Calendar;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public interface ICalendarDao {
	
	public List<Calendar> findAll();

	public List<Calendar> findByAccessLevel(int accessLevel);
	
}
