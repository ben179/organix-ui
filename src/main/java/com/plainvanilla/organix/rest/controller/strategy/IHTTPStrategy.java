package com.plainvanilla.organix.rest.controller.strategy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.plainvanilla.organix.engine.model.OrganixEntity;
import com.plainvanilla.organix.rest.domain.OrganixRep;

public interface IHTTPStrategy<X extends OrganixRep, Y extends OrganixEntity> {
	
	ResponseEntity<X> play(Y entity, HttpServletRequest request);
	
}
