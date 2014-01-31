package com.plainvanilla.organix.rest.domain;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import com.plainvanilla.organix.engine.model.ConnectionType;

public class ConnectionTypeRep extends ResourceSupport implements Serializable {


	private static final long serialVersionUID = 3L;

	private Long dbId;
	private Integer typeNumber;
	private ConnectionEndPointRep sourceEnd;
	private ConnectionEndPointRep targetEnd;
	
	public ConnectionTypeRep() {
		
	}
	
	public ConnectionTypeRep(ConnectionType type) {
		fromConnectionType(type);
	}
	
	public void fromConnectionType(ConnectionType type) {
		dbId = type.getId();
		typeNumber = type.getTypeNumber();
		sourceEnd = new ConnectionEndPointRep(type.getSourceEnd());
		targetEnd = new ConnectionEndPointRep(type.getTargetEnd());
	}
	
	public ConnectionType toConnectionType() {
		ConnectionType type = new ConnectionType();
		
		type.setId(dbId);
		type.setSourceEnd(sourceEnd.toConnectionEndpoint());
		type.setTargetEnd(targetEnd.toConnectionEndpoint());
		type.setTypeNumber(typeNumber);
		return type;
	}
	
	public Long getDbId() {
		return dbId;
	}
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	public Integer getTypeNumber() {
		return typeNumber;
	}
	public void setTypeNumber(Integer typeNumber) {
		this.typeNumber = typeNumber;
	}
	public ConnectionEndPointRep getSourceEnd() {
		return sourceEnd;
	}
	public void setSourceEnd(ConnectionEndPointRep sourceEnd) {
		this.sourceEnd = sourceEnd;
	}
	public ConnectionEndPointRep getTargetEnd() {
		return targetEnd;
	}
	public void setTargetEnd(ConnectionEndPointRep targetEnd) {
		this.targetEnd = targetEnd;
	}
	
	
}
