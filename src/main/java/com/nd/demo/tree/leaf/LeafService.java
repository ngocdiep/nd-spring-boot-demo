package com.nd.demo.tree.leaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nd.demo.common.service.DatabaseEntityService;

@Service
public class LeafService extends DatabaseEntityService<LeafEntity> {

	@Autowired
	public LeafService(LeafRepository repo, ObjectMapper mapper) {
		super(LeafEntity.class, repo, mapper);
	}

}