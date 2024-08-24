package com.zapcom.shipmanagement.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ShipNotfound.class)
	public ResponseEntity<String> shipnotfound(ShipNotfound exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}
	@ExceptionHandler(CruiseNotFound.class)
	public ResponseEntity<String> cruisenotfound(CruiseNotFound exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}
	@ExceptionHandler(CruiselineNotFound.class)
	public ResponseEntity<String> cruisenotfound(CruiselineNotFound exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

}
