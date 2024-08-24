package com.zapcom.shipmanagement.exceptionhandler;

public class CruiseNotFound extends RuntimeException{
	
	public CruiseNotFound(String msg) {
		super(msg);
	}

}
