package com.nd.demo.tree.node;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class UpdateNodeDTO implements Serializable {
	private static final long serialVersionUID = -1354704369720980373L;

	@NotEmpty
	private String name;
	@Min(1)
	private Long parentId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
