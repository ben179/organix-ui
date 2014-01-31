package com.plainvanilla.organix.rest.json;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.plainvanilla.organix.engine.model.Configuration;
import com.plainvanilla.organix.engine.model.ConnectionEndpoint;

public interface ConnectionTypeMixIn {

	@JsonProperty public Long getId();
	@JsonIgnore public Configuration getConfiguration();
	@JsonProperty public Integer getTypeNumber();
	@JsonProperty public ConnectionEndpoint getSourceEnd();
	@JsonProperty public ConnectionEndpoint getTargetEnd();
		
	@JsonIgnore public String getSrcRoleName();
	@JsonIgnore public Boolean getSrcMandatory();
	@JsonIgnore public Boolean getSrcUnique();
	@JsonIgnore public Integer getSrcObjType();	
	@JsonIgnore public String getTrgRoleName();	
	@JsonIgnore public Boolean getTrgMandatory();
	@JsonIgnore public Boolean getTrgUnique();
	@JsonIgnore public Integer getTrgObjType();
//	@JsonIgnore Boolean chosen;
}
