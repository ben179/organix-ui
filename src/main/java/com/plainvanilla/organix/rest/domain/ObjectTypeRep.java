package com.plainvanilla.organix.rest.domain;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import com.plainvanilla.organix.engine.model.ObjectType;

public class ObjectTypeRep extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long dbId;
	private Integer typeNumber;
	private String name;
	private Long configId;

	public ObjectTypeRep() {
		
	}
		
	public ObjectTypeRep(ObjectType type) {
		fromObjectType(type);
	}
	
	
	public Long getDbId() {
		return dbId;
	}
	public void setId(Long dbId) {
		this.dbId = dbId;
	}
	public Integer getTypeNumber() {
		return typeNumber;
	}
	public void setTypeNumber(Integer typeNumber) {
		this.typeNumber = typeNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void fromObjectType(ObjectType type) {
		dbId = type.getId();
		name = type.getName();
		typeNumber = type.getTypeNumber();		
		configId = type.getConfiguration().getId();
	}
	
	public ObjectType toObjectType() {
		
		ObjectType type = new ObjectType();
		type.setId(dbId);
		type.setName(name);
		type.setTypeNumber(typeNumber);
				
		return type;
		
	}
	
}
