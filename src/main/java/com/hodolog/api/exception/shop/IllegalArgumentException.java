package com.hodolog.api.exception.shop;

import com.hodolog.api.exception.HodologException;

public class IllegalArgumentException extends HodologException {
	private static final String MESSAGE = "적합하지 argument 값입니다.";
	
	public IllegalArgumentException() {
		super(MESSAGE);
	}

	public IllegalArgumentException(String message) {
		super(MESSAGE);
	}
	
	@Override
	public int getStatusCode() {
		return 409;  // Conflict
	}
}
