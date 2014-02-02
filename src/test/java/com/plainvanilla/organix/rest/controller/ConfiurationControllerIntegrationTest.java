package com.plainvanilla.organix.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static org.mockito.Mockito.*;


import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import com.plainvanilla.organix.engine.model.Configuration;
import com.plainvanilla.organix.engine.services.ConfigurationService;
import com.plainvanilla.organix.rest.controller.fixture.RESTData;

public class ConfiurationControllerIntegrationTest {
	
	MockMvc mockMvc;
	
	@Mock
	ConfigurationService service;
	
	@InjectMocks
	ConfigurationController controller;
	
	UUID key = UUID.fromString("f3512d26-72f6-4290-9265-63ad69eccc13");
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = standaloneSetup(controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}
	
	@Test
	public void testThatNoConfigurationExists() throws Exception {
		
		
		when(service.getConfiguration(any(Long.class))).thenReturn(null);
		this.mockMvc.perform(get("/configuration/1")
				.accept(MediaType.APPLICATION_JSON)
				.content("")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				
		.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void testConfigurationCreate() throws Exception {

	    when(service.importConfiguration(any(Configuration.class)))
        .thenReturn(RESTData.getConfiguration().toEntity());
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(RESTData.getConfiguration());
		
		this.mockMvc.perform(post("/configuration")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
				
	}

}
