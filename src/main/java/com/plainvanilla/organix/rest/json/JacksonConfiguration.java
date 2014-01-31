package com.plainvanilla.organix.rest.json;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.plainvanilla.organix.engine.model.ConnectionType;
import com.plainvanilla.organix.engine.model.Database;
import com.plainvanilla.organix.engine.model.ObjectType;

@Configuration
public class JacksonConfiguration {

	@Bean
	public MappingJacksonJsonView databaseView() {
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		// TODO: add mixins for Database Entities
		return view;
	}

	@Bean
	public MappingJacksonJsonView configurationView() {
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setObjectMapper(configurationObjectMapper());
		return view;
	}

	@Bean
	public ObjectMapper configurationObjectMapper() {

		ObjectMapper mapper = new ObjectMapper();
		SerializationConfig sConfig = mapper.getSerializationConfig();

		sConfig.addMixInAnnotations(ConnectionType.class, ConnectionTypeMixIn.class);
		sConfig.addMixInAnnotations(ObjectType.class, ObjectTypeMixIn.class);
		sConfig.addMixInAnnotations(Configuration.class, ConfigurationMixIn.class);
		sConfig.addMixInAnnotations(Database.class, DatabaseMixIn.class);

		DeserializationConfig dConfig = mapper.getDeserializationConfig();
		dConfig.addMixInAnnotations(ConnectionType.class, ConnectionTypeMixIn.class);
		dConfig.addMixInAnnotations(ObjectType.class, ObjectTypeMixIn.class);
		dConfig.addMixInAnnotations(Configuration.class, ConfigurationMixIn.class);
		dConfig.addMixInAnnotations(Database.class, DatabaseMixIn.class);

		return mapper;
	}
}
