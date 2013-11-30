package com.plainvanilla.organix.ui.ajax;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.plainvanilla.organix.engine.model.ConnectionType;
import com.plainvanilla.organix.engine.model.ObjectType;
import com.plainvanilla.organix.engine.services.DatabaseConfigurationService;
import com.plainvanilla.organix.engine.services.DatabaseService;


@Controller
@RequestMapping(value="/organix")
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
	
	@RequestMapping(value="/objectType", method=RequestMethod.GET)
	public @ResponseBody List<? extends ObjectType> getObjectTypes(HttpServletResponse response) {
		return databaseConfigurationService.getObjectTypes();
	}

	@RequestMapping(value="/objectType/{id}", method=RequestMethod.GET)
	public @ResponseBody ObjectType getObjectType(@PathVariable Integer id) {
		return databaseConfigurationService.getObjectType(id);
	}
	
	@RequestMapping(value="/objectType", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ObjectType createObjectType(ObjectType type, HttpServletResponse response) {
		
		// TODO: validation
		return databaseConfigurationService.addObjectType(type.getTypeNumber(), type.getName());
	}
	
	@RequestMapping(value="/connectionType", method=RequestMethod.GET)
	public @ResponseBody List<? extends ConnectionType> getConnectionTypes(HttpServletResponse response) {
		return databaseConfigurationService.getConnectionTypes();
	}
	
	@RequestMapping(value="/connectionType/{id}", method=RequestMethod.GET)
	public @ResponseBody ConnectionType getConnectionType(@PathVariable Integer id) {
		return databaseConfigurationService.getConnectionType(id);
	}	
	
	
	@RequestMapping(value="/connectionType", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ConnectionType createConnectionType(ConnectionType type, HttpServletResponse response) {
		
		// TODO: validation
		return databaseConfigurationService.addConnectionType(type.getTypeNumber(), type.getSourceEnd().getRoleName(), type.getSourceEnd().getObjectType(), type.getSourceEnd().getUnique(), type.getSourceEnd().getMandatory(), type.getTargetEnd().getRoleName(), type.getTargetEnd().getObjectType(), type.getTargetEnd().getUnique(), type.getTargetEnd().getMandatory());
	}
	
}
