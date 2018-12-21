package com.nd.demo.tree.node;

import java.io.Serializable;
import java.util.List;

public class NodeTreeDTO implements Serializable {

	private static final long serialVersionUID = 2386491980532019986L;

	private Long id;
	private Long parentId;
	private String name;
	private List<NodeTreeDTO> children;
	private boolean isLeaf;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<NodeTreeDTO> getChildren() {
		return children;
	}

	public void setChildren(List<NodeTreeDTO> children) {
		this.children = children;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

}
