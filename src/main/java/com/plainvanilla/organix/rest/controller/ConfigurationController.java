package com.plainvanilla.organix.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.plainvanilla.organix.rest.domain.ConfigurationRep;
import com.plainvanilla.organix.rest.domain.ObjectTypeRep;


@Controller
@RequestMapping(value="/configuration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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

	private static String md5(Object input) {		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(input.toString().getBytes());
			return new String(md.digest());		
		} catch (Exception e) {
			e.printStackTrace();
			return input.toString();
		}
	}
	
	private static String getEtag(Object input) {
		return "\"" + md5(input) + "\"";
	}
	

	
	/* Object Types */

	@RequestMapping(value="/{configId}/objectType/{objId}", method=RequestMethod.GET)
	public ResponseEntity<ObjectTypeRep> getObjectType(@PathVariable Long configId, @PathVariable Long objId) {
		
		ObjectType type = configurationService.getObjectType(objId, configId);
		
		ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(ConfigurationController.class).getObjectType(configId, objId));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(selfLinkBuilder.toUri());
		headers.setETag(getEtag(type));
		
		ObjectTypeRep representation = new ObjectTypeRep(type);
		representation.add(selfLinkBuilder.withSelfRel());

		return new ResponseEntity<ObjectTypeRep>(representation, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{configId}/objectType/{objId}", method=RequestMethod.PUT)
	public ResponseEntity<ObjectTypeRep> updateObjectType(@PathVariable Long configId, @PathVariable Long objId, @RequestBody ObjectTypeRep type, HttpServletRequest request) {
		
		String etag = request.getHeader("If-Match");
		if (etag == null) {
			return new ResponseEntity<ObjectTypeRep>(null, null, HttpStatus.FORBIDDEN);
		}
		
		ObjectType t = configurationService.getObjectType(objId, configId);
		
		if (!etag.equals(getEtag(t))) {
			return new ResponseEntity<ObjectTypeRep>(null, null, HttpStatus.PRECONDITION_FAILED);
		}
		
		ObjectType updated = configurationService.updateObjectType(type.toObjectType());
				
		ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(ConfigurationController.class).getObjectType(configId, objId));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(selfLinkBuilder.toUri());
		headers.setETag(getEtag(updated));
		
		ObjectTypeRep representation = new ObjectTypeRep(updated);
		representation.add(selfLinkBuilder.withSelfRel());

		return new ResponseEntity<ObjectTypeRep>(representation, headers, HttpStatus.OK);
	}

	@RequestMapping(value="/{configId}/objectType/{objId}", method=RequestMethod.DELETE)
	public ResponseEntity<ObjectTypeRep> updateObjectType(@PathVariable Long configId, @PathVariable Long objId, HttpServletRequest request) {
		
		String etag = request.getHeader("If-Match");
		if (etag == null) {
			return new ResponseEntity<ObjectTypeRep>(null, null, HttpStatus.FORBIDDEN);
		}
		
		ObjectType t = configurationService.getObjectType(objId, configId);
		
		if (!etag.equals(getEtag(t))) {
			return new ResponseEntity<ObjectTypeRep>(null, null, HttpStatus.PRECONDITION_FAILED);
		}
		
		ObjectType updated = configurationService.updateObjectType(type.toObjectType());
				
		ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(ConfigurationController.class).getObjectType(configId, objId));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(selfLinkBuilder.toUri());
		headers.setETag(getEtag(updated));
		
		ObjectTypeRep representation = new ObjectTypeRep(updated);
		representation.add(selfLinkBuilder.withSelfRel());

		return new ResponseEntity<ObjectTypeRep>(representation, headers, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/{configId}/objectType", method=RequestMethod.GET)
	public @ResponseBody List<ObjectType> getObjectType(@PathVariable Long configId) {
		return configurationService.getAllObjectTypes(configId);
	}
	
	
	@RequestMapping(value="/{configId}/objectType", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ObjectTypeRep> createObjectType(@PathVariable Long configId, @RequestBody ObjectTypeRep type) {

		ObjectType t = configurationService.addObjectType(type.getTypeNumber(), type.getName(), configId);
		
		ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(ConfigurationController.class).createObjectType(configId, type)).slash(t.getId()); 

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(selfLinkBuilder.toUri());
			/*
		ObjectTypeRep representation = new ObjectTypeRep(t);
		representation.add(selfLinkBuilder.withSelfRel());*/
		
		return new ResponseEntity<ObjectTypeRep>(null, headers, HttpStatus.CREATED);

	}

	
	/* Connection Type */
	
	@RequestMapping(value="/{configId}/connectionType/{id}", method=RequestMethod.GET)
	public View getConnectionType(@PathVariable Long configId, @PathVariable Integer id, Model model) {
		ConnectionType t = configurationService.getConnectionType(id, configId);
		model.addAttribute(t);
		return configurationView;
	}	

	@RequestMapping(value="/{configId}/connectionType", method=RequestMethod.POST, consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public View createConnectionType(@PathVariable Long configId, @RequestBody @Validated ConnectionType type, Model model) {
		model.addAttribute(configurationService.addConnectionType(type.getTypeNumber(), type.getSourceEnd().getRoleName(), type.getSourceEnd().getObjectType(), type.getSourceEnd().getUnique(), type.getSourceEnd().getMandatory(), type.getTargetEnd().getRoleName(), type.getTargetEnd().getObjectType(), type.getTargetEnd().getUnique(), type.getTargetEnd().getMandatory(), configId));
		return configurationView;
	}
	
	/* Configuration */
	
	@RequestMapping(method=RequestMethod.GET)
	public View getAllConfigurations(@RequestParam(required=false) boolean headersOnly, Model model) {
		List<Configuration> configurations = configurationService.getAllConfigurations(headersOnly);
		model.addAttribute(configurations);
		return configurationView;
	}
	
	@RequestMapping(value="/{configId}", method=RequestMethod.GET)
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
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public View createConfiguration(@RequestParam String name, Model model, HttpServletResponse response) {
		Configuration config = configurationService.createNewConfiguration(name);
		model.addAttribute(config);
		return configurationView;
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ConfigurationRep> importConfiguration(@RequestParam("ConfigFile") MultipartFile file, UriComponentsBuilder b) throws IOException {
		Configuration config = configurationObjectMapper.readValue(file.getBytes(), Configuration.class);
		return uploadConfiguration(new ConfigurationRep(config), b);
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ConfigurationRep> uploadConfiguration(@RequestBody @Validated ConfigurationRep config, UriComponentsBuilder b) {
		
		Configuration uploaded = configurationService.importConfiguration(config.toConfiguration());

		UriComponents c = b.path("/configuration/{id}").buildAndExpand(uploaded.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(c.toUri());
		return new ResponseEntity<ConfigurationRep>(new ConfigurationRep(uploaded), headers, HttpStatus.CREATED);
	}
	
	/* Database */
	
	@RequestMapping(value="/{configId}/database", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public View createDatabase(@RequestParam String dbName, @PathVariable Long configId, Model model) {
		Database db = configurationService.createNewDatabase(dbName, configId); 
		model.addAttribute(db);
		return configurationView;
	}
	
	
}
