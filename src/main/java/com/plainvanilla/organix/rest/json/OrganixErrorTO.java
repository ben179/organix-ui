package com.plainvanilla.organix.rest.json;

import java.util.ArrayList;
import java.util.List;

public class OrganixErrorTO {

	private List<OrganixFieldErrorTO> fieldErrors = new ArrayList<OrganixFieldErrorTO>();
	private String fieldErrorsToString = "";
	private String organixModelErrorMessage;
	
	public void addFieldError(String field, String message) {
		OrganixFieldErrorTO error = new OrganixFieldErrorTO();
		error.setField(field);
		error.setMessage(message);
		fieldErrors.add(error);
		fieldErrorsToString += message + " ";
	}

	public String getOrganixModelErrorMessage() {
		return organixModelErrorMessage;
	}

	public void setOrganixModelErrorMessage(String organixModelErrorMessage) {
		this.organixModelErrorMessage = organixModelErrorMessage;
	}

	public String getFieldErrorsToString() {
		return fieldErrorsToString;
	}

}
