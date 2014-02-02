package com.plainvanilla.organix.rest.controller.fixture;

import com.plainvanilla.organix.rest.domain.ConfigurationRep;

public class RESTData {
	
	public static ConfigurationRep getConfiguration() {
		ConfigurationRep c = new ConfigurationRep();
		
		c.setName("testConfig");
		c.setVersion(1);

		
		return c;
	}

}
