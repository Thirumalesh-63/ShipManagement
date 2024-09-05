package com.zapcom.shipmanagement.exceptionhandler;

public class ShipAlreadyExists  extends RuntimeException{
	
	public ShipAlreadyExists(String msg){
		super(msg);
	}

}
