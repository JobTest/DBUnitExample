package com.example.dbunit.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class JpaDAO<T extends Serializable> implements IJpaDAO<T> {

	private static final Logger LOG = LoggerFactory.getLogger(JpaDAO.class);

	private Class<T> clazz;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void delete(final T entity) {
		this.entityManager.remove(entity);
	}

	@Override
	public List<T> findAll() {
		return this.entityManager.createQuery("from " + this.clazz.getName()).getResultList();
	}

	@Override
	public T findById(final int id) {
		return this.entityManager.find(this.clazz, id);
	}

	@Override
	public void save(final T entity) {
		LOG.debug("--- SAVE ACTION IN DAO ---");
		entityManager.persist(entity);
		LOG.debug("--- END ---");
	}

	@Override
	public void setClazz(final Class<T> clazzToSet) {
		this.clazz = clazzToSet;
	}

	@Override
	public void update(final T entity) {
		this.entityManager.merge(entity);
	}
}