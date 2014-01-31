package com.plainvanilla.organix.rest.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.ResourceSupport;

import com.plainvanilla.organix.engine.model.Configuration;
import com.plainvanilla.organix.engine.model.ConnectionType;
import com.plainvanilla.organix.engine.model.ObjectType;

public class ConfigurationRep extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 2L;

	private Long dbId;
	private Date configurationDate;
	private Date lastUpdateDate;
	private String name;
	private Integer version;
	private Set<ConnectionTypeRep> connectionTypes = new HashSet<ConnectionTypeRep>();	
	private Set<ObjectTypeRep> objectTypes = new HashSet<ObjectTypeRep>();
	
	
	public ConfigurationRep() {
		
	}
	
	public ConfigurationRep(Configuration config) {
		fromConfiguration(config);
	}
	
	public void fromConfiguration(Configuration config) {
		dbId = config.getId();
		configurationDate = config.getConfigurationDate();
		lastUpdateDate = config.getLastUpdateDate();
		name = config.getName();
		version = config.getVersion();

		for (ConnectionType type : config.getConnectionTypes()) {
			connectionTypes.add(new ConnectionTypeRep(type));
		}
		
		for (ObjectType type : config.getObjectTypes()) {
			objectTypes.add(new ObjectTypeRep(type));
		}
	}
	
	public Configuration toConfiguration() {
		
		Configuration c = new Configuration();
		
		c.setConfigurationDate(configurationDate);
		c.setName(name);
		c.setVersion(version);
		c.setId(dbId);
		
		for (ConnectionTypeRep r : connectionTypes) {
			c.addConnectionType(r.toConnectionType());
		}
		
		for (ObjectTypeRep r : objectTypes) {
			c.addObjectType(r.toObjectType());
		}
		
		return c;
		
	}
	
	public Long getDbId() {
		return dbId;
	}
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	public Date getConfigurationDate() {
		return configurationDate;
	}
	public void setConfigurationDate(Date configurationDate) {
		this.configurationDate = configurationDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Set<ConnectionTypeRep> getConnectionTypes() {
		return connectionTypes;
	}
	public void setConnectionTypes(Set<ConnectionTypeRep> connectionTypes) {
		this.connectionTypes = connectionTypes;
	}
	public Set<ObjectTypeRep> getObjectTypes() {
		return objectTypes;
	}
	public void setObjectTypes(Set<ObjectTypeRep> objectTypes) {
		this.objectTypes = objectTypes;
	}
	
}
