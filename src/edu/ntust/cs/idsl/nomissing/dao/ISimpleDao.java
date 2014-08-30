package edu.ntust.cs.idsl.nomissing.dao;

import java.util.List;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public interface ISimpleDao<T> {

    public int insert(T entity);

    public int update(T entity);

    public int delete(int id);

    public List<T> findAll();

    public T find(int id);

}
