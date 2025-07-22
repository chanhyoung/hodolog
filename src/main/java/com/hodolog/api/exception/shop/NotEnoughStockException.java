package com.hodolog.api.exception.shop;

import com.hodolog.api.exception.HodologException;

public class NotEnoughStockException extends HodologException {
	private static final String MESSAGE = "재고가 부족합니다.";
	
	public NotEnoughStockException() {
		super(MESSAGE);
	}

	public NotEnoughStockException(String message) {
		super(MESSAGE);
	}
	
	@Override
	public int getStatusCode() {
		return 409;  // Conflict
	}
}
