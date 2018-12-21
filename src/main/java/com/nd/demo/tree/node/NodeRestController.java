package com.nd.demo.tree.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.nd.demo.tree.leaf.LeafEntity;
import com.nd.demo.tree.leaf.LeafRepository;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/nodes")
@RestController
public class NodeRestController extends AbstractRestController<NodeEntity> {

	@Autowired
	private NodeService nodeService;
	@Autowired
	private LeafRepository leafRepository;

	public NodeRestController() {
		super(NodeEntity.class);
	}

	@ApiOperation(value = "Create new node")
	@PostMapping
	public ResponseEntity<Long> create(@Valid @RequestBody CreateNodeDTO createNodeDTO) {
		NodeEntity node = new NodeEntity();
		node.setName(createNodeDTO.getName());
		if (createNodeDTO.getParentId() != null) {
			node.setParent(super.findOne(createNodeDTO.getParentId()));
		}
		NodeEntity newNode = super.save(node);

		return new ResponseEntity<>(newNode.getId(), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Get one node")
	@GetMapping("/{id}")
	public ResponseEntity<NodeEntity> getOne(@PathVariable(name = "id") Long id) {
		NodeEntity node = super.findOne(id);

		return ResponseEntity.ok(node);
	}
	
	@ApiOperation(value = "Get all nodes")
	@GetMapping
	public ResponseEntity<List<NodeEntity>> getAll() {
		List<NodeEntity> allNodes = super.findAll();
		
		return ResponseEntity.ok(allNodes);
	}

	@ApiOperation(value = "Rename a node")
	@PatchMapping("/rename/{id}")
	public ResponseEntity<Void> rename(@PathVariable(name = "id") Long id, @Valid @RequestBody String name) {
		NodeEntity node = super.findOne(id);
		if (!node.getName().equals(name)) {
			node.setName(name);
			super.save(node);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Change parent id of a node")
	@PatchMapping("/change-parent-id/{id}")
	public ResponseEntity<Void> changeParentId(@PathVariable(name = "id") Long id,
			@Valid @RequestBody(required = false) Long parentId) {
		NodeEntity node = super.findOne(id);
		if (parentId != null) {
			if (node.getParent() == null || !parentId.equals(node.getParent().getId())) {
				node.setParent(super.findOne(parentId));
				super.save(node);
			}
		} else {
			if (node.getParent() != null) {
				node.setParent(null);
				super.save(node);
			}
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Delete a node")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
		super.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Get the tree that represent for all nodes")
	@GetMapping(value = "/tree")
	public ResponseEntity<NodeTreeDTO> getNodeTree() {
		List<NodeEntity> allNodes = nodeService.findAllByOrderByIdAsc();
		List<LeafEntity> allLeafs = leafRepository.findAll();

		Map<Long, List<NodeTreeDTO>> leafMap = new HashMap<>();
		buildLeafMap(allLeafs, leafMap);

		NodeTreeDTO root = new NodeTreeDTO();
		root.setName("root");
		root.setChildren(new ArrayList<>());
		Map<Long, NodeTreeDTO> nodeTreeDTOMap = buildNodeTreeDTOMap(root, allNodes, leafMap);

		addChildrensForNodeTreeDTO(nodeTreeDTOMap);

		return ResponseEntity.ok(root);
	}

	private void buildLeafMap(List<LeafEntity> allLeafs, Map<Long, List<NodeTreeDTO>> leafMap) {
		for (LeafEntity leaf : allLeafs) {
			NodeTreeDTO leafNodeTreeDTO = new NodeTreeDTO();
			leafNodeTreeDTO.setId(leaf.getId());
			if (leaf.getParent() != null) {
				Long nodeId = leaf.getParent().getId();
				leafNodeTreeDTO.setParentId(nodeId);

				if (leafMap.containsKey(nodeId)) {
					leafMap.get(nodeId).add(leafNodeTreeDTO);
				} else {
					ArrayList<NodeTreeDTO> leafs = new ArrayList<>();
					leafs.add(leafNodeTreeDTO);
					leafMap.put(nodeId, leafs);
				}
			}
			leafNodeTreeDTO.setName(leaf.getName());
			leafNodeTreeDTO.setIsLeaf(true);
		}
	}

	private Map<Long, NodeTreeDTO> buildNodeTreeDTOMap(NodeTreeDTO root, List<NodeEntity> allNodes,
			Map<Long, List<NodeTreeDTO>> leafMap) {
		Map<Long, NodeTreeDTO> nodeTreeDTOMap = new HashMap<>();

		for (int i = 0; i < allNodes.size(); i++) {
			NodeEntity node = allNodes.get(i);
			NodeTreeDTO nodeTreeDTO = new NodeTreeDTO();
			nodeTreeDTO.setName(node.getName());
			nodeTreeDTO.setId(node.getId());
			if (node.getParent() != null) {
				nodeTreeDTO.setParentId(node.getParent().getId());
			} else {
				root.getChildren().add(nodeTreeDTO);
			}
			nodeTreeDTO.setChildren(new ArrayList<>());
			nodeTreeDTOMap.put(nodeTreeDTO.getId(), nodeTreeDTO);
			if (leafMap.containsKey(node.getId())) {
				nodeTreeDTO.getChildren().addAll(leafMap.get(node.getId()));
			}
		}
		return nodeTreeDTOMap;
	}

	private void addChildrensForNodeTreeDTO(Map<Long, NodeTreeDTO> nodeTreeDTOMap) {
		for (Entry<Long, NodeTreeDTO> entry : nodeTreeDTOMap.entrySet()) {
			NodeTreeDTO value = entry.getValue();
			if (nodeTreeDTOMap.containsKey(value.getParentId())) {
				nodeTreeDTOMap.get(value.getParentId()).getChildren().add(value);
			}
		}
	}
}
