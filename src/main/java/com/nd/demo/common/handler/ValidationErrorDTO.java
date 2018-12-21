package com.nd.demo.common.handler;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO {

	private String error = "Validation Failed";

	private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

	public void addFieldError(String path, String message) {
		FieldErrorDTO fieldErrorDTO = new FieldErrorDTO(path, message);
		fieldErrors.add(fieldErrorDTO);
	}

	public String getError() {
		return error;
	}

	public List<FieldErrorDTO> getFieldErrors() {
		return fieldErrors;
	}
}
