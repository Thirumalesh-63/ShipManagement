package com.zapcom.shipmanagement.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ShipNotfound.class)
	public ResponseEntity<String> shipNotFound(ShipNotfound exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler(CruiseNotFound.class)
	public ResponseEntity<String> cruiseNotFound(CruiseNotFound exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler(CruiselineNotFound.class)
	public ResponseEntity<String> cruiseNotFound(CruiselineNotFound exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler(CruiseLineAlreadyExists.class)
	public ResponseEntity<String> cruiselineAlreadyExists(CruiseLineAlreadyExists exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	// Handle Unauthorized Access Exceptions
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleUnauthorizedAccess(RuntimeException ex) {
		System.err.println("RuntimeException: " + ex.getMessage());

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

}
