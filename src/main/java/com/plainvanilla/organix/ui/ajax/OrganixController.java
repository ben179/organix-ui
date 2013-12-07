package com.plainvanilla.organix.ui.ajax;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.plainvanilla.organix.engine.model.Connection;
import com.plainvanilla.organix.engine.model.ConnectionType;
import com.plainvanilla.organix.engine.model.ObjectInstance;
import com.plainvanilla.organix.engine.model.ObjectType;
import com.plainvanilla.organix.engine.services.DatabaseConfigurationService;
import com.plainvanilla.organix.engine.services.DatabaseService;


@Controller
public class OrganixController {

	
	private DatabaseService databaseService;
	private DatabaseConfigurationService databaseConfigurationService;
	
	@Autowired
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	@Autowired
	public void setDatabaseConfigurationService(
			DatabaseConfigurationService databaseConfigurationService) {
		this.databaseConfigurationService = databaseConfigurationService;
	}
	
	
	@RequestMapping(value="/objectInstance", method=RequestMethod.GET)
	public @ResponseBody List<? extends ObjectInstance> getObjectInstances() {
		return databaseService.getObjectInstances();
	}

	@RequestMapping(value="/connection", method=RequestMethod.GET)
	public @ResponseBody List<? extends Connection> getConnections() {
		return databaseService.getConnectionInstances();
	}
	
	@RequestMapping(value="/objectInstance/{typeId}", method=RequestMethod.GET)
	public @ResponseBody List<? extends ObjectInstance> getObjectInstances(@PathVariable Integer typeId) {
		return databaseService.findObjectsByTypeId(typeId);
	}
	
	@RequestMapping(value="/connection/{typeId}", method=RequestMethod.GET)
	public @ResponseBody List<? extends Connection> getConnections(@PathVariable Integer typeId) {
		return databaseService.findConnectionsByTypeId(typeId);
	}
	
	@RequestMapping(value="/objectInstance", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ObjectInstance createObjectInstance(ObjectInstance instance, HttpServletResponse response) {

		//TODO: validation
		return databaseService.addObjectInstance(instance.getType(), instance.getName());
	}

	@RequestMapping(value="/connection", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Connection createConnection(Connection connection, HttpServletResponse response) {

		//TODO: validation
		return databaseService.addConnection(connection.getType().getTypeNumber(), connection.getSourceObject(), connection.getTargetObject());
	}
	
	@RequestMapping(value="/connection", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeConnection(Connection instance) {
		databaseService.removeConnectionById(instance.getId());
	}
	
	@RequestMapping(value="/objectInstance", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeObjectInstance(ObjectInstance instance) {
		databaseService.removeObjectInstanceById(instance.getId());
	}
	
	

	@RequestMapping(value="/objectType/{id}", method=RequestMethod.GET)
	public @ResponseBody ObjectType getObjectType(@PathVariable Integer id) {
		return databaseConfigurationService.getObjectType(id);
	}
	
	@RequestMapping(value="/objectType", method=RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ObjectType createObjectType(@RequestBody @Validated ObjectType type, HttpServletResponse response) {
		return databaseConfigurationService.addObjectType(type.getTypeNumber(), type.getName());
	}

	
	@RequestMapping(value="/connectionType/{id}", method=RequestMethod.GET)
	public @ResponseBody ConnectionType getConnectionType(@PathVariable Integer id) {
		return databaseConfigurationService.getConnectionType(id);
	}	
	
	
	@RequestMapping(value="/connectionType", method=RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ConnectionType createConnectionType(@RequestBody @Validated ConnectionType type, HttpServletResponse response) {
		return databaseConfigurationService.addConnectionType(type.getTypeNumber(), type.getSourceEnd().getRoleName(), type.getSourceEnd().getObjectType(), type.getSourceEnd().getUnique(), type.getSourceEnd().getMandatory(), type.getTargetEnd().getRoleName(), type.getTargetEnd().getObjectType(), type.getTargetEnd().getUnique(), type.getTargetEnd().getMandatory());
	}
	
}
