package com.plainvanilla.organix.ui.json;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.plainvanilla.organix.engine.model.Configuration;
import com.plainvanilla.organix.engine.model.Connection;
import com.plainvanilla.organix.engine.model.ObjectInstance;
import com.plainvanilla.organix.engine.model.User;

public class DatabaseMixIn {
	
	@JsonProperty Long id;
	@JsonProperty String name;
	@JsonIgnore Configuration configuration;
	@JsonIgnore Set<ObjectInstance> objects;
	@JsonIgnore Set<Connection> connections;
	@JsonIgnore Set<User> users;
}
