package com.nd.demo.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nd.demo.common.domain.AbstractDatabaseEntity;
import com.nd.demo.common.exception.EntityNotFoundException;
import com.nd.demo.common.repository.BaseRepository;

public abstract class DatabaseEntityService<E extends AbstractDatabaseEntity> {

	protected final Class<E> clazz;
	protected BaseRepository<E> repository;
	protected ObjectMapper mapper;

	@Autowired
	public DatabaseEntityService(final Class<E> clazz, final BaseRepository<E> repo, ObjectMapper mapper) {
		this.clazz = clazz;
		this.repository = repo;
		this.mapper = mapper;
	}

	public E save(final E entity) {
		return this.repository.save(entity);
	}

	public E findOne(final Long id) {

		Optional<E> foundEntity = this.repository.findById(id);

		if (!foundEntity.isPresent()) {
			throw new EntityNotFoundException(clazz, id);
		}
		return foundEntity.get();
	}

	public void delete(final Long id) {
		this.repository.deleteById(id);
	}

	public List<E> findAll() {
		return this.repository.findAll();
	}

	public Page<E> findAll(final Pageable pageable) {
		return this.repository.findAll(pageable);
	}

}
