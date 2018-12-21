package com.nd.demo.tree.leaf;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nd.demo.common.controller.AbstractRestController;
import com.nd.demo.tree.node.NodeService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/leafs")
public class LeafRestController extends AbstractRestController<LeafEntity> {

	@Autowired
	private NodeService nodeService;

	public LeafRestController() {
		super(LeafEntity.class);
	}

	@ApiOperation(value = "Create new leaf")
	@PostMapping
	public ResponseEntity<Long> create(@Valid @RequestBody CreateLeafDTO createLeafDTO) {
		LeafEntity leaf = new LeafEntity();
		leaf.setName(createLeafDTO.getName());
		if (createLeafDTO.getParentId() != null) {
			leaf.setParent(nodeService.findOne(createLeafDTO.getParentId()));
		}
		LeafEntity newLeaf = super.save(leaf);

		return new ResponseEntity<>(newLeaf.getId(), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Get one leaf")
	@GetMapping("/{id}")
	public ResponseEntity<LeafEntity> getOne(@PathVariable(name = "id") Long id) {
		LeafEntity node = super.findOne(id);

		return ResponseEntity.ok(node);
	}

	@ApiOperation(value = "Get all leafs")
	@GetMapping
	public ResponseEntity<List<LeafEntity>> getAll() {
		List<LeafEntity> allLeafs = super.findAll();

		return ResponseEntity.ok(allLeafs);
	}

	@ApiOperation(value = "Rename a leaf")
	@PatchMapping("/rename/{id}")
	public ResponseEntity<Void> rename(@PathVariable(name = "id") Long id, @Valid @RequestBody String name) {
		LeafEntity leaf = super.findOne(id);
		if (!leaf.getName().equals(name)) {
			leaf.setName(name);
			super.save(leaf);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Change parent id of a leaf")
	@PatchMapping("/change-parent-id/{id}")
	public ResponseEntity<Void> changeNodeId(@PathVariable(name = "id") Long id,
			@Valid @RequestBody(required = false) Long nodeId) {
		LeafEntity leaf = super.findOne(id);
		if (nodeId != null) {
			if (leaf.getParent() == null || !nodeId.equals(leaf.getParent().getId())) {
				leaf.setParent(nodeService.findOne(nodeId));
				super.save(leaf);
			}
		} else {
			if (leaf.getParent() != null) {
				leaf.setParent(null);
				super.save(leaf);
			}
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Delete a leaf")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
		super.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
