package com.hodolog.api.exception.shop;

import com.hodolog.api.exception.HodologException;

public class AlreadyExistsNameException extends HodologException {
	private static final String MESSAGE = "이미 존재하는 회원입니다.";
	
	public AlreadyExistsNameException() {
		super(MESSAGE);
	}
	
	@Override
	public int getStatusCode() {
		return 400;
	}
}
