package com.plainvanilla.organix.ui.json;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.plainvanilla.organix.engine.model.Configuration;

public class ObjectTypeMixIn {

	@JsonProperty Long id;
	@JsonIgnore Configuration configuration;	
	@JsonProperty Integer typeNumber;
	@JsonProperty String name;
	
	@JsonProperty Boolean chosen;
}
