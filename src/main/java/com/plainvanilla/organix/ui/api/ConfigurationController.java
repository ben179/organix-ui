package com.plainvanilla.organix.ui.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.plainvanilla.organix.engine.model.Configuration;
import com.plainvanilla.organix.engine.model.ConnectionType;
import com.plainvanilla.organix.engine.model.Database;
import com.plainvanilla.organix.engine.model.ObjectType;
import com.plainvanilla.organix.engine.services.ConfigurationService;


@Controller
@RequestMapping(value="/configuration", produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"})
public class ConfigurationController {

	private MappingJacksonJsonView configurationView;
	private ObjectMapper configurationObjectMapper;
	private ConfigurationService configurationService;
	
	@Autowired
	public void setConfigurationView(MappingJacksonJsonView configurationView) {
		this.configurationView = configurationView;
	}

	
	@Autowired
	public void setConfigurationObjectMapper(ObjectMapper configurationObjectMapper) {
		this.configurationObjectMapper = configurationObjectMapper;
	}

	@Autowired
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@RequestMapping(method=RequestMethod.GET/*, produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"}*/)
	public View getAllConfigurations(@RequestParam(required=false) boolean headersOnly, Model model) {
		List<Configuration> configurations = configurationService.getAllConfigurations(headersOnly);
		model.addAttribute(configurations);
		return configurationView;
	}
	
	@RequestMapping(value="/{configId}/objectType/{id}", method=RequestMethod.GET)
	public View getObjectType(@PathVariable Long configId, @PathVariable Integer id, Model model) {
		ObjectType type = configurationService.getObjectType(id, configId);
		model.addAttribute(type);
		return configurationView;
	}

	@RequestMapping(value="/{configId}/objectType", method=RequestMethod.GET)
	public @ResponseBody List<ObjectType> getObjectType(@PathVariable Long configId) {
		return configurationService.getAllObjectTypes(configId);
	}
	
	
	@RequestMapping(value="/{configId}/objectType", method=RequestMethod.POST, /*produces = {"application/json"},*/ consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public View createObjectType(@PathVariable Long configId, @RequestBody @Validated ObjectType type, Model model) {
		ObjectType t = configurationService.addObjectType(type.getTypeNumber(), type.getName(), configId);
		model.addAttribute(t);
		return configurationView;
	}

	
	@RequestMapping(value="/{configId}/connectionType/{id}", method=RequestMethod.GET)
	public View getConnectionType(@PathVariable Long configId, @PathVariable Integer id, Model model) {
		ConnectionType t = configurationService.getConnectionType(id, configId);
		model.addAttribute(t);
		return configurationView;
	}	
	
	
	@RequestMapping(value="/{configId}", method=RequestMethod.GET /*, produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"}*/)
	public View getConfiguration(@PathVariable Long configId, Model model) {
		Configuration config = configurationService.getConfiguration(configId);
		model.addAttribute(config);
		return configurationView;
	}

	@RequestMapping(value="/{configId}/file", method=RequestMethod.GET)
	public @ResponseBody void getConfigurationFile(@PathVariable Long configId, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {

		Configuration config = configurationService.getConfiguration(configId);
		
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition", "attachment;filename=\"organix-config-" + config.getName() + ".json\"");
		
		configurationView.getObjectMapper().writeValue(response.getOutputStream(), config);
	}
	
	@RequestMapping(method=RequestMethod.POST/*, produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"}*/)
	@ResponseStatus(HttpStatus.CREATED)
	public View createConfiguration(@RequestParam String name, Model model, HttpServletResponse response) {
		Configuration config = configurationService.createNewConfiguration(name);
		model.addAttribute(config);
		return configurationView;
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST/*, produces = {"application/json"}*/, consumes = {"multipart/form-data"})
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Configuration> importConfiguration(@RequestParam("ConfigFile") MultipartFile file/*, Model model*/, UriComponentsBuilder b) throws IOException {
		Configuration config = configurationObjectMapper.readValue(file.getBytes(), Configuration.class);
		return uploadConfiguration(config/*, model*/, b);
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes = {"application/json"})
//	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Configuration> uploadConfiguration(@RequestBody @Validated Configuration config/*, Model model*/, UriComponentsBuilder b) {
		Configuration uploaded = configurationService.importConfiguration(config);
		//model.addAttribute(uploaded);

		UriComponents c = b.path("/configuration/{id}").buildAndExpand(uploaded.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(c.toUri());
		return new ResponseEntity<Configuration>(uploaded, headers, HttpStatus.CREATED);
		//return configurationView;
	}
	
	@RequestMapping(value="/{configId}/database", method=RequestMethod.POST/*, produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"}*/)
	@ResponseStatus(HttpStatus.CREATED)
	public View createDatabase(@RequestParam String dbName, @PathVariable Long configId, Model model) {
		Database db = configurationService.createNewDatabase(dbName, configId); 
		model.addAttribute(db);
		return configurationView;
	}
	
	@RequestMapping(value="/{configId}/connectionType", method=RequestMethod.POST/*, produces = {"application/json"}*/, consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public View createConnectionType(@PathVariable Long configId, @RequestBody @Validated ConnectionType type, Model model) {
		model.addAttribute(configurationService.addConnectionType(type.getTypeNumber(), type.getSourceEnd().getRoleName(), type.getSourceEnd().getObjectType(), type.getSourceEnd().getUnique(), type.getSourceEnd().getMandatory(), type.getTargetEnd().getRoleName(), type.getTargetEnd().getObjectType(), type.getTargetEnd().getUnique(), type.getTargetEnd().getMandatory(), configId));
		return configurationView;
	}
	
}
