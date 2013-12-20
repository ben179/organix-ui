package com.plainvanilla.organix.ui.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.plainvanilla.organix.engine.model.Configuration;
import com.plainvanilla.organix.engine.model.Connection;
import com.plainvanilla.organix.engine.model.ConnectionType;
import com.plainvanilla.organix.engine.model.Database;
import com.plainvanilla.organix.engine.model.ObjectInstance;
import com.plainvanilla.organix.engine.model.ObjectType;
import com.plainvanilla.organix.engine.services.ConfigurationService;
import com.plainvanilla.organix.engine.services.DatabaseService;


@Controller
public class OrganixController {

	
	private DatabaseService databaseService;
	private ConfigurationService configurationService;
	
	@Autowired
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	@Autowired
	public void setDatabaseConfigurationService(
			ConfigurationService databaseConfigurationService) {
		this.configurationService = databaseConfigurationService;
	}
	
	
	@RequestMapping(value="/database/{dbId}/objectInstance", method=RequestMethod.GET)
	public @ResponseBody List<? extends ObjectInstance> getObjectInstances(@PathVariable Long dbId) {
		return databaseService.getObjectInstances(dbId);
	}

	@RequestMapping(value="/database/{dbId}/connection", method=RequestMethod.GET)
	public @ResponseBody List<? extends Connection> getConnections(@PathVariable Long dbId) {
		return databaseService.getConnectionInstances(dbId);
	}
	
	@RequestMapping(value="/database/{dbId}/objectInstance/{typeId}", method=RequestMethod.GET)
	public @ResponseBody List<? extends ObjectInstance> getObjectInstances(@PathVariable Long dbId, @PathVariable Integer typeId) {
		return databaseService.findObjectsByTypeId(typeId, dbId);
	}
	
	@RequestMapping(value="/database/{dbId}/connection/{typeId}", method=RequestMethod.GET)
	public @ResponseBody List<? extends Connection> getConnections(@PathVariable Long dbId, @PathVariable Integer typeId) {
		return databaseService.findConnectionsByTypeId(typeId, dbId);
	}
	
	@RequestMapping(value="/database/{dbId}/objectInstance", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ObjectInstance createObjectInstance(@PathVariable Long dbId, ObjectInstance instance, HttpServletResponse response) {

		//TODO: validation
		return databaseService.addObjectInstance(instance.getType(), instance.getName(), dbId);
	}

	@RequestMapping(value="/database/{dbId}/connection", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Connection createConnection(@PathVariable Long dbId, Connection connection, HttpServletResponse response) {

		//TODO: validation
		return databaseService.addConnection(connection.getType().getTypeNumber(), connection.getSourceObject(), connection.getTargetObject(), dbId);
	}
	
	@RequestMapping(value="/database/{dbId}/connection", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeConnection(@PathVariable Long dbId, Connection instance) {
		databaseService.removeConnectionById(instance.getId(), dbId);
	}
	
	@RequestMapping(value="/database/{dbId}/objectInstance", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeObjectInstance(@PathVariable Long dbId, ObjectInstance instance) {
		databaseService.removeObjectInstanceById(instance.getId(), dbId);
	}
	
	

	@RequestMapping(value="/configuration/{configId}/objectType/{id}", method=RequestMethod.GET)
	public @ResponseBody ObjectType getObjectType(@PathVariable Long configId, @PathVariable Integer id) {
		return configurationService.getObjectType(id, configId);
	}

	@RequestMapping(value="/configuration/{configId}/objectType", method=RequestMethod.GET)
	public @ResponseBody List<ObjectType> getObjectType(@PathVariable Long configId) {
		return configurationService.getAllObjectTypes(configId);
	}
	
	
	@RequestMapping(value="/configuration/{configId}/objectType", method=RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ObjectType createObjectType(@PathVariable Long configId, @RequestBody @Validated ObjectType type, HttpServletResponse response) {
		return configurationService.addObjectType(type.getTypeNumber(), type.getName(), configId);
	}

	
	@RequestMapping(value="/configuration/{configId}/connectionType/{id}", method=RequestMethod.GET)
	public @ResponseBody ConnectionType getConnectionType(@PathVariable Long configId, @PathVariable Integer id) {
		return configurationService.getConnectionType(id, configId);
	}	
	
	
	@RequestMapping(value="/configuration/{configId}", method=RequestMethod.GET, produces = {"application/json"}, consumes = {"application/json"})
	public @ResponseBody Configuration createConfiguration(@PathVariable Long configId) {
		return configurationService.getConfiguration(configId);
	}
	
	@RequestMapping(value="/configuration", method=RequestMethod.POST, produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"})
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody String createConfiguration(@RequestParam String name) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Configuration.class, ConfigurationMixIn.class);
		Configuration config = configurationService.createNewConfiguration(name);
		return mapper.writeValueAsString(config);
	}
	
	@RequestMapping(value="/configuration/{configId}/database", method=RequestMethod.POST, produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"})
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody String createDatabase(@RequestParam String dbName, @PathVariable Long configId) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Database.class, DatabaseMixIn.class);
		Database db = configurationService.createNewDatabase(dbName, configId); 
		return mapper.writeValueAsString(db);
	}
	
	@RequestMapping(value="/configuration/{configId}/connectionType", method=RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ConnectionType createConnectionType(@PathVariable Long configId, @RequestBody @Validated ConnectionType type, HttpServletResponse response) {
		return configurationService.addConnectionType(type.getTypeNumber(), type.getSourceEnd().getRoleName(), type.getSourceEnd().getObjectType(), type.getSourceEnd().getUnique(), type.getSourceEnd().getMandatory(), type.getTargetEnd().getRoleName(), type.getTargetEnd().getObjectType(), type.getTargetEnd().getUnique(), type.getTargetEnd().getMandatory(), configId);
	}
	
}
