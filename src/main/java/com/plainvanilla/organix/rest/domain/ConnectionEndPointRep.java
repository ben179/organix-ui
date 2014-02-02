package com.plainvanilla.organix.rest.domain;

import java.io.Serializable;

import com.plainvanilla.organix.engine.model.ConnectionEndpoint;

public class ConnectionEndPointRep extends OrganixRep<ConnectionEndpoint> implements Serializable {

	private static final long serialVersionUID = 4L;

	private Boolean mandatory;
	private Boolean unique;
	private Integer objectType;
	private String roleName;
	
	public ConnectionEndPointRep() {
		
	}
	
	public ConnectionEndPointRep(ConnectionEndpoint point) {
		fromEntity(point);
	}
	


	@Override
	public ConnectionEndpoint toEntity() {
		ConnectionEndpoint point = new ConnectionEndpoint();
		point.setMandatory(mandatory);
		point.setObjectType(objectType);
		point.setUnique(unique);
		point.setRoleName(roleName);
		
		return point;
	}

	@Override
	public void fromEntity(ConnectionEndpoint endpoint) {
		mandatory = endpoint.getMandatory();
		unique = endpoint.getUnique();
		objectType = endpoint.getObjectType();
		roleName = endpoint.getRoleName();
		
	}
	
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	public Boolean getUnique() {
		return unique;
	}
	public void setUnique(Boolean unique) {
		this.unique = unique;
	}
	public Integer getObjectType() {
		return objectType;
	}
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	

}


