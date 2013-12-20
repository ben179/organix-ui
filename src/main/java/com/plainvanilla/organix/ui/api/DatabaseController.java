package com.plainvanilla.organix.ui.api;

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
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.plainvanilla.organix.engine.model.Connection;
import com.plainvanilla.organix.engine.model.ObjectInstance;
import com.plainvanilla.organix.engine.services.DatabaseService;


@Controller
@RequestMapping("/database")
public class DatabaseController {

	private DatabaseService databaseService;
	private MappingJacksonJsonView databaseView;
	
	@Autowired
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	@Autowired
	public void setJacksonView(MappingJacksonJsonView databaseView) {
		this.databaseView = databaseView;
	}

	@RequestMapping(value="/{dbId}/objectInstance", method=RequestMethod.GET)
	public @ResponseBody List<? extends ObjectInstance> getObjectInstances(@PathVariable Long dbId) {
		return databaseService.getObjectInstances(dbId);
	}

	@RequestMapping(value="/{dbId}/connection", method=RequestMethod.GET)
	public @ResponseBody List<? extends Connection> getConnections(@PathVariable Long dbId) {
		return databaseService.getConnectionInstances(dbId);
	}
	
	@RequestMapping(value="/{dbId}/objectInstance/{typeId}", method=RequestMethod.GET)
	public @ResponseBody List<? extends ObjectInstance> getObjectInstances(@PathVariable Long dbId, @PathVariable Integer typeId) {
		return databaseService.findObjectsByTypeId(typeId, dbId);
	}
	
	@RequestMapping(value="/{dbId}/connection/{typeId}", method=RequestMethod.GET)
	public @ResponseBody List<? extends Connection> getConnections(@PathVariable Long dbId, @PathVariable Integer typeId) {
		return databaseService.findConnectionsByTypeId(typeId, dbId);
	}
	
	@RequestMapping(value="/{dbId}/objectInstance", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ObjectInstance createObjectInstance(@PathVariable Long dbId, ObjectInstance instance, HttpServletResponse response) {

		//TODO: validation
		return databaseService.addObjectInstance(instance.getType(), instance.getName(), dbId);
	}

	@RequestMapping(value="/{dbId}/connection", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Connection createConnection(@PathVariable Long dbId, Connection connection, HttpServletResponse response) {

		//TODO: validation
		return databaseService.addConnection(connection.getType().getTypeNumber(), connection.getSourceObject(), connection.getTargetObject(), dbId);
	}
	
	@RequestMapping(value="/{dbId}/connection", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeConnection(@PathVariable Long dbId, Connection instance) {
		databaseService.removeConnectionById(instance.getId(), dbId);
	}
	
	@RequestMapping(value="/{dbId}/objectInstance", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeObjectInstance(@PathVariable Long dbId, ObjectInstance instance) {
		databaseService.removeObjectInstanceById(instance.getId(), dbId);
	}
	
}
