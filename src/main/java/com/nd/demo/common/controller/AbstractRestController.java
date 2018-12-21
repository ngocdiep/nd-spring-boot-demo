package com.nd.demo.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nd.demo.common.domain.AbstractDatabaseEntity;
import com.nd.demo.common.service.DatabaseEntityService;

public class AbstractRestController<E extends AbstractDatabaseEntity> {
	
	@Autowired
	private DatabaseEntityService<E> databaseEntityService;
	protected Class<E> domainClazz;

	public AbstractRestController(final Class<E> domainClazz) {
		this.domainClazz = domainClazz;
	}

	public E save(E entity) {
		return databaseEntityService.save(entity);
	}

	public E findOne(final long id) {
		return databaseEntityService.findOne(id);
	}

	public List<E> findAll() {
		return databaseEntityService.findAll();
	}
	
	public void delete(long id) {
		databaseEntityService.delete(id);
	}
}
