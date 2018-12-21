package com.nd.demo.tree.node;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.nd.demo.common.domain.AbstractDatabaseEntity;
import com.nd.demo.utils.tree.INode;
import com.nd.demo.utils.tree.LTreeType;

@TypeDefs({ @TypeDef(name = "ltree", typeClass = LTreeType.class), })
@Entity
@Table(name = "node")
public class NodeEntity extends AbstractDatabaseEntity implements INode<NodeEntity>, Serializable {

	private static final long serialVersionUID = 60489512800417147L;

	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public NodeEntity parent;

	@Column(name = "parent_path", columnDefinition = "ltree", insertable = false)
	@Type(type = "ltree")
	private String parentPath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeEntity getParent() {
		return parent;
	}

	public void setParent(NodeEntity parent) {
		this.parent = parent;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
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
		NodeEntity other = (NodeEntity) obj;
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
