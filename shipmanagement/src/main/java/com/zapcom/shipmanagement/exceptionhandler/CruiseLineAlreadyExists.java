package com.zapcom.shipmanagement.exceptionhandler;

public class CruiseLineAlreadyExists extends RuntimeException {

	public CruiseLineAlreadyExists(String msg) {
		super(msg);
	}

}
