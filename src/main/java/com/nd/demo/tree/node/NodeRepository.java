package com.nd.demo.tree.node;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nd.demo.common.repository.BaseRepository;

public interface NodeRepository extends BaseRepository<NodeEntity> {
	List<NodeEntity> findAllByOrderByIdAsc();

	@Query(nativeQuery = true, value = "select n.* from node n where n.parent_path <@ cast((select parent_path from node where parent_id = :id limit 1) as ltree)")
	List<NodeEntity> getSubNodesByParentId(@Param("id") long id);
}
