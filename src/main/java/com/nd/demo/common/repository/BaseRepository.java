package com.nd.demo.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nd.demo.common.domain.AbstractDatabaseEntity;

public interface BaseRepository<E extends AbstractDatabaseEntity>
		extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {

}
