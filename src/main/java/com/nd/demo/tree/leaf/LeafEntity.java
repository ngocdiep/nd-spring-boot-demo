package com.nd.demo.tree.leaf;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nd.demo.common.domain.AbstractDatabaseEntity;
import com.nd.demo.tree.node.NodeEntity;

@Entity
@Table(name = "leaf")
public class LeafEntity extends AbstractDatabaseEntity implements Serializable {

	private static final long serialVersionUID = -167029824229225608L;

	@ManyToOne
	@JoinColumn(name = "node_id")
	private NodeEntity parent;

	private String name;

	public NodeEntity getParent() {
		return parent;
	}

	public void setParent(NodeEntity node) {
		this.parent = node;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LeafEntity other = (LeafEntity) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}

}
