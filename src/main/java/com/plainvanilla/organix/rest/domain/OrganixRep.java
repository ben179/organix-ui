package com.plainvanilla.organix.rest.domain;

import org.springframework.hateoas.ResourceSupport;

import com.plainvanilla.organix.engine.model.OrganixEntity;

public abstract class OrganixRep<T extends OrganixEntity> extends ResourceSupport {
	
	public abstract OrganixEntity toEntity();
	public abstract void fromEntity(T entity);
}
