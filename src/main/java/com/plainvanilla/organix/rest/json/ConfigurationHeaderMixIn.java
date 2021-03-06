package com.plainvanilla.organix.rest.json;

import java.util.Date;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.plainvanilla.organix.engine.model.ConnectionType;
import com.plainvanilla.organix.engine.model.Database;
import com.plainvanilla.organix.engine.model.ObjectType;

public class ConfigurationHeaderMixIn {

	@JsonProperty Long id;
	@JsonProperty Date configurationDate;
	@JsonProperty Date lastUpdateDate;
	@JsonProperty String name;
	@JsonProperty Integer version;
	@JsonIgnore Set<ConnectionType> connectionTypes;
	@JsonIgnore Set<ObjectType> objectTypes;
	@JsonIgnore Set<Database> databases;	
}
