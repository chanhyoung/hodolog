package com.hodolog.api.exception.shop;

import com.hodolog.api.exception.HodologException;

public class ItemNotFound extends HodologException {
	private static final String MESSAGE = "존재하지 않는 상품입니다.";
	
	public ItemNotFound() {
		super(MESSAGE);
	}

	public ItemNotFound(String message) {
		super(message);
	}
	
	@Override
	public int getStatusCode() {
		return 404;
	}
}
