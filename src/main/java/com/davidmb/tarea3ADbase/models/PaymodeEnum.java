package com.davidmb.tarea3ADbase.models;

public enum PaymodeEnum {
	TARJETA('T'),
	EFECTIVO('E'),
	BIZUM('B');
	
	private final char payMode;
	
	PaymodeEnum(char paymode) {
		this.payMode = paymode;
    }
	
	public char getPayMode() {
		return payMode;
	}
}
