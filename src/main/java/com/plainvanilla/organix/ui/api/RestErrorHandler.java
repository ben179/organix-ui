package com.plainvanilla.organix.ui.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.plainvanilla.organix.engine.model.exception.OrganixModelException;
import com.plainvanilla.organix.ui.json.OrganixErrorTO;

@ControllerAdvice
public class RestErrorHandler {

	private MessageSource messageSource;

	@Autowired
	public RestErrorHandler(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public OrganixErrorTO processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		OrganixErrorTO err = processFieldErrors(fieldErrors);
		return err;
	}

	@ExceptionHandler(OrganixModelException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public OrganixErrorTO processValidationError(OrganixModelException ex) {
		OrganixErrorTO err = new OrganixErrorTO();
		err.setOrganixModelErrorMessage(ex.getMessage());
		return err;
	}
		
	private OrganixErrorTO processFieldErrors(List<FieldError> fieldErrors) {
		OrganixErrorTO dto = new OrganixErrorTO();

        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }
        return dto;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        /*
    	Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }

        return localizedErrorMessage */
        return fieldError.getDefaultMessage();
    }
	
}
