package com.nd.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 5653548927420945487L;

	public InvalidInputException(String message) {
		super(message);
	}

	public InvalidInputException(String message, Exception exception) {
		super(message, exception);
	}

}
