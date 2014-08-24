package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

public interface ISimpleDao<T> {
	public int insert(T entity);

	public int update(T entity);

	public int delete(int id);

	public List<T> findAll();

	public T find(int id);
}
