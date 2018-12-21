package com.nd.demo.tree.node;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class CreateNodeDTO implements Serializable {

	private static final long serialVersionUID = -4768041104475720301L;

	@NotEmpty
	private String name;
	@Min(value = 1)
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
