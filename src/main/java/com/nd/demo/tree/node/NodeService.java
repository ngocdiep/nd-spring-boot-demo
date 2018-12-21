package com.nd.demo.tree.node;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nd.demo.common.service.DatabaseEntityService;

@Service
@Transactional
public class NodeService extends DatabaseEntityService<NodeEntity> {

	@Autowired
	public NodeService(NodeRepository nodeRepository, ObjectMapper mapper) {
		super(NodeEntity.class, nodeRepository, mapper);
	}

	public List<NodeEntity> findAllByOrderByIdAsc() {
		return ((NodeRepository) super.repository).findAllByOrderByIdAsc();
	}
	
	public List<NodeEntity> getSubNodesByParentId(long id) {
		return ((NodeRepository) super.repository).getSubNodesByParentId(id);
	}
}
