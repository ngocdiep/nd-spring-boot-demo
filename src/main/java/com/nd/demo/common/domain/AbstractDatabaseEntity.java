package com.nd.demo.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class AbstractDatabaseEntity implements IDataBaseEntity {
	@Id
	@GeneratedValue
	private Long id;

	@Column(updatable = false)
	private Long timeStampCreated;

	@Column
	private Long timeStampUpdated;

	@Version
	private long version;

	@PrePersist
	protected void onCreate() {
		this.timeStampUpdated = this.timeStampCreated = new Date().getTime();
	}

	@PreUpdate
	protected void onUpdate() {
		this.timeStampUpdated = new Date().getTime();
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(final long version) {
		this.version = version;
	}

	public Long getTimeStampCreated() {
		return this.timeStampCreated;
	}

	@JsonIgnore
	public Long getTimeStampUpdated() {
		return this.timeStampUpdated;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractDatabaseEntity other = (AbstractDatabaseEntity) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
