package com.example.dbunit.dao;

import java.util.List;

public interface IJpaDAO<T> {
	public void delete(final T entity);

	public List<T> findAll();

	public T findById(final int id);

	public void save(final T entity);

	public void setClazz(final Class<T> clazzToSet);

	public void update(final T entity);
}
