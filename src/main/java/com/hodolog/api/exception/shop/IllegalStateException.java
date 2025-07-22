package com.hodolog.api.exception.shop;

import com.hodolog.api.exception.HodologException;

public class IllegalStateException extends HodologException {
	private static final String MESSAGE = "적합하지 않은 상태입니다.";
	
	public IllegalStateException() {
		super(MESSAGE);
	}

	public IllegalStateException(String message) {
		super(MESSAGE);
	}
	
	@Override
	public int getStatusCode() {
		return 409;  // Conflict
	}
}
