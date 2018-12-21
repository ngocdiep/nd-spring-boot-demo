package com.nd.demo.tree.leaf;

import java.util.List;

import com.nd.demo.common.repository.BaseRepository;

public interface LeafRepository extends BaseRepository<LeafEntity> {
	List<LeafEntity> findByParentIdIn(List<Long> ids);
}
