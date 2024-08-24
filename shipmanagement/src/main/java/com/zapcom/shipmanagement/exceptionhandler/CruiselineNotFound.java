package com.zapcom.shipmanagement.exceptionhandler;

public class CruiselineNotFound extends RuntimeException{
	
	public CruiselineNotFound(String msg) {
		super(msg);
	}

}
