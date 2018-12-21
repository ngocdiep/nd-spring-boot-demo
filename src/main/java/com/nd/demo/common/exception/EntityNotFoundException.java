package com.nd.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1955348830664535172L;

	public EntityNotFoundException(Class<?> entity, Long id) {
		this("Could not find Entity of type: " + entity.getSimpleName() + " with id: " + id);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

}
