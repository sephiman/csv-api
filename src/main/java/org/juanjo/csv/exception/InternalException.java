package org.juanjo.csv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InternalException that will return 500 Internal Server Error
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalException extends Exception {

	public InternalException(String message) {
		super(message);
	}
}
