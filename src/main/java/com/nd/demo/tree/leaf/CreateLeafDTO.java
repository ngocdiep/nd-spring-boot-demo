package com.nd.demo.tree.leaf;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class CreateLeafDTO implements Serializable {

	private static final long serialVersionUID = 162337463355937654L;

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
